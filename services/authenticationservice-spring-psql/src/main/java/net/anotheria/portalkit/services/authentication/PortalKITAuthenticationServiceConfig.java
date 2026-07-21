package net.anotheria.portalkit.services.authentication;

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
 * Spring wiring for the PostgreSQL/JPA authentication service, designed to be <b>embedded</b> in a host
 * application (e.g. a Spring Boot service) without disturbing the host's own persistence. Same design as
 * {@code PortalKITAccountServiceConfig} in {@code accountservice-spring-psql}.
 *
 * <p><b>Everything from the outside.</b> This config does <i>not</i> create its own {@code DataSource},
 * {@code EntityManagerFactory}, {@code TransactionManager} or a generic {@code flyway} bean (Spring Boot's
 * auto-config is {@code @ConditionalOnMissingBean}, so a second one would silently take over or clash). It
 * contributes only:
 * <ul>
 *     <li>the authentication JPA repositories (bound to the host's {@code entityManagerFactory} /
 *         {@code transactionManager}),</li>
 *     <li>the {@link AuthenticationServiceImpl} bean ({@link AuthenticationBeans}),</li>
 *     <li>its own <b>namespaced</b> modern-Flyway bean ({@code authFlyway}) that runs the password/token
 *         migrations on the host {@code DataSource}, with its own {@code flyway_authentication} history table.</li>
 * </ul>
 *
 * <p><b>Flyway.</b> The migrations run with modern {@code org.flywaydb} Flyway (provided by the host,
 * typically Spring Boot). The legacy {@code com.googlecode} Flyway 2.0.3 is unsuitable when embedded because
 * it always also scans the standard {@code db/migration} location and would collide with the host's own
 * migrations.
 *
 * <p><b>Host requirement.</b> Because the repositories use the host's shared {@code EntityManagerFactory},
 * the host must include the authentication entity package in its entity scan, e.g.
 * <pre>{@code @EntityScan({ "<host packages>", "net.anotheria.portalkit.services.authentication.persistence" })}</pre>
 * {@link #authEntityManagerDependsOnFlyway()} makes the host EMF depend on the Flyway bean, so with
 * {@code hibernate.ddl-auto=validate} the tables already exist when Hibernate validates the mappings.
 */
@Configuration
@Import(AuthenticationBeans.class)
@EnableJpaRepositories(basePackages = PortalKITAuthenticationServiceConfig.BASE_PACKAGE + ".persistence")
public class PortalKITAuthenticationServiceConfig {

    public static final String BASE_PACKAGE = "net.anotheria.portalkit.services.authentication";

    private static final String AUTH_MIGRATIONS = "classpath:net/anotheria/portalkit/services/authentication/persistence/jdbc/migrations/common";

    /** Default bean name of the (host-provided) EntityManagerFactory that we order after the migrations. */
    private static final String HOST_ENTITY_MANAGER_FACTORY = "entityManagerFactory";

    /**
     * Password + token migrations, run on the host {@link DataSource}. Registered under the service-specific
     * bean name {@code authFlyway} (never the generic {@code flyway}) so it cannot collide with a host
     * application's own Flyway bean. Metadata table {@code flyway_authentication}.
     */
    @Bean(name = "authFlyway", initMethod = "migrate")
    public Flyway authFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(AUTH_MIGRATIONS)
                .table("flyway_authentication")
                // the auth tables share the host database, so the schema is typically non-empty when this
                // runs; baseline (at 0, so V1_0 still applies) instead of failing on a non-empty schema.
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
    }

    /**
     * Makes the host's {@code entityManagerFactory} depend on the auth Flyway bean, so the auth tables exist
     * before Hibernate validates its mappings (when the host uses {@code ddl-auto=validate} with the auth
     * entities on its shared EMF). No-op if the host EMF is not named {@value #HOST_ENTITY_MANAGER_FACTORY}.
     * Declared {@code static} so it runs during bean-factory-post-processing, before any bean is instantiated.
     */
    @Bean
    public static BeanFactoryPostProcessor authEntityManagerDependsOnFlyway() {
        return beanFactory -> addDependsOn(beanFactory, HOST_ENTITY_MANAGER_FACTORY, "authFlyway");
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
