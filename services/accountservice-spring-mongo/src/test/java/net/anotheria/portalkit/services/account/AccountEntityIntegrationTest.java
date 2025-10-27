package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.account.PortalKITAccountServiceConfig;
import net.anotheria.portalkit.services.account.persistence.AccountEntity;
import net.anotheria.portalkit.services.account.persistence.AccountEntityRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//@Testcontainers
@Import(PortalKITAccountServiceConfig.class) // die Config aus dem Modul
@Ignore
class AccountEntityIntegrationTest {
/*
    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    @Autowired
    private AccountEntityRepository repository;

    @Ignore
    @Test
    void testSaveAndLoad() {
        AccountEntity entity = new AccountEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName("testuser");
        entity.setEmail("test@example.com");
        entity.setBrand("default");
        repository.save(entity);

        AccountEntity loaded = repository.findById(entity.getId()).orElseThrow();
        assertThat(loaded.getName()).isEqualTo("testuser");
        System.out.println("✅ MongoDB Integration Test erfolgreich mit ID: " + loaded.getId());
    }
    */

}