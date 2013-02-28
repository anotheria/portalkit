package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link AuthenticationService} factory for main implementation.
 * 
 * @author Alexandr Bolbat
 */
public class AuthenticationServiceFactory implements ServiceFactory<AuthenticationService> {

	@Override
	public AuthenticationService create() {
		return new AuthenticationServiceImpl();
	}

}
