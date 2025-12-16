package net.anotheria.portalkit.services.foreignid;

import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ForeignIdBeans — spring bean for {@link ForeignIdService}.
 *
 * @author ykalapusha
 * @since 16.12.2025
 */
@Configuration
public class ForeignIdBeans {
    @Bean
    public ForeignIdServiceImpl foreignIdServiceImpl(ForeignIdEntityRepository foreignIdEntityRepository) {
        return new ForeignIdServiceImpl(foreignIdEntityRepository);
    }
}
