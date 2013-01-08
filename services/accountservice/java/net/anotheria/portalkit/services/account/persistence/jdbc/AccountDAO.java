package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:39
 */
public class AccountDAO {
	void saveAccount(Connection connection, Account toSave) throws DAOException,SQLException{
		PreparedStatement stat = connection.prepareStatement(
			"INSERT INTO account (id, name, email) VALUES(?,?,?)"
		) ;
		System.out.println("Saving "+toSave);
		stat.setString(1, toSave.getId().getInternalId());
		stat.setString(2, toSave.getName());
		stat.setString(3, toSave.getEmail());
		int result = stat.executeUpdate();
		System.out.println("Result: "+result);

	}

	public void cleanupFromUnitTests(Connection connection) throws DAOException, SQLException{
		PreparedStatement statement = connection.prepareStatement("DELETE FROM account");
		statement.executeUpdate();
	}
}
