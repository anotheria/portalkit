package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.Collection;
import java.util.List;

/**
 * Administrative interface for {@link AccountService}.
 */
@DistributeMe()
@FailBy(strategyClass=RetryCallOnce.class)
public interface AccountAdminService extends Service {

	/**
	 * Returns collection of registered accounts in the system.
	 * 
	 * @return collection of {@link AccountId}.
	 * @throws AccountAdminServiceException		if error.
	 */
	Collection<AccountId> getAllAccountIds() throws AccountAdminServiceException;

	/**
	 * Get accounts identifiers by account type.
	 * 
	 * @param accountType
	 *            {@link AccountType}
	 * @return {@link List} of {@link Account}
	 * @throws AccountServiceException		if error.
	 */
	List<AccountId> getAccountsByType(@SuppressWarnings("rawtypes") AccountType accountType) throws AccountServiceException;

	/**
	 * Get accounts by query.
	 * 
	 * @param query
	 *            {@link AccountQuery}
	 * @return {@link List} of {@link Account}
	 * @throws AccountAdminServiceException		if error.
	 */
	List<Account> getAccountsByQuery(AccountQuery query) throws AccountAdminServiceException;

}
