package net.anotheria.portalkit.services.authentication.persistence.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 30.01.13 09:58
 */
@Monitor(subsystem = "authentication", category = "DAO")
public class AuthTokenDAO extends AbstractDAO implements DAO {

	public static final String TABLE_NAME = "auth_token";

	@Override
	protected String[] getTableNames() {
		return new String[]{TABLE_NAME};
	}

	public void savePassword(Connection connection, AccountId id, String password) throws SQLException, DAOException {
		String updateSQL = "UPDATE "+TABLE_NAME+" SET password = ?, "+ATT_DAO_UPDATED+" = ? WHERE accid = ?;";
		String insertSQL = "INSERT INTO "+TABLE_NAME+" (accid, password, "+ATT_DAO_CREATED+") VALUES (?,?,?);";

		PreparedStatement update = null;		
		try {
			update = connection.prepareStatement(updateSQL);
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
		} finally {
			JDBCUtil.close(update);
		}
	}

	public String getPassword(Connection connection, AccountId id) throws SQLException, DAOException{
		String selectSQL = "SELECT password FROM "+TABLE_NAME+" WHERE accid = ?;";
		PreparedStatement select = null;
		ResultSet result = null;
		try {
			select = connection.prepareStatement(selectSQL);
			select.setString(1, id.getInternalId());
			result = select.executeQuery();
			if (!result.next())
				return null;
			return result.getString(1);
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(select);
		}
	}

	public void deletePassword(Connection connection, AccountId id) throws SQLException, DAOException {
		String deleteSQL = "DELETE FROM "+TABLE_NAME+" WHERE accid = ?;";
		PreparedStatement delete = null;
		try {
			delete = connection.prepareStatement(deleteSQL);
			delete.setString(1, id.getInternalId());
			delete.executeUpdate(); // we could use 'int result' value to throw an exception, but why?			
		} finally {
			JDBCUtil.close(delete);
		}
	}

	public void saveAuthToken(Connection connection, AccountId id, String encryptedToken) throws SQLException, DAOException{
		String insertSQL = "INSERT INTO "+TABLE_NAME+" (accid, token, "+ATT_DAO_CREATED+") VALUES (?,?,?)";
		PreparedStatement insert = null;
		try {
			insert = connection.prepareStatement(insertSQL);
			insert.setString(1, id.getInternalId());
			insert.setString(2, encryptedToken);
			insert.setLong(3, System.currentTimeMillis());
			int result = insert.executeUpdate();
			if (result!=1){
				throw new DAOException("Inserting token failed (rows updated: "+result+" on "+id+", "+encryptedToken);
			}
		} finally {
			JDBCUtil.close(insert);
		}
	}

	public void deleteAuthToken(Connection connection, AccountId owner, String encryptedToken) throws SQLException, DAOException{
		String deleteSQL = "DELETE FROM "+TABLE_NAME+" where token=?";
		PreparedStatement delete = null;
		try {
			delete = connection.prepareStatement(deleteSQL);
			delete.setString(1, encryptedToken);
			delete.executeUpdate();
		} finally {
			JDBCUtil.close(delete);
		}
	}

	public void deleteAuthTokens(Connection con, AccountId owner) throws SQLException, DAOException{
		String deleteSQL = "DELETE FROM "+TABLE_NAME+" where accid=?";
		PreparedStatement delete = null;
		try {
			delete = con.prepareStatement(deleteSQL);
			delete.setString(1, owner.getInternalId());
			delete.executeUpdate();
		} finally {
			JDBCUtil.close(delete);
		}
	}

	public boolean authTokenExists(Connection con, String encryptedToken) throws SQLException, DAOException{
		String selectSQL = "SELECT token FROM "+TABLE_NAME+" WHERE token = ?;";
		PreparedStatement select = null;
		ResultSet result = null;
		try {
			select = con.prepareStatement(selectSQL);
			select.setString(1, encryptedToken);
			result = select.executeQuery();
			if (!result.next())
				return false;
			return true;
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(select);
		}
	}

	public Set<String> getAuthTokens(Connection con, AccountId owner) throws SQLException, DAOException{
		HashSet<String> ret = new HashSet<String>();
		String selectSQL = "SELECT token FROM "+TABLE_NAME+" WHERE accid = ?;";
		PreparedStatement select = null;
		ResultSet result = null;
		try {
			select = con.prepareStatement(selectSQL);
			select.setString(1, owner.getInternalId());
			result = select.executeQuery();
			while(result.next()){
				ret.add(result.getString(1));
			}
			return ret;
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(select);
		}
	}
}
