package net.anotheria.portalkit.services.account;

import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * TODO comment this class
 * 
 * @author lrosenberg
 * @since 18.02.13 10:11
 */
public interface AccountAdminService extends Service {

	/**
	 * Returns collection of registered accounts in the system.
	 * 
	 * @return Collection<AccountId>
	 * @throws AccountAdminServiceException
	 */
	Collection<AccountId> getAllAccountIds() throws AccountAdminServiceException;
	
	/**
	 * 
	 * @param accountType
	 * @return
	 * @throws AccountServiceException
	 */
	List<AccountId> getAccountsByType(AccountType accountType) throws AccountServiceException;

}
