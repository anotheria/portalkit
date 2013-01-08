package net.anotheria.portalkit.services.common.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 08.01.13 00:47
 */
public interface DAO {
	public void cleanupFromUnitTests(Connection connection) throws DAOException, SQLException;
}
