package net.anotheria.portalkit.services.accountlist;

import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.slicer.Slice;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

/**
 * AccountList service interface.
 * 
 * @author lrosenberg
 * 
 */
@DistributeMe
@FailBy(strategyClass=RetryCallOnce.class)
public interface AccountListService extends Service {

	/**
	 * Adds an account to the specified list of the owner. If the target account
	 * is already in the list, the operation does nothing.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param firstTarget	{@link AccountIdAdditionalInfo}
	 * @param moreTargets	{@link AccountIdAdditionalInfo}
	 * @return boolean
	 * @throws AccountListServiceException if error.
	 */
	boolean addToList(AccountId owner, String listName, AccountIdAdditionalInfo firstTarget, AccountIdAdditionalInfo... moreTargets)
			throws AccountListServiceException;

	/**
	 * Updates an account in the specified list of the owner. If the target
	 * account is already in the list, the operation does nothing.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param targets	list of {@link AccountIdAdditionalInfo}.
	 * @return boolean
	 * @throws AccountListServiceException if error.
	 */
	boolean updateInList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException;

	/**
	 * Adds collection of accounts to the specified list of the owner. If the
	 * target account is already on the list, the operation does nothing.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param targets	list of {@link AccountIdAdditionalInfo}.
	 * @throws AccountListServiceException if error.
	 * @return boolean
	 */
	boolean addToList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException;

	/**
	 * Removes collection of accounts from list.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param targets	list of {@link AccountIdAdditionalInfo}.
	 * @throws AccountListServiceException if error.
	 * @return boolean
	 */
	boolean removeFromList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException;

	/**
	 * Removes one or multiple accounts from list.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param firstTarget	{@link AccountIdAdditionalInfo}
	 * @param moreTarget	{@link AccountIdAdditionalInfo}
	 * @throws AccountListServiceException if error.
	 * @return boolean
	 */
	boolean removeFromList(AccountId owner, String listName, AccountIdAdditionalInfo firstTarget, AccountIdAdditionalInfo... moreTarget)
			throws AccountListServiceException;

	/**
	 * Returns the account list with given name.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @throws AccountListServiceException if error.
	 * @return list of {@link AccountIdAdditionalInfo}
	 */
	List<AccountIdAdditionalInfo> getList(AccountId owner, String listName) throws AccountListServiceException;

	/**
	 * Returns the account list with given name. In addition can split result on
	 * pages and can sort with any field.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param filter {@link AccountListFilter}
	 * @return list of {@link AccountIdAdditionalInfo}
	 * @throws AccountListServiceException if error.
	 */
	Slice<AccountIdAdditionalInfo> getList(AccountId owner, String listName, AccountListFilter filter) throws AccountListServiceException;

	/**
	 * Returns the list of ownerIds that have added this account in lists with
	 * given name. Warning: this operation is probably very expensive.
	 * 
	 * @param target	account id.
	 * @param listName list name.
	 * @throws AccountListServiceException if error.
	 * @return list of {@link AccountIdAdditionalInfo}
	 */
	List<AccountIdAdditionalInfo> reverseLookup(AccountId target, String listName) throws AccountListServiceException;

	/**
	 * Returns the list of ownerIds that have added this account in lists with
	 * given name. Warning: this operation is probably very expensive. In
	 * addition can split result on pages and can sort with any field.
	 *
	 * @param target	account id.
	 * @param listName list name.
	 * @param filter {@link AccountListFilter}
	 * @return list of {@link AccountIdAdditionalInfo}
	 * @throws AccountListServiceException if error.
	 */
	Slice<AccountIdAdditionalInfo> reverseLookup(AccountId target, String listName, AccountListFilter filter) throws AccountListServiceException;

}
