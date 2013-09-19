package net.anotheria.portalkit.services.accountsettings.persistence.inmemory;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceService;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 19.09.13 23:13
 */
public class InMemoryAccountSettingsPersistenceServiceFactory implements ServiceFactory<AccountSettingsPersistenceService> {
	@Override
	public AccountSettingsPersistenceService create() {
		return new InMemoryAccountSettingsPersistenceServiceImpl();
	}
}
