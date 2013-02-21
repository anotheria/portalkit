package net.anotheria.portalkit.services.accountlist;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;
import java.util.List;

/**
 * AccountList service interface.
 * 
 * @author lrosenberg
 * 
 */
public interface AccountListService extends Service {

	/**
	 * Adds an account to the specified list of the owner. If the target account
	 * is already in the list, the operation does nothing.
	 * 
	 * @param owner
	 * @param listName
	 * @param firstTarget
	 * @param moreTargets
	 * @return
	 * @throws AccountListServiceException
	 */
	boolean addToList(AccountId owner, String listName, AccountIdAdditionalInfo firstTarget, AccountIdAdditionalInfo... moreTargets) throws AccountListServiceException;

	/**
	 * Adds collection of accounts to the specified list of the owner. If the
	 * target account is already on the list, the operation does nothing.
	 * 
	 * @param owner
	 * @param listName
	 * @param target
	 * @throws AccountListServiceException
	 */
	boolean addToList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException;

	/**
	 * Removes collection of accounts from list.
	 * 
	 * @param owner
	 * @param listName
	 * @param target
	 * @throws AccountListServiceException
	 */
	boolean removeFromList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException;

	/**
	 * Removes one or multiple accounts from list.
	 * 
	 * @param owner
	 * @param listName
	 * @param firstTarget
	 * @param moreTarget
	 * @return
	 * @throws AccountListServiceException
	 */
	boolean removeFromList(AccountId owner, String listName, AccountIdAdditionalInfo firstTarget, AccountIdAdditionalInfo... moreTarget) throws AccountListServiceException;

	/**
	 * Returns the account list with given name.
	 * 
	 * @param owner
	 * @param listName
	 * @return
	 * @throws AccountListServiceException
	 */
	List<AccountIdAdditionalInfo> getList(AccountId owner, String listName) throws AccountListServiceException;

	/**
	 * Returns the list of ownerIds that have added this account in lists with
	 * given name. Warning: this operation is probably very expensive.
	 * 
	 * @param target
	 * @param listName
	 * @return
	 * @throws AccountListServiceException
	 */
	List<AccountIdAdditionalInfo> reverseLookup(AccountId target, String listName) throws AccountListServiceException;

}
