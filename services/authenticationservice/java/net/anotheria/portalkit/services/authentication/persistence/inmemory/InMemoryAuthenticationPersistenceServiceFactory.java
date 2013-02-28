package net.anotheria.portalkit.services.authentication.persistence.inmemory;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;

/**
 * {@link AuthenticationPersistenceService} factory for InMemory implementation.
 * 
 * @author Alexandr Bolbat
 */
public class InMemoryAuthenticationPersistenceServiceFactory implements ServiceFactory<AuthenticationPersistenceService> {

	@Override
	public AuthenticationPersistenceService create() {
		return new InMemoryAuthenticationPersistenceServiceImpl();
	}

}
