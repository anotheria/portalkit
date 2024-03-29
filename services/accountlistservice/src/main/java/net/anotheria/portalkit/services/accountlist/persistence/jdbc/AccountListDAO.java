package net.anotheria.portalkit.services.accountlist.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.anotheria.portalkit.services.accountlist.AccountIdAdditionalInfo;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

/**
 * Account list DAO implementation.
 * 
 * @author dagafonov
 */
public class AccountListDAO extends AbstractDAO implements DAO {

	/**
	 * 
	 */
	private static final String TABLE_NAME = "accountlist";
	
	/**
	 * 
	 */
	private static final String OWNER_ID = "owner";
	/**
	 * 
	 */
	private static final String TARGET_ID = "target";
	
	/**
	 * 
	 */
	private static final String LIST_NAME = "listName";
	
	/**
	 * 
	 */
	private static final String ADDITIONAL_INFO = "additionalinfo";
	
	/**
	 * 
	 */
	private static final String CREATION_TIMESTAMP = "creationTimestamp";

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
	 * @param connection batabase connection.
	 *            current connection to DB.
	 * @param owner owner id.	
	 * @param listName list name.
	 * @return list of {@link AccountIdAdditionalInfo}
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public List<AccountIdAdditionalInfo> getList(Connection connection, AccountId owner, String listName) throws DAOException, SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			List<AccountIdAdditionalInfo> res = new ArrayList<AccountIdAdditionalInfo>();
			stmt = connection.prepareStatement(String.format("SELECT %s, %s, %s from %s WHERE %s=? and %s=? order by %s asc", TARGET_ID,
					ADDITIONAL_INFO, CREATION_TIMESTAMP, TABLE_NAME, OWNER_ID, LIST_NAME, CREATION_TIMESTAMP));
			stmt.setString(1, owner.getInternalId());
			stmt.setString(2, listName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				AccountIdAdditionalInfo aidai = new AccountIdAdditionalInfo(new AccountId(rs.getString(TARGET_ID)), rs.getString(ADDITIONAL_INFO));
				aidai.setCreationTimestamp(rs.getLong(CREATION_TIMESTAMP));
				res.add(aidai);
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
	 * @param connection batabase connection.
	 *            current connection to DB.
	 * @param owner owner id.
	 * @param listName list name.
	 * @param targets list of {@link AccountIdAdditionalInfo} list of {@link AccountIdAdditionalInfo}
	 * @return boolean
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public boolean addToList(Connection connection, AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets)
			throws DAOException, SQLException {
		PreparedStatement insertStmt = null;
		try {
			String prefSQL = String.format("insert into %s (%s, %s, %s, %s, %s) values (?, ?, ?, ?, ?)", TABLE_NAME, OWNER_ID, TARGET_ID, LIST_NAME,
					ADDITIONAL_INFO, CREATION_TIMESTAMP);

			connection.setAutoCommit(false);
			insertStmt = connection.prepareStatement(prefSQL);

			final boolean batchSupport = connection.getMetaData().supportsBatchUpdates();

			for (AccountIdAdditionalInfo accId : targets) {
				// in case when batch is not supported - should clear prev
				// params
				if (!batchSupport)
					insertStmt.clearParameters();

				insertStmt.setString(1, owner.getInternalId());
				insertStmt.setString(2, accId.getAccountId().getInternalId());
				insertStmt.setString(3, listName);
				insertStmt.setString(4, accId.getAdditionalInfo());
				insertStmt.setLong(5, accId.getCreationTimestamp());

				// performing update - or add to batch
				if (!batchSupport)
					insertStmt.executeUpdate();
				else
					insertStmt.addBatch();
			}
			// execute batch if possible
			if (batchSupport)
				insertStmt.executeBatch();

			// committing stuff
			connection.commit();
			return true;
		} finally {
			JDBCUtil.close(insertStmt);
		}
	}

	/**
	 * Removes targets from specified list of accounts for specified account.
	 * 
	 * @param connection batabase connection.
	 *            current connection to DB.
	 * @param owner owner id.
	 * @param listName list name.
	 * @param targets list of {@link AccountIdAdditionalInfo}
	 * @return boolean
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public boolean removeFromList(Connection connection, AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets)
			throws DAOException, SQLException {
		PreparedStatement deleteStmt = null;
		try {
			String prefSQL = String.format("delete from %s where %s=? and %s=? and %s=? ;", TABLE_NAME, OWNER_ID, TARGET_ID, LIST_NAME);
			final boolean batchSupport = connection.getMetaData().supportsBatchUpdates();
			connection.setAutoCommit(false);
			deleteStmt = connection.prepareStatement(prefSQL);

			for (AccountIdAdditionalInfo accId : targets) {
				// clear prev stuff - when no natch is supported
				if (!batchSupport)
					deleteStmt.clearParameters();

				deleteStmt.setString(1, owner.getInternalId());
				deleteStmt.setString(2, accId.getAccountId().getInternalId());
				deleteStmt.setString(3, listName);
				// depending on batch support - add to the batch or simply
				// execute immediately
				if (!batchSupport)
					deleteStmt.executeUpdate();
				else
					deleteStmt.addBatch();
			}
			if (batchSupport)
				deleteStmt.executeBatch();

			connection.commit();
			return true;
		} finally {
			JDBCUtil.close(deleteStmt);
		}
	}

	/**
	 * Gets list of accounts where I am in the specified list of specified
	 * account.
	 * 
	 * @param connection batabase connection.
	 * @param target target id.
	 * @param listName list name.
	 * @return boolean
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public List<AccountIdAdditionalInfo> getReverseList(Connection connection, AccountId target, String listName) throws DAOException, SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<AccountIdAdditionalInfo> res = new ArrayList<AccountIdAdditionalInfo>();
		try {
			stmt = connection.prepareStatement(String.format("SELECT %s, %s, %s from %s WHERE %s=? and %s=? order by %s asc", OWNER_ID,
					ADDITIONAL_INFO, CREATION_TIMESTAMP, TABLE_NAME, TARGET_ID, LIST_NAME, CREATION_TIMESTAMP));
			stmt.setString(1, target.getInternalId());
			stmt.setString(2, listName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				AccountIdAdditionalInfo aidai = new AccountIdAdditionalInfo(new AccountId(rs.getString(OWNER_ID)), rs.getString(ADDITIONAL_INFO));
				aidai.setCreationTimestamp(rs.getLong(CREATION_TIMESTAMP));
				res.add(aidai);
			}
			return res;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(stmt);
		}
	}

	/**
	 * updateInList
	 * @param connection batabase connection.
	 * @param owner owner id.
	 * @param listName list name.
	 * @param targets list of {@link AccountIdAdditionalInfo}
	 * @return boolean
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public boolean updateInList(Connection connection, AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws DAOException, SQLException {
		PreparedStatement updateStmt = null;
		try {
            String prefSQL = String.format("update %s set %s=?, %s=? where %s=? and %s=? and %s=?", TABLE_NAME, ADDITIONAL_INFO, CREATION_TIMESTAMP,
                    OWNER_ID, TARGET_ID, LIST_NAME);
			connection.setAutoCommit(false);
			updateStmt = connection.prepareStatement(prefSQL);

			final boolean batchSupport = connection.getMetaData().supportsBatchUpdates();

			for (AccountIdAdditionalInfo accId : targets) {
				// in case when batch is not supported - should clear prev
				// params
				if (!batchSupport)
					updateStmt.clearParameters();
                updateStmt.setString(1, accId.getAdditionalInfo());
                updateStmt.setLong(2, accId.getCreationTimestamp());
				updateStmt.setString(3, owner.getInternalId());
				updateStmt.setString(4, accId.getAccountId().getInternalId());
				updateStmt.setString(5, listName);


				// performing update - or add to batch
				if (!batchSupport)
					updateStmt.executeUpdate();
				else
					updateStmt.addBatch();
			}
			// execute batch if possible
			if (batchSupport)
				updateStmt.executeBatch();

			// committing stuff
			connection.commit();
			connection.setAutoCommit(true);
			return true;
		} finally {
			JDBCUtil.close(updateStmt);
		}
	}
}
