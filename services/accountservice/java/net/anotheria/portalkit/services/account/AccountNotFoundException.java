package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 20.12.12 15:39
 */
public class AccountNotFoundException extends AccountServiceException{
	public AccountNotFoundException(AccountId id){
		super("Account with id "+id+" not found");
	}
}
