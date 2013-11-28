package net.anotheria.portalkit.services.common.persistence.jdbc;

import java.sql.SQLException;

public interface BasePersistenceService {

	void init();

	void cleanupFromUnitTests() throws SQLException, DAOException;

}
