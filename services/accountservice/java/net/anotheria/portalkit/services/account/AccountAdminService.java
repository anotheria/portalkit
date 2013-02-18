package net.anotheria.portalkit.services.account;

import java.util.Collection;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * TODO comment this class
 * 
 * @author lrosenberg
 * @since 18.02.13 10:11
 */
public interface AccountAdminService extends Service {
	Collection<AccountId> getAllAccountIds() throws AccountAdminServiceException;
}
