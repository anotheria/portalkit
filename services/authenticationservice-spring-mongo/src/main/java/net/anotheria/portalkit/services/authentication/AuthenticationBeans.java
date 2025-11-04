package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.authentication.persistence.AuthTokenEntityRepository;
import net.anotheria.portalkit.services.authentication.persistence.PasswordEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AuthenticationBeans — spring bean for {@link AuthenticationService}.
 *
 * @author ykalapusha
 * @since 03.11.2025
 */
@Configuration
public class AuthenticationBeans {
    @Bean
    public AuthenticationServiceImpl authenticationServiceImpl(PasswordEntityRepository passwordEntityRepository,  AuthTokenEntityRepository authTokenEntityRepository) {
        return new AuthenticationServiceImpl(passwordEntityRepository, authTokenEntityRepository);
    }
}
