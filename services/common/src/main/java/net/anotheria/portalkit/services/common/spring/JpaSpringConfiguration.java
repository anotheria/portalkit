package net.anotheria.portalkit.services.common.spring;

import com.googlecode.flyway.core.Flyway;
import net.anotheria.portalkit.services.common.flyway.FlywayUtils;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCConfig;
import org.apache.commons.dbcp.BasicDataSource;
import org.configureme.ConfigurationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author bvanchuhov
 */
@Configuration
@EnableTransactionManagement
public class JpaSpringConfiguration {

    protected String getServiceName() {
        throw new UnsupportedOperationException("No implementation for getServiceName()");
    }

    protected String getBasePackage() {
        throw new UnsupportedOperationException("No implementation for getBasePackageName()");
    }

    protected String getJdbcConfigurationName() {
        throw new UnsupportedOperationException("No implementation for getJdbcConfigurationName()");
    }

    protected String getEntityPackagesToScan() {
        return getBasePackage();
    }

    protected String[] getFlywayLocations() {
        return FlywayUtils.getDefaultFlywayLocations(getBasePackage(), getJdbcConfig().getDriver());
    }

    protected String getTableNameForMigration() {
        return FlywayUtils.getDefaultTableNameForMigration(getServiceName());
    }

    public JDBCConfig getJdbcConfig() {
        JDBCConfig jdbcConfig = new JDBCConfig();
        ConfigurationManager.INSTANCE.configureAs(jdbcConfig, getJdbcConfigurationName());
        return jdbcConfig;
    }

    @Bean(destroyMethod="close")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        JDBCConfig jdbcConfig = getJdbcConfig();
        dataSource.setDriverClassName(jdbcConfig.getDriver());
        dataSource.setUrl(jdbcConfig.getUrl());
        dataSource.setUsername(jdbcConfig.getUsername());
        dataSource.setPassword(jdbcConfig.getPassword());
        dataSource.setValidationQuery("SELECT 1");

        return dataSource;
    }

    @Bean
    public Database database() {
        return DBUtils.getDatabase(getJdbcConfig().getDriver());
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(Database database) {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(database);
        jpaVendorAdapter.setDatabasePlatform(DBUtils.getHibernateDialect(database).getName());
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(false);

        return jpaVendorAdapter;
    }

    @Bean
    @DependsOn("flyway")
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setPackagesToScan(getEntityPackagesToScan());

        Properties props = new Properties();
        if (ConfigurationManager.INSTANCE.getDefaultEnvironment().expandedStringForm().startsWith("prod")) {
            props.put("hibernate.show_sql", false);
        } else {
            props.put("hibernate.show_sql", true);
        }

        props.put("hibernate.hbm2ddl.auto", "validate");
        entityManagerFactoryBean.setJpaProperties(props);

        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(getFlywayLocations());
        flyway.setTable(getTableNameForMigration());
        flyway.setInitOnMigrate(true);
        return flyway;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
