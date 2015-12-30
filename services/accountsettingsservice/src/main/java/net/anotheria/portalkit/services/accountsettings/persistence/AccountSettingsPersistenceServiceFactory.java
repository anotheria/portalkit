package net.anotheria.portalkit.services.accountsettings.persistence;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * 
 * @author dagafonov
 *
 */
public class AccountSettingsPersistenceServiceFactory implements ServiceFactory<AccountSettingsPersistenceService> {
	
	@Override
	public AccountSettingsPersistenceService create() {
		return new MongoAccountSettingsPersistenceServiceImpl();
	}

}
