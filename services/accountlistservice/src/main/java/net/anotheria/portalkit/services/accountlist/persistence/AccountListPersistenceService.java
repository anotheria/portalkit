package net.anotheria.portalkit.services.accountlist.persistence;

import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.accountlist.AccountIdAdditionalInfo;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * Account list persistence service interface.
 * 
 * @author dagafonov
 * 
 */
public interface AccountListPersistenceService extends Service {

	/**
	 * Gets list of involved accounts into specified list of owner account.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @return list of {@link AccountIdAdditionalInfo}
	 * @throws AccountListPersistenceServiceException if error.
	 */
	List<AccountIdAdditionalInfo> getList(AccountId owner, String listName) throws AccountListPersistenceServiceException;

	/**
	 * Adds list of involved accounts into specified list of owner account.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param targets	list of {@link AccountIdAdditionalInfo}
	 * @return list of {@link AccountIdAdditionalInfo}
	 * @throws AccountListPersistenceServiceException if error.
	 */
	boolean addToList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListPersistenceServiceException;

	/**
	 * Removes list of involved accounts into specified list of owner account.
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param targets	list of {@link AccountIdAdditionalInfo}
	 * @return list of {@link AccountIdAdditionalInfo}
	 * @throws AccountListPersistenceServiceException if error.
	 */
	boolean removeFromList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets)
			throws AccountListPersistenceServiceException;

	/**
	 * Gets list of owners that have list where target account is involved.
	 * 
	 * @param target	account id.
	 * @param listName list name.
	 * @return list of {@link AccountIdAdditionalInfo}
	 * @throws AccountListPersistenceServiceException if error.
	 */
	List<AccountIdAdditionalInfo> getReverseList(AccountId target, String listName) throws AccountListPersistenceServiceException;

	/**
	 * 
	 * @param owner owner id.
	 * @param listName list name.
	 * @param itemsToUpdate	list of {@link AccountIdAdditionalInfo}
	 * @return list of {@link AccountIdAdditionalInfo}
	 * @throws AccountListPersistenceServiceException if error.
	 */
	boolean updateInList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> itemsToUpdate) throws AccountListPersistenceServiceException;

}
