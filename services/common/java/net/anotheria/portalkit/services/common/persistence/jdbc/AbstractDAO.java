package net.anotheria.portalkit.services.common.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 08.01.13 00:47
 */
public abstract class AbstractDAO implements DAO{

	public static final String ATT_DAO_CREATED = "dao_created";
	public static final String ATT_DAO_UPDATED = "dao_updated";

	public static final String DAO_STD_FIELD_DECL = ATT_DAO_CREATED+", "+ATT_DAO_UPDATED;
	public static final String DAO_STD_FIELD_VALUES = "?,?";

	protected abstract String[] getTableNames();

	
	
	public void cleanupFromUnitTests(Connection connection) throws DAOException, SQLException {
		String[] tableNames = getTableNames();
		if (tableNames==null || tableNames.length==0)
			return;
		for (String table : tableNames){
			PreparedStatement statement = connection.prepareStatement("DELETE FROM "+table);
			statement.executeUpdate();
		}
	}

	protected void fillCreatedStatement(PreparedStatement statement, int pos) throws SQLException{
		statement.setLong(pos+1, System.currentTimeMillis()); //DAO_CREATED
		statement.setLong(pos+2, 0L); //DAO_UPDATED
	}



}
