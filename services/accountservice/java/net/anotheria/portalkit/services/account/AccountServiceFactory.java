package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link AccountService} factory for main implementation.
 * 
 * @author Alexandr Bolbat
 */
public class AccountServiceFactory implements ServiceFactory<AccountService> {

	@Override
	public AccountService create() {
		return new AccountServiceImpl();
	}

}
