package net.anotheria.portalkit.services.account;

import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * Administrative interface for {@link AccountService}.
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
	 * Get accounts identifiers by account type.
	 * 
	 * @param accountType
	 *            {@link AccountType}
	 * @return {@link List} of {@link Account}
	 * @throws AccountServiceException
	 */
	List<AccountId> getAccountsByType(@SuppressWarnings("rawtypes") AccountType accountType) throws AccountServiceException;

	/**
	 * Get accounts by query.
	 * 
	 * @param query
	 *            {@link AccountQuery}
	 * @return {@link List} of {@link Account}
	 * @throws AccountAdminServiceException
	 */
	List<Account> getAccountsByQuery(AccountQuery query) throws AccountAdminServiceException;

}
