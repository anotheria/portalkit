package net.anotheria.portalkit.services.authentication.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.01.13 23:18
 */
public class PasswordDAO extends AbstractDAO implements DAO {

	public static final String TABLE_NAME = "auth_passwd";

	@Override
	protected String[] getTableNames() {
		return new String[]{TABLE_NAME};
	}

	public void savePassword(Connection connection, AccountId id, String password) throws SQLException, DAOException {
		String updateSQL = "UPDATE "+TABLE_NAME+" SET password = ?, "+ATT_DAO_UPDATED+" = ? WHERE accid = ?;";
		String insertSQL = "INSERT INTO "+TABLE_NAME+" (accid, password, "+ATT_DAO_CREATED+") VALUES (?,?,?);";

		PreparedStatement update = connection.prepareStatement(updateSQL);
		update.setString(1, password);
		update.setLong(2, System.currentTimeMillis());
		update.setString(3, id.getInternalId());
		int result = update.executeUpdate();
		if (result==1){
			return;
		}
		if (result>1)
			throw new DAOException(updateSQL+" cause too many updates "+result+" on acc: "+id+", pwd "+password);

		PreparedStatement insert = connection.prepareStatement(insertSQL);
		insert.setString(1, id.getInternalId());
		insert.setString(2, password);
		insert.setLong(3, System.currentTimeMillis());
		result = insert.executeUpdate();
		if (result!=1){
			throw new DAOException("Inserting password failed (rows updated: "+result+" on "+id+", "+password);
		}
	}

	public String getPassword(Connection connection, AccountId id) throws SQLException, DAOException{
		String selectSQL = "SELECT password FROM "+TABLE_NAME+" WHERE accid = ?;";
		PreparedStatement select = connection.prepareStatement(selectSQL);
		select.setString(1, id.getInternalId());
		ResultSet result = select.executeQuery();
		if (!result.next())
			return null;
		return result.getString(1);
	}

	public void deletePassword(Connection connection, AccountId id) throws SQLException, DAOException {
		String deleteSQL = "DELETE FROM "+TABLE_NAME+" WHERE accid = ?;";
		PreparedStatement delete = connection.prepareStatement(deleteSQL);
		delete.setString(1, id.getInternalId());
		int result = delete.executeUpdate();
		//TODO we could use this value to throw an exception, but why?

	}
}