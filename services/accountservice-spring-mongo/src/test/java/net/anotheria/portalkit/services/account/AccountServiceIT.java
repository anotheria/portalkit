package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.account.persistence.AccountEntity;
import net.anotheria.portalkit.services.account.persistence.AccountEntityRepository;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        AccountServiceIT.MongoConfig.class,
        AccountServiceIT.ServiceConfig.class
})
class AccountServiceIT {

    @Configuration
    @EnableMongoRepositories(basePackageClasses = AccountEntityRepository.class)
    static class MongoConfig extends AbstractMongoClientConfiguration {
        @Override protected String getDatabaseName() { return "portalkit-test"; }
        @Bean public com.mongodb.client.MongoClient mongoClient() {
            return com.mongodb.client.MongoClients.create("mongodb://localhost:27017");
        }
    }

    @Configuration
    @ComponentScan(basePackageClasses = AccountServiceImpl.class) // findet @Service
    static class ServiceConfig {
        // simple in-memory Caches, passend zu deinem Service
        @Bean Map<Object,Object> cacheBacking() { return new ConcurrentHashMap<>(); }
        @Bean Map<Object,Object> nonExistingBacking() { return new ConcurrentHashMap<>(); }


    }

    @org.springframework.beans.factory.annotation.Autowired
    AccountService accountService;
    @org.springframework.beans.factory.annotation.Autowired
    AccountEntityRepository repo;

    @Test
    void getAccount_readsFromMongo_andCaches() throws Exception {
        // Arrange: Entity direkt in Mongo anlegen (wie es in echt schon vorhanden wäre)
        String id = UUID.randomUUID().toString();
        AccountEntity e = new AccountEntity();
        e.setId(id);
        e.setName("smoke-test");
        e.setEmail("smoke1@example.com");
        e.setBrand("testbrand");
        repo.save(e);

        AccountId accountId = new AccountId(id);

        // Act: Service aufrufen
        Account a1 = accountService.getAccount(accountId);

        // Assert: gelesen & korrekt
        assertThat(a1).isNotNull();
        assertThat(a1.getName()).isEqualTo("smoke-test");

        // Optional: zweiter Aufruf muss aus Cache kommen (Repo nicht mehr gefragt)
        Account a2 = accountService.getAccount(accountId);
        assertThat(a2.getName()).isEqualTo("smoke-test");
    }
}