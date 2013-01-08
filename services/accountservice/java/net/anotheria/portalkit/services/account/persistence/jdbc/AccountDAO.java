package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.Account;
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
 * @since 06.01.13 01:39
 */
public class AccountDAO extends AbstractDAO implements DAO {

	public static final String TABLE_NAME = "account";

	public static final int POS_ID = 1;
	public static final int POS_NAME = 2;
	public static final int POS_EMAIL = 3;
	public static final int POS_TYPE = 4;
	public static final int POS_REG = 5;
	public static final int MAX_POS = POS_REG;

	void saveAccount(Connection connection, Account toSave) throws DAOException,SQLException{
		PreparedStatement stat = connection.prepareStatement(
			"INSERT INTO account (id, name, email, type, regts, "+DAO_STD_FIELD_DECL+") VALUES(?,?,?,?,?,"+DAO_STD_FIELD_VALUES+")"
		) ;
		stat.setString(POS_ID, toSave.getId().getInternalId());
		stat.setString(POS_NAME, toSave.getName());
		stat.setString(POS_EMAIL, toSave.getEmail());
		stat.setInt(POS_TYPE, toSave.getType());
		stat.setLong(POS_REG, toSave.getRegistrationTimestamp());
		fillCreatedStatement(stat, MAX_POS);


		int result = stat.executeUpdate();
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


}
