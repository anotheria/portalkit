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
	 * Gets list of accounts that belongs to specified account ant account list. Never returs null value.
	 * @param connection
	 * @param owner
	 * @param listName
	 * @return
	 * @throws DAOException
	 * @throws SQLException
	 */
	public List<AccountId> getList(Connection connection, AccountId owner, String listName) throws DAOException, SQLException {
		List<AccountId> res = new ArrayList<AccountId>();
		PreparedStatement stat = connection.prepareStatement(String.format("SELECT %s from %s WHERE %s=? and %s=?", TARGET_ID, TABLE_NAME, OWNER_ID, LIST_NAME));

		System.out.println("searching for " + owner.getInternalId() + " in list '" + listName+"'");

		stat.setString(1, owner.getInternalId());
		stat.setString(2, listName);
		ResultSet result = stat.executeQuery();
		while (result.next()) {
			System.out.println("found " + result.getString(TARGET_ID));
			res.add(new AccountId(result.getString(TARGET_ID)));
		}
		return res;
	}

	public boolean addToList(Connection connection, AccountId owner, String listName, Collection<AccountId> targets) throws DAOException, SQLException {
		String prefSQL = String.format("insert into %s (%s, %s, %s) values (?, ?, ?)", TABLE_NAME, OWNER_ID, TARGET_ID, LIST_NAME);
		PreparedStatement insertStatement = connection.prepareStatement(prefSQL);
		for (AccountId accId : targets) {
			System.out.println("inserting owner=" + owner.getInternalId() + ", listname=" + listName + ", target=" + accId.getInternalId());
			insertStatement.setString(1, owner.getInternalId());
			insertStatement.setString(2, accId.getInternalId());
			insertStatement.setString(3, listName);
			insertStatement.addBatch();
		}
		insertStatement.executeBatch();
		return true;
	}

	public boolean removeFromList(Connection connection, AccountId owner, String listName, Collection<AccountId> targets) throws DAOException, SQLException {
		String prefSQL = String.format("delete from %s where %s=? and %s=? and %s=? ;", TABLE_NAME, OWNER_ID, TARGET_ID, LIST_NAME);
		PreparedStatement insertStatement = connection.prepareStatement(prefSQL);
		for (AccountId accId : targets) {
			System.out.println("deleting owner=" + owner.getInternalId() + ", listname=" + listName + ", target=" + accId.getInternalId());
			insertStatement.setString(1, owner.getInternalId());
			insertStatement.setString(2, accId.getInternalId());
			insertStatement.setString(3, listName);
			insertStatement.addBatch();
		}
		insertStatement.executeBatch();
		return true;
	}

	public List<AccountId> getReverseList(Connection connection, AccountId target, String listName) throws DAOException, SQLException {
		List<AccountId> res = new ArrayList<AccountId>();
		PreparedStatement stat = connection.prepareStatement(String.format("SELECT %s from %s WHERE %s=? and %s=?", OWNER_ID, TABLE_NAME, TARGET_ID, LIST_NAME));

		System.out.println("searching for " + target.getInternalId() + " in list '" + listName+"'");

		stat.setString(1, target.getInternalId());
		stat.setString(2, listName);
		ResultSet result = stat.executeQuery();
		while (result.next()) {
			System.out.println("found " + result.getString(OWNER_ID));
			res.add(new AccountId(result.getString(OWNER_ID)));
		}
		return res;
	}
}
