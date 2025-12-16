package net.anotheria.portalkit.services.foreignid;

import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdEntityRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * PortalKITForeignIdServiceConfig — context configuration for foreign id service.
 *
 * @author ykalapusha
 * @since 16.12.2025
 */
@Configuration
@EnableMongoRepositories(
        basePackageClasses = {
                ForeignIdEntityRepository.class
        }
)
@ComponentScan(basePackages = {
        "net.anotheria.portalkit.services.foreignid"
})
public class PortalKITForeignIdServiceConfig {
}
