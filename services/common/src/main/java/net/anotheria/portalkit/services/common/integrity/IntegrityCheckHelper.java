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
	 * Basic usages. If this parameter equals {@code true} incorrect data will be deleted.
	 */
	private final boolean performDelete;

	/**
	 * Default constructor.
	 *
	 * @param someAccountIds for integrity check
	 */
	public IntegrityCheckHelper(Collection<AccountId> someAccountIds, boolean performDelete){
		accountIds = new HashSet<>(someAccountIds.size());
        accountIds.addAll(someAccountIds);
		this.performDelete = performDelete;
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

	public boolean isPerformDelete() {
		return performDelete;
	}

	@Override
	public String toString(){
		return "IntegrityCheckHelper with " + accountIds.size() + " accounts";
	}
}
