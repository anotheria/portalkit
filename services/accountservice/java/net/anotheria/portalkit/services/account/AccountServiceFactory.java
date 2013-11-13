package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AccountService} factory for main implementation.
 * 
 * @author Alexandr Bolbat
 */
public class AccountServiceFactory implements ServiceFactory<AccountService> {

	@Override
	public AccountService create() {
		return ServiceProxyUtil.createServiceProxy(AccountService.class, new AccountServiceImpl(), "service", "portal-kit", true);
	}

}
