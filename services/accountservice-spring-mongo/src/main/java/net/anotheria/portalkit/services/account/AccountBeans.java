package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.account.persistence.AccountAuditEntityRepository;
import net.anotheria.portalkit.services.account.persistence.AccountEntityRepository;
import net.anotheria.portalkit.services.account.persistence.AccountNoteEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountBeans {
    @Bean
    public AccountServiceImpl accountServiceImpl(AccountEntityRepository accountEntityRepository, AccountNoteEntityRepository accountNoteEntityRepository, AccountAuditEntityRepository accountAuditEntityRepository) {
        AccountServiceImpl.INSTANCE.setAccountEntityRepository(accountEntityRepository);
        AccountServiceImpl.INSTANCE.setAccountNoteEntityRepository(accountNoteEntityRepository);
        AccountServiceImpl.INSTANCE.setAccountAuditEntityRepository(accountAuditEntityRepository);
        return AccountServiceImpl.INSTANCE;
    }
}
