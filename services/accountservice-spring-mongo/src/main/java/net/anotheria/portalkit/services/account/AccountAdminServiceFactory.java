package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AccountAdminService} factory for main implementation.
 * 
 */
public class AccountAdminServiceFactory implements ServiceFactory<AccountAdminService> {

	@Override
	public AccountAdminService create() {
		return ServiceProxyUtil.createServiceProxy(AccountAdminService.class, AccountServiceImpl.INSTANCE, "service", "portal-kit", true, AccountService.class);
	}

}
