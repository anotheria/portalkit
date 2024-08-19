package net.anotheria.portalkit.services.common.integrity;

import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;
import java.util.HashSet;

/**
 * Integrity check helper.
 *
 * @author lrosenberg
 * @since 23.08.15 19:38
 */
public class IntegrityCheckHelper {

	/**
	 * {@link HashSet} of {@link AccountId}
	 */
	private final HashSet<AccountId> accountIds;

	/**
	 * Default constructor.
	 *
	 * @param someAccountIds for integrity check
	 */
	public IntegrityCheckHelper(Collection<AccountId> someAccountIds){
		accountIds = new HashSet<>(someAccountIds.size());
        accountIds.addAll(someAccountIds);
	}

	/**
	 * Is parameter account id in set of integrity check accounts
	 *
	 * @param accountId	{@link AccountId}
	 * @return {@code true} if account id in set, {@code false} - otherwise
	 */
	public boolean accountExists(AccountId accountId){
		return accountIds.contains(accountId);
	}

	@Override
	public String toString(){
		return "IntegrityCheckHelper with " + accountIds.size() + " accounts";
	}
}
