package net.anotheria.portalkit.services.accountlist.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

/**
 * Account list DAO implementation.
 * 
 * @author dagafonov
 * 
 */
public class AccountListDAO extends AbstractDAO implements DAO {

	private static final String TABLE_NAME = "accountlist";
	private static final String OWNER_ID = "owner";
	private static final String TARGET_ID = "target";
	private static final String LIST_NAME = "listName";

	@Override
	protected String[] getTableNames() {
		return new String[] { TABLE_NAME };
	}

	@Override
	public void cleanupFromUnitTests(Connection connection) throws DAOException, SQLException {
		super.cleanupFromUnitTests(connection);
	}

	/**
	 * Gets list of accounts that belongs to specified account ant account list.
	 * Never returs null value.
	 * 
	 * @param connection
	 *            current connection to DB.
	 * @param owner
	 * @param listName
	 * @return
	 * @throws DAOException
	 * @throws SQLException
	 */
	public List<AccountId> getList(Connection connection, AccountId owner, String listName) throws DAOException, SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			List<AccountId> res = new ArrayList<AccountId>();
			stmt = connection.prepareStatement(String.format("SELECT %s from %s WHERE %s=? and %s=?", TARGET_ID, TABLE_NAME, OWNER_ID, LIST_NAME));
			stmt.setString(1, owner.getInternalId());
			stmt.setString(2, listName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				res.add(new AccountId(rs.getString(TARGET_ID)));
			}
			return res;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(stmt);
		}
	}

	/**
	 * Adds targets into specified list of accounts for specified account.
	 * 
	 * @param connection
	 *            current connection to DB.
	 * @param owner
	 * @param listName
	 * @param targets
	 * @return
	 * @throws DAOException
	 * @throws SQLException
	 */
	public boolean addToList(Connection connection, AccountId owner, String listName, Collection<AccountId> targets) throws DAOException,
			SQLException {
		PreparedStatement insertStmt = null;
		try {
			String prefSQL = String.format("insert into %s (%s, %s, %s) values (?, ?, ?)", TABLE_NAME, OWNER_ID, TARGET_ID, LIST_NAME);
			insertStmt = connection.prepareStatement(prefSQL);
			for (AccountId accId : targets) {
				insertStmt.setString(1, owner.getInternalId());
				insertStmt.setString(2, accId.getInternalId());
				insertStmt.setString(3, listName);
				insertStmt.addBatch();
			}
			insertStmt.executeBatch();
			return true;
		} finally {
			JDBCUtil.close(insertStmt);
		}
	}

	/**
	 * Removes targets from specified list of accounts for specified account.
	 * 
	 * @param connection
	 *            current connection to DB.
	 * @param owner
	 * @param listName
	 * @param targets
	 * @return
	 * @throws DAOException
	 * @throws SQLException
	 */
	public boolean removeFromList(Connection connection, AccountId owner, String listName, Collection<AccountId> targets) throws DAOException,
			SQLException {
		PreparedStatement insertStmt = null;
		try {
			String prefSQL = String.format("delete from %s where %s=? and %s=? and %s=? ;", TABLE_NAME, OWNER_ID, TARGET_ID, LIST_NAME);
			insertStmt = connection.prepareStatement(prefSQL);
			for (AccountId accId : targets) {
				insertStmt.setString(1, owner.getInternalId());
				insertStmt.setString(2, accId.getInternalId());
				insertStmt.setString(3, listName);
				insertStmt.addBatch();
			}
			insertStmt.executeBatch();
			return true;
		} finally {
			JDBCUtil.close(insertStmt);
		}
	}

	/**
	 * Gets list of accounts where I am in the specified list of specified
	 * account.
	 * 
	 * @param connection
	 * @param target
	 * @param listName
	 * @return
	 * @throws DAOException
	 * @throws SQLException
	 */
	public List<AccountId> getReverseList(Connection connection, AccountId target, String listName) throws DAOException, SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			List<AccountId> res = new ArrayList<AccountId>();
			stmt = connection.prepareStatement(String.format("SELECT %s from %s WHERE %s=? and %s=?", OWNER_ID, TABLE_NAME, TARGET_ID, LIST_NAME));
			stmt.setString(1, target.getInternalId());
			stmt.setString(2, listName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				res.add(new AccountId(rs.getString(OWNER_ID)));
			}
			return res;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(stmt);
		}
	}
}
