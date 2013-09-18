package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link AccountSettingsService} for main implementation.
 * 
 * @author Alexandr Bolbat
 */
public class AccountSettingsServiceFactory implements ServiceFactory<AccountSettingsService> {

	@Override
	public AccountSettingsService create() {
		return new AccountSettingsServiceImpl();
	}

}
