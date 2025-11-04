package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.authentication.persistence.AuthTokenEntityRepository;
import net.anotheria.portalkit.services.authentication.persistence.PasswordEntityRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * PortalKITAuthenticationServiceConfig — context configuration for authentication service.
 *
 * @author ykalapusha
 * @since 27.10.2025
 */
@Configuration
@EnableMongoRepositories(basePackageClasses = {
        PasswordEntityRepository.class,
        AuthTokenEntityRepository.class
})
@ComponentScan(basePackages = {
        "net.anotheria.portalkit.services.authentication"
})
public class PortalKITAuthenticationServiceConfig {
}
