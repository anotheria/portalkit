package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountQuery;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for account object.
 * 
 * @author lrosenberg
 * @since 06.01.13 01:39
 */
@Monitor(category = "DAO", subsystem = "account")
public class AccountDAO extends AbstractDAO implements DAO {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountDAO.class);

	/**
	 * Name of the table in the database.
	 */
	public static final String TABLE_NAME = "account";

	/**
	 * Position of the id field in insert/update statements.
	 */
	public static final int POS_ID = 1;
	/**
	 * Position of the name field in insert/update statements.
	 */
	public static final int POS_NAME = 2;
	/**
	 * Position of the email field in insert/update statements.
	 */
	public static final int POS_EMAIL = 3;
	/**
	 * Position of the type field in insert/update statements.
	 */
	public static final int POS_TYPE = 4;
	/**
	 * Position of the regtimestamp field in insert/update statements.
	 */
	public static final int POS_REG = 5;
	/**
	 * Position of the status field in insert/update statements.
	 */
	public static final int POS_STATUS = 6;

	public static final int POS_TENANT = 7;
	/**
	 * Max value of the position field.
	 */
	public static final int MAX_POS = POS_TENANT;

	/**
	 * Internal create account operation.
	 */
	private boolean createAccount(Connection connection, Account toSave) throws SQLException {
		String insert = "INSERT INTO " + TABLE_NAME + "(id, name, email, type, regts, status, tenant, " + ATT_DAO_CREATED + "," + ATT_DAO_UPDATED + ") "
				+ "VALUES ( ?,?,?,?,?,?,?,?,? )";

		try {
			Account acc = getAccount(connection, toSave.getId());
			if (acc != null) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		PreparedStatement insertStatement = null;
		try {
			insertStatement = connection.prepareStatement(insert);

			insertStatement.setString(POS_ID, toSave.getId().getInternalId());
			insertStatement.setString(POS_NAME, toSave.getName());
			insertStatement.setString(POS_EMAIL, toSave.getEmail());
			insertStatement.setInt(POS_TYPE, toSave.getType());
			insertStatement.setLong(POS_REG, toSave.getRegistrationTimestamp());
			insertStatement.setLong(POS_STATUS, toSave.getStatus());
			insertStatement.setString(POS_TENANT, toSave.getTenant());
			insertStatement.setLong(MAX_POS + 1, System.currentTimeMillis());
			insertStatement.setLong(MAX_POS + 2, 0);
			// insertStatement.setString(MAX_POS + 3, toSave.getId().getInternalId());

			int insertResult = insertStatement.executeUpdate();
			System.out.println("INSERT: " + insertResult);
			return insertResult == 1;
		} finally {
			JDBCUtil.close(insertStatement);
		}
	}

	/**
	 * Internal update operation.
	 */
	private boolean updateAccount(Connection connection, Account toSave) throws SQLException, DAOException {
		String update = "UPDATE " + TABLE_NAME + " set name = ?, email = ?, type = ?, regts = ?, status = ?, tenant = ?, " + ATT_DAO_UPDATED + " = ? WHERE id = ?";
		PreparedStatement updateStatement = null;
		try {
			updateStatement = connection.prepareStatement(update);

			// setupdate
			int i = 1;
			// /*
			updateStatement.setString(i++, toSave.getName());
			updateStatement.setString(i++, toSave.getEmail());
			updateStatement.setInt(i++, toSave.getType());
			updateStatement.setLong(i++, toSave.getRegistrationTimestamp());
			updateStatement.setLong(i++, toSave.getStatus());
			updateStatement.setString(i++, toSave.getTenant());
			updateStatement.setLong(i++, System.currentTimeMillis());
			updateStatement.setString(i++, toSave.getId().getInternalId());
			// */

			int updateResult = updateStatement.executeUpdate();
			if (updateResult > 1)
				throw new DAOException("Update " + update + " on " + toSave + " returned more than 1");

			return updateResult == 1;
		} finally {
			JDBCUtil.close(updateStatement);
		}
	}

	void saveAccount(Connection connection, Account toSave) throws DAOException, SQLException {
		if (!updateAccount(connection, toSave)) {
			createAccount(connection, toSave);
		}
	}

	Account getAccount(Connection connection, AccountId id) throws DAOException, SQLException {
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			stat = connection.prepareStatement("SELECT id,name, email, type, regts, status, tenant from " + TABLE_NAME + " WHERE id = ?;");
			stat.setString(1, id.getInternalId());
			result = stat.executeQuery();
			if (!result.next()) {
				return null;
			}

			Account acc = new Account(id);
			acc.setName(result.getString(POS_NAME));
			acc.setEmail(result.getString(POS_EMAIL));
			acc.setRegistrationTimestamp(result.getLong(POS_REG));
			acc.setType(result.getInt(POS_TYPE));
			acc.setStatus(result.getLong(POS_STATUS));
			acc.setTenant(result.getString(POS_TENANT));
			return acc;
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(stat);
		}
	}

	public List<AccountId> getAccountIds(Connection connection) throws DAOException, SQLException {
		List<AccountId> result = new ArrayList<AccountId>();

		Statement st = null;
		ResultSet rs = null;
		try {
			st = connection.createStatement();
			rs = st.executeQuery("SELECT id FROM " + TABLE_NAME + ";");
			while (rs.next())
				result.add(new AccountId(rs.getString(POS_ID)));

			return result;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(st);
		}
	}

	@Override
	protected String[] getTableNames() {
		return new String[] { TABLE_NAME };
	}

	public void deleteAccount(Connection connection, AccountId id) throws SQLException, DAOException {
		PreparedStatement delete = null;
		try {
			delete = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE id = ?");
			delete.setString(1, id.getInternalId());
			delete.executeUpdate();
		} finally {
			JDBCUtil.close(delete);
		}
	}

	public AccountId getIdByName(Connection connection, String name) throws SQLException, DAOException {
		return getIdByField(connection, "name", name);
	}

	public AccountId getIdByEmail(Connection connection, String email) throws SQLException, DAOException {
		return getIdByField(connection, "email", email);
	}

	private AccountId getIdByField(Connection connection, String fieldName, String fieldValue) throws SQLException, DAOException {
		String sql = "SELECT id FROM " + TABLE_NAME + " WHERE " + fieldName + " = ?";
		PreparedStatement select = null;
		ResultSet result = null;

		try {
			select = connection.prepareStatement(sql);
			select.setString(1, fieldValue);
			result = select.executeQuery();
			if (!result.next())
				return null;
			return new AccountId(result.getString(1));
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(select);
		}
	}

	public List<AccountId> getAccountIdsByType(Connection con, int typeId) throws SQLException, DAOException {
		List<AccountId> result = new ArrayList<AccountId>();
		String sql = "SELECT id FROM " + TABLE_NAME + " WHERE type = ?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			select = con.prepareStatement(sql);
			select.setInt(1, typeId);
			rs = select.executeQuery();
			while (rs.next()) {
				result.add(new AccountId(rs.getString("id")));
			}
			return result;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(select);
		}
	}

	public List<Account> getAccountsByQuery(final Connection con, final AccountQuery query) throws SQLException {
		final String sqlSelectPart = "SELECT id, name, email, type, status, regts FROM " + TABLE_NAME;
		final String sqlWherePart = " WHERE 1=1";
		final String sqlOrderPart = " ORDER BY regts DESC";
		// general selection part
		final StringBuilder sqlRawQuery = new StringBuilder(sqlSelectPart);
		// filtering part
		sqlRawQuery.append(sqlWherePart);
		if (query.getRegisteredFrom() != null)
			sqlRawQuery.append(" AND regts >= ").append(query.getRegisteredFrom());
		if (query.getRegisteredTill() != null)
			sqlRawQuery.append(" AND regts <= ").append(query.getRegisteredTill());
		if (!query.getIds().isEmpty()) {
			final String ids = StringUtils.concatenateTokens(query.getIds(), ',', '\'', '\'');
			sqlRawQuery.append(" AND id IN (").append(ids).append(")");
		}
		if (!StringUtils.isEmpty(query.getEmailMask()))
			sqlRawQuery.append(" AND email LIKE '").append(query.getEmailMask()).append("'");
		if (!StringUtils.isEmpty(query.getNameMask()))
			sqlRawQuery.append(" AND name LIKE '").append(query.getNameMask()).append("'");
		if (!StringUtils.isEmpty(query.getIdMask()))
			sqlRawQuery.append(" AND id LIKE '").append(query.getIdMask()).append("'");
		if (!query.getTypesIncluded().isEmpty()) {
			final String types = StringUtils.concatenateTokens(query.getTypesIncluded(), ",");
			sqlRawQuery.append(" AND type IN (").append(types).append(")");
		}
		if (!query.getTypesExcluded().isEmpty()) {
			final String types = StringUtils.concatenateTokens(query.getTypesExcluded(), ",");
			sqlRawQuery.append(" AND type NOT IN (").append(types).append(")");
		}
		// ordering part
		sqlRawQuery.append(sqlOrderPart);

		final String sqlQuery = sqlRawQuery.toString();
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("getAccountsByQuery(Connection, AccountQuery) executing with query[" + sqlQuery + "].");

		final List<Account> rawResult = new ArrayList<Account>();
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(sqlQuery);
			while (rs.next()) {
				Account account = new Account();
				account.setId(new AccountId(rs.getString("id")));
				account.setName(rs.getString("name"));
				account.setEmail(rs.getString("email"));
				account.setType(rs.getInt("type"));
				account.setStatus(rs.getLong("status"));
				account.setRegistrationTimestamp(rs.getLong("regts"));
				rawResult.add(account);
			}
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(statement);
		}

		if (rawResult.isEmpty()) // no results
			return rawResult;
		if (query.getStatusesIncluded().isEmpty() && query.getStatusesExcluded().isEmpty()) // no filtering by status
			return rawResult;

		final List<Account> result = new ArrayList<Account>();
		// now we should exclude accounts by status field - we can't do this in the query because value stored as long bitmap
		for (final Account account : rawResult) {
			boolean skip = false;
			for (final Long status : query.getStatusesIncluded())
				if (!account.hasStatus(status)) { // skipping account if it doesn't have required status
					skip = true;
					break;
				}
			if (skip)
				continue;

			for (final Long status : query.getStatusesExcluded())
				if (account.hasStatus(status)) { // skipping account if it have doesn't required status
					skip = true;
					break;
				}
			if (skip)
				continue;

			result.add(account);
		}

		return result;
	}

}
