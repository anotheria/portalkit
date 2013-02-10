package net.anotheria.portalkit.services.accountlist;

import java.util.List;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * AccountList service interface.
 * 
 * @author dagafonov
 * 
 */
public interface AccountListService {

	void addAccountToMyList(AccountId partner, String list) throws AccountListServiceException;

	// void addUserToList(AccountId owner, AccountId partner, String list)
	// throws AccountListServiceException;

	void removeAccountFromMyList(AccountId partner, String list) throws AccountListServiceException;

	// void removeUserFromList(AccountId owner, AccountId partner, String list)
	// throws AccountListServiceException;

	List<AccountId> getMyList(String list) throws AccountListServiceException;

	// List<AccountId> getList(AccountId owner, String list)throws
	// AccountListServiceException;

	boolean isAccountInMyList(AccountId partner, String list) throws AccountListServiceException;

}
