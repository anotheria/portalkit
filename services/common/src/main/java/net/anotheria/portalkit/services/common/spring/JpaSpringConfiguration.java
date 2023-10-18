package net.anotheria.portalkit.services.common.spring;

import com.googlecode.flyway.core.Flyway;
import net.anotheria.portalkit.services.common.flyway.FlywayUtils;
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

    protected String getDbConfigurationName() {
        return "pk-jdbc-" + getServiceName();
    }

    protected String getEntityPackagesToScan() {
        return getBasePackage();
    }

    protected String[] getFlywayLocations() {
        return FlywayUtils.getDefaultFlywayLocations(getBasePackage(), getDbConfig().getDriver());
    }

    protected String getTableNameForMigration() {
        return FlywayUtils.getDefaultTableNameForMigration(getServiceName());
    }

    public HibernateConfig getDbConfig() {
        HibernateConfig dbConfig = new HibernateConfig();
        ConfigurationManager.INSTANCE.configureAs(dbConfig, getDbConfigurationName());
        return dbConfig;
    }

    @Bean(destroyMethod="close")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        HibernateConfig dbConfig = getDbConfig();
        dataSource.setDriverClassName(dbConfig.getDriver());
        dataSource.setUrl(dbConfig.getUrl());
        dataSource.setUsername(dbConfig.getUsername());
        dataSource.setPassword(dbConfig.getPassword());
        dataSource.setValidationQuery("SELECT 1");

        return dataSource;
    }

    @Bean
    public Database database() {
        return DBUtils.getDatabase(getDbConfig().getDriver());
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

        HibernateConfig dbConfig = getDbConfig();
        Properties props = new Properties();
        props.put("hibernate.show_sql", dbConfig.isShowSql());
//        props.put("hibernate.hbm2ddl.auto", "validate");
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
