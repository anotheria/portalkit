package net.anotheria.portalkit.services.account;

import jakarta.persistence.EntityManagerFactory;
import net.anotheria.portalkit.services.common.AccountId;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.apache.commons.dbcp2.BasicDataSource;
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
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test against a real PostgreSQL started with Testcontainers.
 *
 * <p>The {@link HostConfig} plays the role of an embedding application (like {@code houseid-statistic-backend}):
 * it owns the {@code DataSource}, the {@code EntityManagerFactory} (with the account entity package on its
 * scan and {@code ddl-auto=validate}) and the {@code TransactionManager}, then {@code @Import}s the account
 * module. This proves the module wires itself onto host-provided persistence, runs its own namespaced Flyway
 * migrations, and never registers a generic {@code flyway}/{@code dataSource}/{@code entityManagerFactory} bean.
 *
 * <p>Requires Docker to be available at test time.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AccountServicePsqlTest.HostConfig.class)
class AccountServicePsqlTest {

    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("pk_test")
            .withUsername("pk")
            .withPassword("pk");

    static {
        POSTGRES.start();
    }

    /**
     * Simulates the embedding host application: provides the shared DataSource / EntityManagerFactory /
     * TransactionManager (Boot would auto-configure these) and imports the account service module.
     */
    @Configuration
    @EnableTransactionManagement
    @Import(PortalKITAccountServiceConfig.class)
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
            // the host must include the account entity package in its entity scan
            emf.setPackagesToScan("net.anotheria.portalkit.services.account.persistence");
            Properties props = new Properties();
            props.put("hibernate.hbm2ddl.auto", "validate"); // mirror the houseid app's ddl-auto: validate
            emf.setJpaProperties(props);
            return emf;
        }

        @Bean
        public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }
    }

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountAdminService accountAdminService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ApplicationContext ctx;

    @Test
    void moduleDoesNotContributeGenericPersistenceBeans() {
        // The account module must reuse the host's persistence and never squat the generic bean names
        // that Spring Boot's auto-configuration uses, otherwise embedding it breaks the host context.
        assertFalse(ctx.containsBean("flyway"), "must not register a generic 'flyway' bean");
        assertTrue(ctx.containsBean("accountFlyway"), "account Flyway should be named 'accountFlyway'");
        assertTrue(ctx.containsBean("accountFlywayAudit"), "audit Flyway should be named 'accountFlywayAudit'");
        // these are the host-owned beans the module binds to
        assertTrue(ctx.containsBean("dataSource"), "host dataSource present");
        assertTrue(ctx.containsBean("entityManagerFactory"), "host entityManagerFactory present");
        assertTrue(ctx.containsBean("transactionManager"), "host transactionManager present");
    }

    private Account newAccount(String suffix) {
        Account a = new Account();
        a.setName("name_" + suffix);
        a.setEmail("mail_" + suffix + "@example.com");
        a.setType(1);
        a.setStatus(0);
        a.setRegistrationTimestamp(System.currentTimeMillis());
        a.setTenant("t1");
        a.setRandomUID(5);
        return a;
    }

    @Test
    void flywayBuildsFullSchema() throws Exception {
        assertTrue(tableExists("account"), "account table should exist");
        assertTrue(tableExists("account_note"), "account_note table should exist");
        assertTrue(tableExists("account_audit"), "account_audit table should exist");
        assertTrue(tableExists("flyway_account"), "flyway_account metadata table should exist");
        assertTrue(tableExists("flyway_audit_account"), "flyway_audit_account metadata table should exist");
    }

    @Test
    void createReadRoundTrip() throws Exception {
        String s = uniq();
        Account created = accountService.createAccount(newAccount(s));

        assertNotNull(created.getId());
        Account fetched = accountService.getAccount(created.getId());
        assertEquals("name_" + s, fetched.getName());
        assertEquals("mail_" + s + "@example.com", fetched.getEmail());
        assertEquals(5, fetched.getRandomUID());

        assertEquals(created.getId(), accountService.getAccountIdByName("name_" + s));
        assertEquals(created.getId(), accountService.getAccountIdByEmail("mail_" + s + "@example.com"));
    }

    @Test
    void updateWritesAudit() throws Exception {
        Account created = accountService.createAccount(newAccount(uniq()));

        // create alone already produces one audit record (audit enabled in test config)
        List<AccountAudit> afterCreate = accountAdminService.getAccountAudits(created.getId());
        assertNotNull(afterCreate, "audit must be enabled");
        assertFalse(afterCreate.isEmpty(), "create should produce an audit record");

        created.setStatus(3L); // bits 1 and 2 added
        accountService.updateAccount(created);

        List<AccountAudit> afterUpdate = accountAdminService.getAccountAudits(created.getId());
        assertTrue(afterUpdate.size() > afterCreate.size(), "status change should produce more audit records");
        assertEquals(3L, accountService.getAccount(created.getId()).getStatus());
    }

    @Test
    void noteLifecycle() throws Exception {
        Account created = accountService.createAccount(newAccount(uniq()));

        AccountNote note = new AccountNote();
        note.setAccountId(created.getId());
        note.setAuthor("admin");
        note.setText("hello");
        accountService.saveAccountNote(note);

        List<AccountNote> notes = accountService.getNotesByAccountId(created.getId());
        assertEquals(1, notes.size());
        long noteId = notes.get(0).getId();
        assertTrue(noteId > 0, "BIGSERIAL id should be generated");

        assertEquals("hello", accountService.getAccountNoteById(noteId).getText());

        AccountNote toUpdate = notes.get(0);
        toUpdate.setText("updated");
        toUpdate.setAuthor("admin2");
        accountService.updateAccountNote(toUpdate);
        assertEquals("updated", accountService.getAccountNoteById(noteId).getText());

        accountService.deleteAccountNote(noteId);
        assertNull(accountService.getAccountNoteById(noteId));
    }

    @Test
    void queryAndTypeLookup() throws Exception {
        String s = uniq();
        Account a = newAccount(s);
        a.setType(7);
        Account created = accountService.createAccount(a);

        AccountQuery query = new AccountQuery.Builder()
                .setNameMask("name_" + s)
                .build();
        List<Account> byQuery = accountAdminService.getAccountsByQuery(query);
        assertTrue(byQuery.stream().anyMatch(x -> x.getId().equals(created.getId())),
                "query by name mask should find the account");

        List<AccountId> byType = accountAdminService.getAccountsByType(TestType.SEVEN);
        assertTrue(byType.contains(created.getId()), "getAccountsByType should find the account");
    }

    @Test
    void deleteRemovesAccount() throws Exception {
        Account created = accountService.createAccount(newAccount(uniq()));
        accountService.deleteAccount(created.getId());
        try {
            accountService.getAccount(created.getId());
            org.junit.jupiter.api.Assertions.fail("expected AccountNotFoundException");
        } catch (AccountNotFoundException expected) {
            // ok
        }
    }

    @Test
    void reRunningFlywayIsNoOp() {
        // Idempotency check: pointing a fresh Flyway (same location + metadata table) at the already-migrated
        // database must apply nothing and report the last account migration version.
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:net/anotheria/portalkit/services/account/persistence/jdbc/migrations/common")
                .table("flyway_account")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();

        MigrateResult result = flyway.migrate();
        assertEquals(0, result.migrationsExecuted, "no migrations should be applied on an up-to-date database");
        assertEquals("1.11", flyway.info().current().getVersion().toString(),
                "current version should be the last account migration");
    }

    /** Minimal {@link AccountType} used to exercise {@code getAccountsByType}. */
    enum TestType implements AccountType<TestType> {
        SEVEN;
        @Override public int getId() { return 7; }
        @Override public String getName() { return name(); }
        @Override public TestType find(int type) { return SEVEN; }
    }

    private String uniq() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    private boolean tableExists(String table) throws Exception {
        try (Connection con = dataSource.getConnection();
             ResultSet rs = con.getMetaData().getTables(null, null, table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }
}
