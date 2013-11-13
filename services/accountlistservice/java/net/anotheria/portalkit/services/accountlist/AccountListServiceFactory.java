package net.anotheria.portalkit.services.accountlist;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AccountListService} factory for main implementation.
 * 
 * @author dagafonov
 * 
 */
public class AccountListServiceFactory implements ServiceFactory<AccountListService> {

	@Override
	public AccountListService create() {
		return ServiceProxyUtil.createServiceProxy(AccountListService.class, new AccountListServiceImpl(), "service", "portal-kit", true);
	}

}
