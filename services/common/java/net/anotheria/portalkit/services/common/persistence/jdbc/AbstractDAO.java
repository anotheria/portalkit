package net.anotheria.portalkit.services.common.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.googlecode.flyway.core.util.jdbc.JdbcUtils;

/**
 * TODO comment this class
 * 
 * @author lrosenberg
 * @since 08.01.13 00:47
 */
public abstract class AbstractDAO implements DAO {

	/**
	 * Attribute constant for the dao_created attribute in table.
	 */
	public static final String ATT_DAO_CREATED = "dao_created";
	/**
	 * Attribute constant for the dao_updated attribute in table.
	 */
	public static final String ATT_DAO_UPDATED = "dao_updated";

	public static final String DAO_STD_FIELD_DECL = ATT_DAO_CREATED + ", " + ATT_DAO_UPDATED;
	public static final String DAO_STD_FIELD_VALUES = "?,?";

	/**
	 * Returns the table names that are modified by this dao. Used to cleanup from unittests.
	 * 
	 * @return
	 */
	protected abstract String[] getTableNames();

	/**
	 * Deletes all data that is managed by this dao. Use with care and NEVER call live.
	 * 
	 * @param connection
	 * @throws DAOException
	 * @throws SQLException
	 */
	public void cleanupFromUnitTests(Connection connection) throws DAOException, SQLException {
		String[] tableNames = getTableNames();
		if (tableNames == null || tableNames.length == 0)
			return;

		PreparedStatement statement = null;
		for (String table : tableNames)
			try {
				statement = connection.prepareStatement("DELETE FROM " + table);
				statement.executeUpdate();
			} finally {
				JDBCUtil.close(statement);
			}
	}

	protected void fillCreatedStatement(PreparedStatement statement, int pos) throws SQLException {
		statement.setLong(pos + 1, System.currentTimeMillis()); // DAO_CREATED
		statement.setLong(pos + 2, 0L); // DAO_UPDATED
	}
	
}
