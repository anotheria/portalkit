package net.anotheria.portalkit.services.foreignid.persistence.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceService;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 19.07.17 19:18
 */
public class JDBCForeignIdPersistenceServiceFactory implements ServiceFactory<ForeignIdPersistenceService> {
	@Override
	public ForeignIdPersistenceService create() {
		return new JDBCForeignIdPersistenceServiceImpl();
	}
}
