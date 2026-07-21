package net.anotheria.portalkit.services.account;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spring wiring for the PostgreSQL/JPA account service, designed to be <b>embedded</b> in a host
 * application (e.g. a Spring Boot service) without disturbing the host's own persistence.
 *
 * <p><b>Everything from the outside.</b> Unlike the other portalkit JPA services, this config does
 * <i>not</i> create its own {@code DataSource}, {@code EntityManagerFactory}, {@code TransactionManager}
 * or a generic {@code flyway} bean. Spring Boot's {@code DataSourceAutoConfiguration} and JPA
 * auto-config are {@code @ConditionalOnMissingBean}, so the moment this module contributed its own
 * {@code DataSource}/EMF the host would silently lose its primary persistence; and Boot's
 * {@code FlywayAutoConfiguration} also registers a bean literally named {@code flyway}, which clashed
 * with the shared {@code JpaSpringConfiguration#flyway()} bean. Reusing the host's beans avoids all of
 * that. This module therefore contributes only:
 * <ul>
 *     <li>the account JPA repositories (bound to the host's {@code entityManagerFactory} /
 *         {@code transactionManager}),</li>
 *     <li>the {@link AccountServiceImpl} bean ({@link AccountBeans}),</li>
 *     <li>its own <b>namespaced</b> Flyway beans ({@code accountFlyway} / {@code accountFlywayAudit})
 *         that run the account/note and audit migrations on the host {@code DataSource}, with the separate
 *         {@code flyway_account} / {@code flyway_audit_account} metadata tables.</li>
 * </ul>
 *
 * <p><b>Host requirement.</b> Because the repositories use the host's shared {@code EntityManagerFactory},
 * the host must include the account entity package in its entity scan, e.g.
 * <pre>{@code @EntityScan({ "<host packages>", "net.anotheria.portalkit.services.account.persistence" })}</pre>
 * The account tables then live in the host's database. {@link #accountEntityManagerDependsOnFlyway()}
 * makes the host EMF depend on the account Flyway beans, so with {@code hibernate.ddl-auto=validate} the
 * account tables already exist when Hibernate validates the mappings.
 */
@Configuration
@Import(AccountBeans.class)
@EnableJpaRepositories(basePackages = PortalKITAccountServiceConfig.BASE_PACKAGE + ".persistence")
public class PortalKITAccountServiceConfig {

    public static final String BASE_PACKAGE = "net.anotheria.portalkit.services.account";

    private static final String ACCOUNT_MIGRATIONS = "classpath:net/anotheria/portalkit/services/account/persistence/jdbc/migrations/common";
    private static final String AUDIT_MIGRATIONS = "classpath:net/anotheria/portalkit/services/account/persistence/audit/jdbc/migrations/common";

    /** Default bean name of the (host-provided) EntityManagerFactory that we order after the migrations. */
    private static final String HOST_ENTITY_MANAGER_FACTORY = "entityManagerFactory";

    /**
     * Account + note migrations, run on the host {@link DataSource}. Registered under the service-specific
     * bean name {@code accountFlyway} (never the generic {@code flyway}) so it cannot collide with a host
     * application's own Flyway bean. Metadata table {@code flyway_account} matches the legacy service.
     */
    @Bean(name = "accountFlyway", initMethod = "migrate")
    public Flyway accountFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(ACCOUNT_MIGRATIONS)
                .table("flyway_account")
                // the account tables share the host database, so the schema is typically non-empty when
                // this runs; baseline (at 0, so V1_0 still applies) instead of failing on a non-empty schema.
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
    }

    /**
     * Audit migrations, run on the host {@link DataSource} under the separate {@code flyway_audit_account}
     * metadata table (the exact name the legacy audit persistence service computes).
     */
    @Bean(name = "accountFlywayAudit", initMethod = "migrate")
    public Flyway accountFlywayAudit(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(AUDIT_MIGRATIONS)
                .table("flyway_audit_account")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
    }

    /**
     * Makes the host's {@code entityManagerFactory} depend on the account Flyway beans, so the account
     * tables exist before Hibernate validates its mappings (when the host uses {@code ddl-auto=validate}
     * with the account entities on its shared EMF). No-op if the host EMF is not named
     * {@value #HOST_ENTITY_MANAGER_FACTORY}. Declared {@code static} so it runs during the
     * bean-factory-post-processing phase, before any bean is instantiated.
     */
    @Bean
    public static BeanFactoryPostProcessor accountEntityManagerDependsOnFlyway() {
        return beanFactory -> addDependsOn(beanFactory, HOST_ENTITY_MANAGER_FACTORY, "accountFlyway", "accountFlywayAudit");
    }

    private static void addDependsOn(ConfigurableListableBeanFactory beanFactory, String beanName, String... dependencies) {
        if (!beanFactory.containsBeanDefinition(beanName)) {
            return;
        }
        BeanDefinition definition = beanFactory.getBeanDefinition(beanName);
        List<String> merged = new ArrayList<>();
        if (definition.getDependsOn() != null) {
            merged.addAll(Arrays.asList(definition.getDependsOn()));
        }
        for (String dependency : dependencies) {
            if (!merged.contains(dependency)) {
                merged.add(dependency);
            }
        }
        definition.setDependsOn(merged.toArray(new String[0]));
    }
}
