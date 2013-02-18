package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 18.02.13 10:11
 */
public interface AccountAdminService {
	Collection<AccountId> getAllAccountIds() throws AccountAdminServiceException;
}
