package net.anotheria.portalkit.services.account.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

/**
 * DAO class for account object.
 * 
 * @author lrosenberg
 * @since 06.01.13 01:39
 */
public class AccountDAO extends AbstractDAO implements DAO {

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
	/**
	 * Max value of the position field.
	 */
	public static final int MAX_POS = POS_STATUS;

	/**
	 * Internal create account operation.
	 */
	private boolean createAccount(Connection connection, Account toSave) throws SQLException {
		String insert = "INSERT INTO account (id, name, email, type, regts, status, " + ATT_DAO_CREATED + "," + ATT_DAO_UPDATED + ") "
				+ "SELECT ?,?,?,?,?,?,?,? WHERE NOT EXISTS (SELECT 1 FROM " + TABLE_NAME + " WHERE id = ? );";

		PreparedStatement insertStatement = null;
		try {
			insertStatement = connection.prepareStatement(insert);

			insertStatement.setString(POS_ID, toSave.getId().getInternalId());
			insertStatement.setString(POS_NAME, toSave.getName());
			insertStatement.setString(POS_EMAIL, toSave.getEmail());
			insertStatement.setInt(POS_TYPE, toSave.getType());
			insertStatement.setLong(POS_REG, toSave.getRegistrationTimestamp());
			insertStatement.setLong(POS_STATUS, toSave.getStatus());
			insertStatement.setLong(MAX_POS + 1, System.currentTimeMillis());
			insertStatement.setLong(MAX_POS + 2, 0);
			insertStatement.setString(MAX_POS + 3, toSave.getId().getInternalId());

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
		String update = "UPDATE account set name = ?, email = ?, type = ?, regts = ?, status = ?, " + ATT_DAO_UPDATED + " = ? WHERE id = ?";
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
			stat = connection.prepareStatement("SELECT id,name, email, type, regts, status from " + TABLE_NAME + " WHERE id = ?;");
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

}
