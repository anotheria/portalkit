package net.anotheria.portalkit.services.authentication.persistence.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;

/**
 * {@link AuthenticationPersistenceService} factory for JDBC implementation.
 * 
 * @author Alexandr Bolbat
 */
public class JDBCAuthenticationPersistenceServiceFactory implements ServiceFactory<AuthenticationPersistenceService> {

	@Override
	public AuthenticationPersistenceService create() {
		return new JDBCAuthenticationPersistenceServiceImpl();
	}

}
