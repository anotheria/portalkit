package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link AccountAdminService} factory for main implementation.
 * 
 * @author Alexandr Bolbat
 */
public class AccountAdminServiceFactory implements ServiceFactory<AccountAdminService> {

	@Override
	public AccountAdminService create() {
		try {
			return AccountAdminService.class.cast(MetaFactory.get(AccountService.class));
		} catch (MetaFactoryException e) {
			throw new RuntimeException("Can't obtain AccountAdminService implementation.");
		}
	}

}
