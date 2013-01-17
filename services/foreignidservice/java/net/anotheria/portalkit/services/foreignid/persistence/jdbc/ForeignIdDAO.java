package net.anotheria.portalkit.services.foreignid.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.foreignid.ForeignId;

import org.apache.log4j.Logger;

public class ForeignIdDAO extends AbstractDAO implements DAO {

	public static final String TABLE_NAME = "foreignid";

	private static Logger log = Logger.getLogger(ForeignIdDAO.class);

	@Override
	protected String[] getTableNames() {
		return new String[] { TABLE_NAME };
	}

	public void link(Connection connection, AccountId accId, int sid, String fid) throws DAOException, SQLException {
		PreparedStatement insertStatement = connection.prepareStatement("insert into " + TABLE_NAME
				+ " (accid, sourceid, foreignid) values (?, ?, ?);");
		insertStatement.setString(1, accId.getInternalId());
		insertStatement.setInt(2, sid);
		insertStatement.setString(3, fid);
		int insertResult = insertStatement.executeUpdate();
		System.out.println("INSERT: " + insertResult);
	}

	public AccountId getAccountIdByForeignId(Connection connection, int sid, String fid) throws DAOException, SQLException {
		PreparedStatement stat = connection.prepareStatement("SELECT accid from " + TABLE_NAME + " WHERE sourceid = ? and foreignid = ?;");
		stat.setInt(1, sid);
		stat.setString(2, fid);
		ResultSet result = stat.executeQuery();
		if (!result.next()) {
			return null;
		}
		AccountId accId = new AccountId(result.getString(1));
		return accId;
	}

	public List<ForeignId> getForeignIdsByAccountId(Connection connection, AccountId accId) throws DAOException, SQLException {
		PreparedStatement stat = connection.prepareStatement("SELECT * from " + TABLE_NAME + " WHERE accid = ?;");
		stat.setString(1, accId.getInternalId());
		ResultSet result = stat.executeQuery();
		List<ForeignId> res = new ArrayList<ForeignId>();
		while (result.next()) {
			res.add(new ForeignId(new AccountId(result.getString("accid")), result.getInt("sourceid"), result.getString("foreignid")));
		}
		return res;
	}

}
