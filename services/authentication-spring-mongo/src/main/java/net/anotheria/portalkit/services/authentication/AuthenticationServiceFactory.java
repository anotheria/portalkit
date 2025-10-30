package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.authentication.persistence.AuthTokenEntityRepository;
import net.anotheria.portalkit.services.authentication.persistence.PasswordEntityRepository;
import net.anotheria.portalkit.services.common.UserDataManagingService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link AuthenticationService} factory for main implementation.
 * 
 * @author Alexandr Bolbat
 */
@Component
public class AuthenticationServiceFactory implements ServiceFactory<AuthenticationService> {

    private AuthTokenEntityRepository authTokenEntityRepository;
    private PasswordEntityRepository passwordEntityRepository;

    @Autowired
    public void setAuthTokenEntityRepository(AuthTokenEntityRepository authTokenEntityRepository) {
        this.authTokenEntityRepository = authTokenEntityRepository;
    }

    @Autowired
    public void setPasswordEntityRepository(PasswordEntityRepository passwordEntityRepository) {
        this.passwordEntityRepository = passwordEntityRepository;
    }

    @Override
	public AuthenticationService create() {
        AuthenticationServiceImpl  authenticationService = new AuthenticationServiceImpl();
        authenticationService.setAuthTokenEntityRepository(authTokenEntityRepository);
        authenticationService.setPasswordEntityRepository(passwordEntityRepository);
		return ServiceProxyUtil.createServiceProxy(AuthenticationService.class, authenticationService, "service", "portal-kit", true, UserDataManagingService.class);
	}

}
