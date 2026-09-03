package net.anotheria.portalkit.services.authentication.persistence.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.authentication.EncryptedAuthToken;
import net.anotheria.portalkit.services.authentication.TokenInventoryEntry;
import net.anotheria.portalkit.services.authentication.TokenObfuscator;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	/**
	 * Name of the column carrying the last used timestamp, added by V1_3__AddLastUsedAt.sql.
	 */
	public static final String ATT_LAST_USED = "last_used_at";

	/**
	 * The columns a token inventory entry is built from, in the order the result set is read in.
	 */
	private static final String INVENTORY_FIELDS = "accid, token, type, expiryTimestamp, multiUse, exclusive, exclusiveInType, "
			+ ATT_DAO_CREATED + ", " + ATT_LAST_USED;

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

	public void saveAuthToken(Connection connection, AccountId id, EncryptedAuthToken encryptedToken) throws SQLException, DAOException{
		String insertSQL = "INSERT INTO "+TABLE_NAME+" (accid, token, "+ATT_DAO_CREATED+", expiryTimestamp,multiUse,exclusive,exclusiveInType,type) VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement insert = null;
		try {
			insert = connection.prepareStatement(insertSQL);
			insert.setString(1, id.getInternalId());
			insert.setString(2, encryptedToken.getEncryptedVersion());
			insert.setLong(3, System.currentTimeMillis());
			insert.setLong(4, encryptedToken.getAuthToken().getExpiryTimestamp());
			insert.setBoolean(5, encryptedToken.getAuthToken().isMultiUse());
			insert.setBoolean(6, encryptedToken.getAuthToken().isExclusive());
			insert.setBoolean(7, encryptedToken.getAuthToken().isExclusiveInType());
			insert.setInt(8, encryptedToken.getAuthToken().getType());

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
	public long getAuthTokensCount(Connection connection) throws SQLException, DAOException{
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			stat = connection.prepareStatement("SELECT count(accid) from " + TABLE_NAME);
			result = stat.executeQuery();
			return result.next() ? result.getLong(1) : 0;
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(stat);
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

	/**
	 * Sets the last used timestamp, but only if the stored one is unknown or older than the given threshold.
	 * The condition is part of the statement so that a token which is used constantly costs one update per
	 * interval and not one per authentication.
	 *
	 * @param con             connection.
	 * @param encryptedToken  the token which was used.
	 * @param timestamp       timestamp to store.
	 * @param onlyIfOlderThan store only if the currently stored value is null or smaller than this.
	 * @throws SQLException if error.
	 * @throws DAOException if error.
	 */
	public void updateLastUsed(Connection con, String encryptedToken, long timestamp, long onlyIfOlderThan) throws SQLException, DAOException{
		String updateSQL = "UPDATE "+TABLE_NAME+" SET "+ATT_LAST_USED+" = ? WHERE token = ? AND ("+ATT_LAST_USED+" IS NULL OR "+ATT_LAST_USED+" < ?)";
		PreparedStatement update = null;
		try {
			update = con.prepareStatement(updateSQL);
			update.setLong(1, timestamp);
			update.setString(2, encryptedToken);
			update.setLong(3, onlyIfOlderThan);
			update.executeUpdate();
		} finally {
			JDBCUtil.close(update);
		}
	}

	/**
	 * Returns the inventory entries of all tokens of the given owner.
	 *
	 * @param con   connection.
	 * @param owner owner of the tokens.
	 * @return the entries, never null.
	 * @throws SQLException if error.
	 * @throws DAOException if error.
	 */
	public List<TokenInventoryEntry> getTokenInventoryByAccount(Connection con, AccountId owner) throws SQLException, DAOException{
		String selectSQL = "SELECT "+INVENTORY_FIELDS+" FROM "+TABLE_NAME+" WHERE accid = ?";
		PreparedStatement select = null;
		ResultSet result = null;
		try {
			select = con.prepareStatement(selectSQL);
			select.setString(1, owner.getInternalId());
			result = select.executeQuery();
			return readEntries(result);
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(select);
		}
	}

	/**
	 * Returns the inventory entries of tokens of the given type, bound by limit and offset.
	 *
	 * @param con    connection.
	 * @param type   token type.
	 * @param limit  maximum number of entries.
	 * @param offset number of entries to skip.
	 * @return the entries, never null.
	 * @throws SQLException if error.
	 * @throws DAOException if error.
	 */
	public List<TokenInventoryEntry> getTokenInventoryByType(Connection con, int type, int limit, int offset) throws SQLException, DAOException{
		String selectSQL = "SELECT "+INVENTORY_FIELDS+" FROM "+TABLE_NAME+" WHERE type = ? ORDER BY token LIMIT ? OFFSET ?";
		PreparedStatement select = null;
		ResultSet result = null;
		try {
			select = con.prepareStatement(selectSQL);
			select.setInt(1, type);
			select.setInt(2, limit);
			select.setInt(3, offset);
			result = select.executeQuery();
			return readEntries(result);
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(select);
		}
	}

	/**
	 * Reads inventory entries from a result set which was selected with {@link #INVENTORY_FIELDS}.
	 *
	 * @param result the result set.
	 * @return the entries, never null.
	 * @throws SQLException if error.
	 */
	private List<TokenInventoryEntry> readEntries(ResultSet result) throws SQLException{
		List<TokenInventoryEntry> ret = new ArrayList<TokenInventoryEntry>();
		while (result.next()){
			AccountId accountId = new AccountId(result.getString(1));
			String obfuscated = TokenObfuscator.obfuscate(result.getString(2));

			int type = result.getInt(3);
			if (result.wasNull())
				type = TokenInventoryEntry.TYPE_UNKNOWN;

			long expiry = result.getLong(4);
			boolean multiUse = result.getBoolean(5);
			boolean exclusive = result.getBoolean(6);
			boolean exclusiveInType = result.getBoolean(7);

			long created = result.getLong(8);
			if (result.wasNull())
				created = TokenInventoryEntry.TIMESTAMP_UNKNOWN;

			long lastUsed = result.getLong(9);
			if (result.wasNull())
				lastUsed = TokenInventoryEntry.TIMESTAMP_UNKNOWN;

			ret.add(new TokenInventoryEntry(accountId, type, obfuscated, created, lastUsed, expiry, multiUse, exclusive, exclusiveInType));
		}
		return ret;
	}
}
