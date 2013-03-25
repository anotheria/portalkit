package net.anotheria.portalkit.services.accountlist;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link AccountListService} factory for main implementation.
 * 
 * @author dagafonov
 * 
 */
public class AccountListServiceFactory implements ServiceFactory<AccountListService> {

	@Override
	public AccountListService create() {
		return new AccountListServiceImpl();
	}

}
