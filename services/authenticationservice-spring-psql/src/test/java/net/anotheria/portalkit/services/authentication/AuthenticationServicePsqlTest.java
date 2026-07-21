package net.anotheria.portalkit.services.authentication;

import jakarta.persistence.EntityManagerFactory;
import net.anotheria.portalkit.services.common.AccountId;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Integration test against a real PostgreSQL started with Testcontainers. {@link HostConfig} plays the role
 * of an embedding application: it owns the {@code DataSource}, the {@code EntityManagerFactory} (with the
 * auth entity package on its scan and {@code ddl-auto=validate}) and the {@code TransactionManager}, then
 * {@code @Import}s the auth module. Requires Docker.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AuthenticationServicePsqlTest.HostConfig.class)
class AuthenticationServicePsqlTest {

    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("pk_test")
            .withUsername("pk")
            .withPassword("pk");

    static {
        POSTGRES.start();
    }

    @Configuration
    @EnableTransactionManagement
    @Import(PortalKITAuthenticationServiceConfig.class)
    static class HostConfig {

        @Bean
        public DataSource dataSource() {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUrl(POSTGRES.getJdbcUrl());
            ds.setUsername(POSTGRES.getUsername());
            ds.setPassword(POSTGRES.getPassword());
            return ds;
        }

        @Bean
        public JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
            adapter.setDatabase(Database.POSTGRESQL);
            adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQL82Dialect");
            adapter.setGenerateDdl(false);
            adapter.setShowSql(false);
            return adapter;
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
            LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
            emf.setDataSource(dataSource);
            emf.setJpaVendorAdapter(jpaVendorAdapter);
            emf.setPackagesToScan("net.anotheria.portalkit.services.authentication.persistence");
            Properties props = new Properties();
            props.put("hibernate.hbm2ddl.auto", "validate"); // like an embedding app's ddl-auto: validate
            emf.setJpaProperties(props);
            return emf;
        }

        @Bean
        public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }
    }

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ApplicationContext ctx;

    @Test
    void flywayBuildsSchema() throws Exception {
        assertTrue(tableExists("auth_passwd"), "auth_passwd table should exist");
        assertTrue(tableExists("auth_token"), "auth_token table should exist");
        assertTrue(tableExists("flyway_authentication"), "flyway_authentication metadata table should exist");
    }

    @Test
    void moduleDoesNotContributeGenericPersistenceBeans() {
        assertFalse(ctx.containsBean("flyway"), "must not register a generic 'flyway' bean");
        assertTrue(ctx.containsBean("authFlyway"), "auth Flyway should be named 'authFlyway'");
        assertTrue(ctx.containsBean("dataSource"), "host dataSource present");
        assertTrue(ctx.containsBean("entityManagerFactory"), "host entityManagerFactory present");
        assertTrue(ctx.containsBean("transactionManager"), "host transactionManager present");
    }

    @Test
    void passwordSetAndAuthenticate() throws Exception {
        AccountId id = AccountId.generateNew();
        authenticationService.setPassword(id, "s3cret");

        assertTrue(authenticationService.canAuthenticate(id, "s3cret"), "correct password should authenticate");
        assertFalse(authenticationService.canAuthenticate(id, "wrong"), "wrong password should not authenticate");
    }

    @Test
    void multiUseTokenLifecycle() throws Exception {
        AccountId id = AccountId.generateNew();
        AuthToken token = new AuthToken();
        token.setAccountId(id);
        token.setType(5);
        token.setMultiUse(true);

        EncryptedAuthToken encrypted = authenticationService.generateEncryptedToken(token);
        String tokenString = encrypted.getEncryptedVersion();
        assertNotNull(tokenString);

        assertTrue(authenticationService.canAuthenticateByEncryptedToken(tokenString));
        assertEquals(id, authenticationService.authenticateByEncryptedToken(tokenString));
        // multi-use: still present after authentication
        assertEquals(tokenString, authenticationService.getTokenByType(id, 5));

        authenticationService.deleteToken(tokenString);
        assertFalse(authenticationService.canAuthenticateByEncryptedToken(tokenString));
    }

    @Test
    void singleUseTokenIsConsumed() throws Exception {
        AccountId id = AccountId.generateNew();
        AuthToken token = new AuthToken();
        token.setAccountId(id);
        token.setMultiUse(false);

        EncryptedAuthToken encrypted = authenticationService.generateEncryptedToken(token);
        String tokenString = encrypted.getEncryptedVersion();

        assertEquals(id, authenticationService.authenticateByEncryptedToken(tokenString));
        try {
            authenticationService.authenticateByEncryptedToken(tokenString);
            fail("single-use token should be gone after first use");
        } catch (AuthTokenNotFoundException expected) {
            // ok
        }
    }

    @Test
    void deleteUserDataRemovesPassword() throws Exception {
        AccountId id = AccountId.generateNew();
        authenticationService.setPassword(id, "pw");
        assertTrue(authenticationService.canAuthenticate(id, "pw"));

        authenticationService.deleteUserData(id);
        assertFalse(authenticationService.canAuthenticate(id, "pw"), "password should be gone after deleteUserData");
    }

    @Test
    void reRunningFlywayIsNoOp() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:net/anotheria/portalkit/services/authentication/persistence/jdbc/migrations/common")
                .table("flyway_authentication")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();

        MigrateResult result = flyway.migrate();
        assertEquals(0, result.migrationsExecuted, "no migrations should be applied on an up-to-date database");
        assertEquals("1.2", flyway.info().current().getVersion().toString(),
                "current version should be the last auth migration");
    }

    private boolean tableExists(String table) throws Exception {
        try (Connection con = dataSource.getConnection();
             ResultSet rs = con.getMetaData().getTables(null, null, table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }
}
