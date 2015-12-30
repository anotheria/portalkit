package net.anotheria.portalkit.services.account.persistence.inmemory;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;

/**
 * {@link AccountPersistenceService} factory for in-memory implementation.
 * 
 * @author Alexandr Bolbat
 */
public class InMemoryAccountPersistenceServiceFactory implements ServiceFactory<AccountPersistenceService> {

	@Override
	public AccountPersistenceService create() {
		return new InMemoryAccountPersistenceServiceImpl();
	}

}
