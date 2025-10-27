package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.account.persistence.AccountEntityRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import net.anotheria.portalkit.services.account.persistence.AccountEntityRepository;

@Configuration
@EnableMongoRepositories(basePackageClasses = AccountEntityRepository.class)
@ComponentScan(basePackages = {
        "net.anotheria.portalkit.services.account" // Services, Mapper etc.
})
public class PortalKITAccountServiceConfig {
}
