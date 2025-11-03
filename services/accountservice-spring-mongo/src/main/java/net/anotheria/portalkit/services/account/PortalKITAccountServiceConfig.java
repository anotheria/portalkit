package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.account.persistence.AccountAuditEntityRepository;
import net.anotheria.portalkit.services.account.persistence.AccountEntityRepository;
import net.anotheria.portalkit.services.account.persistence.AccountNoteEntityRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@EnableMongoRepositories(basePackages = {
        "net.anotheria.portalkit.services.account.persistence",
})
@ComponentScan(basePackages = {
        "net.anotheria.portalkit.services.account"
})
public class PortalKITAccountServiceConfig {
}
