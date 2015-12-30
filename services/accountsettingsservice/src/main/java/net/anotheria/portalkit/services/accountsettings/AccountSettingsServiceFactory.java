package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.DeletionService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AccountSettingsService} for main implementation.
 * 
 * @author Alexandr Bolbat
 */
public class AccountSettingsServiceFactory implements ServiceFactory<AccountSettingsService> {

	@Override
	public AccountSettingsService create() {
		return ServiceProxyUtil.createServiceProxy(AccountSettingsService.class, new AccountSettingsServiceImpl(), "service", "portal-kit", true, DeletionService.class);
	}

}
