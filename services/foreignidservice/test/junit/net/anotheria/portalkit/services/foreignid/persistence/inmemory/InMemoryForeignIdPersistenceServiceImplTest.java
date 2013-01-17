package net.anotheria.portalkit.services.foreignid.persistence.inmemory;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;

public class InMemoryForeignIdPersistenceServiceImplTest {

	protected InMemoryForeignIdPersistenceServiceImpl getService(String environment) throws Exception {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", environment));
		InMemoryForeignIdPersistenceServiceImpl service = new InMemoryForeignIdPersistenceServiceImpl();
		return service;
	}

}
