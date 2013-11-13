package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AccountPersistenceService} factory for JDBC implementation.
 * 
 * @author Alexandr Bolbat
 */
public class JDBCAccountPersistenceServiceFactory implements ServiceFactory<AccountPersistenceService> {

	@Override
	public AccountPersistenceService create() {
		return ServiceProxyUtil.createServiceProxy(AccountPersistenceService.class, new JDBCAccountPersistenceServiceImpl(), "service", "portal-kit-persistence", true);
	}

}
