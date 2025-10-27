package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.account.persistence.AccountEntity;
import net.anotheria.portalkit.services.account.persistence.AccountEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.context.annotation.Configuration;


import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountEntitySmokeTest {

    @Test
    void testSaveAndLoadWithRealMongo() {
        // Manuell ApplicationContext hochfahren
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // MongoDB URI setzen (für real DB)
        context.getEnvironment().getSystemProperties().put(
                "spring.data.mongodb.uri",
                "mongodb://localhost:27017/portalkit-test"
        );

        // Wichtig: deine Konfiguration registrieren
        context.register(PortalKITAccountServiceConfig.class);
        context.refresh();

        AccountEntityRepository repository = context.getBean(AccountEntityRepository.class);

        // Testdaten anlegen
        AccountEntity entity = new AccountEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName("smoketest");
        entity.setEmail("smoke@example.com");
        entity.setBrand("testbrand");
        repository.save(entity);

        // Laden & prüfen
        var loaded = repository.findById(entity.getId()).orElseThrow();
        assertThat(loaded.getName()).isEqualTo("smoketest");

        System.out.println("✅ Smoke Test erfolgreich mit ID: " + loaded.getId());
    }

    @Configuration
    @EnableMongoRepositories(basePackageClasses = AccountEntityRepository.class)
    static class TestMongoConfig extends AbstractMongoClientConfiguration {
        @Override protected String getDatabaseName() { return "portalkit-test"; }

        @Bean
        public com.mongodb.client.MongoClient mongoClient() {
            // echte lokale Mongo verwenden
            return com.mongodb.client.MongoClients.create("mongodb://localhost:27017");
        }

        // AbstractMongoClientConfiguration erzeugt dir daraus automatisch:
        // - MongoDatabaseFactory
        // - MongoTemplate  (=> löst deinen Fehler)
    }
}