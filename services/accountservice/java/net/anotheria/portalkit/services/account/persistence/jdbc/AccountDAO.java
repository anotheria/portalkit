package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:39
 */
public class AccountDAO extends AbstractDAO implements DAO {

	public static final String TABLE_NAME = "account";

	private static Logger log = Logger.getLogger(AccountDAO.class);

	public static final int POS_ID = 1;
	public static final int POS_NAME = 2;
	public static final int POS_EMAIL = 3;
	public static final int POS_TYPE = 4;
	public static final int POS_REG = 5;
	public static final int MAX_POS = POS_REG;

	void saveAccount(Connection connection, Account toSave) throws DAOException,SQLException{

		String insert = "INSERT INTO account (id, name, email, type, regts, "+ATT_DAO_CREATED+"," + ATT_DAO_UPDATED+") "+
				"SELECT ?,?,?,?,?,?,? WHERE NOT EXISTS (SELECT 1 FROM "+TABLE_NAME+" WHERE id = ? );";
		String update = "UPDATE account set name = ?, email = ?, type = ?, regts = ?, "+ATT_DAO_UPDATED+" = ? WHERE id = ?";
		String sql = update + "; "+insert;
		log.debug("saveAccount, upsert sql : " + sql);

		System.out.println(sql);

		PreparedStatement upsertStatement = connection.prepareStatement(
			sql
		) ;

		//setupdate
		int i=1;
		upsertStatement.setString(i++, toSave.getName());
		upsertStatement.setString(i++, toSave.getEmail());
		upsertStatement.setInt(i++, toSave.getType());
		upsertStatement.setLong(i++, toSave.getRegistrationTimestamp());
		upsertStatement.setLong(i++, System.currentTimeMillis());
		upsertStatement.setString(i++, toSave.getId().getInternalId());

		i--;

		//setinsert
		upsertStatement.setString(i+POS_ID, toSave.getId().getInternalId());
		upsertStatement.setString(i+POS_NAME, toSave.getName());
		upsertStatement.setString(i+POS_EMAIL, toSave.getEmail());
		upsertStatement.setInt(i+POS_TYPE, toSave.getType());
		upsertStatement.setLong(i+POS_REG, toSave.getRegistrationTimestamp());
		upsertStatement.setLong(i+MAX_POS + 1, System.currentTimeMillis());
		upsertStatement.setLong(i+MAX_POS + 2, 0);
		upsertStatement.setString(i+MAX_POS + 3, toSave.getId().getInternalId());

		int insertResult = upsertStatement.executeUpdate();
	}

	Account getAccount(Connection connection, AccountId id) throws DAOException, SQLException{
		PreparedStatement stat = connection.prepareStatement(
			"SELECT id,name, email, type, regts from "+TABLE_NAME+" WHERE id = ?;"
		);
		stat.setString(1, id.getInternalId());
		ResultSet result = stat.executeQuery();
		if (!result.next()){
			return null;
		}

		Account acc = new Account(id);
		acc.setName(result.getString(POS_NAME));
		acc.setEmail(result.getString(POS_EMAIL));
		acc.setRegistrationTimestamp(result.getLong(POS_REG));
		acc.setType(result.getInt(POS_TYPE));
		return acc;
	}

	protected String[] getTableNames(){
		return new String[]{TABLE_NAME};
	}


	public void deleteAccount(Connection connection, AccountId id) throws SQLException, DAOException{
		PreparedStatement delete = connection.prepareStatement(
			"DELETE FROM "+TABLE_NAME+" WHERE id = ?"
		);
		delete.setString(1, id.getInternalId());
		delete.executeUpdate();
	}
}
