package net.anotheria.portalkit.services.accountlist;

import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;
import java.util.List;

/**
 * AccountList service interface.
 * 
 * @author dagafonov
 * 
 */
public interface AccountListService {

	/**
	 * Adds an account to the specified list of the owner. If the target account is already on the list,
	 * the operation does nothing.
	 * @param owner
	 * @param listName
	 * @param firstTarget
	 * @param moreTargets
	 * @return
	 * @throws AccountListServiceException
	 */
	boolean addToList(AccountId owner, String listName, AccountId firstTarget, AccountId ... moreTargets) throws AccountListServiceException;


	/**
	 * Removes one or multiple accounts from list.
	 * @param owner
	 * @param listName
	 * @param firstTarget
	 * @param moreTarget
	 * @return
	 * @throws AccountListServiceException
	 */
	boolean removeFromList(AccountId owner, String listName, AccountId firstTarget, AccountId ... moreTarget) throws AccountListServiceException;

	/**
	 * Returns the account list with given name.
	 * @param owner
	 * @param listName
	 * @return
	 * @throws AccountListServiceException
	 */
	List<AccountId> getList(AccountId owner, String listName) throws AccountListServiceException;

	void addAccountsToList(AccountId owner, String listName, Collection<AccountId> target) throws AccountListServiceException;

	void removeAccountsFromList(AccountId owner, String listName, Collection<AccountId> target) throws AccountListServiceException;

	/**
	 * Returns the list of ownerIds that have added this account in lists with given name. Warning: this operation is probably very expensive.
	 * @param target
	 * @param listName
	 * @return
	 * @throws AccountListServiceException
	 */
	List<AccountId> reverseLookup(AccountId target, String listName) throws AccountListServiceException;

}
