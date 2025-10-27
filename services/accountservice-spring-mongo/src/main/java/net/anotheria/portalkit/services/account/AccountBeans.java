package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.account.persistence.AccountEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountBeans {
    @Bean
    public AccountServiceImpl accountServiceImpl(AccountEntityRepository accountEntityRepository) {
        AccountServiceImpl.INSTANCE.setAccountEntityRepository(accountEntityRepository);
        return AccountServiceImpl.INSTANCE;
    }
}
