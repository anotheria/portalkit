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
	public static final String ACCOUNTID_FIELD_NAME = "accid";
	public static final String SOURCEID_FIELD_NAME = "sourceid";
	public static final String FOREIGN_FIELD_NAME = "foreignid";

	private static Logger log = Logger.getLogger(ForeignIdDAO.class);

	@Override
	protected String[] getTableNames() {
		return new String[] { TABLE_NAME };
	}

	public void link(Connection connection, AccountId accId, int sid, String fid) throws DAOException, SQLException {
		PreparedStatement insertStatement = connection.prepareStatement("insert into " + TABLE_NAME + " (" + ACCOUNTID_FIELD_NAME + ", "
				+ SOURCEID_FIELD_NAME + ", " + FOREIGN_FIELD_NAME + ") values (?, ?, ?);");
		insertStatement.setString(1, accId.getInternalId());
		insertStatement.setInt(2, sid);
		insertStatement.setString(3, fid);
		int insertResult = insertStatement.executeUpdate();
		System.out.println("INSERT: " + insertResult);
	}

	public AccountId getAccountIdByForeignId(Connection connection, int sid, String fid) throws DAOException, SQLException {
		PreparedStatement stat = connection.prepareStatement("SELECT " + ACCOUNTID_FIELD_NAME + " from " + TABLE_NAME + " WHERE "
				+ SOURCEID_FIELD_NAME + " = ? and " + FOREIGN_FIELD_NAME + " = ?;");
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
		PreparedStatement stat = connection.prepareStatement("SELECT * from " + TABLE_NAME + " WHERE " + ACCOUNTID_FIELD_NAME + " = ?;");
		stat.setString(1, accId.getInternalId());
		ResultSet result = stat.executeQuery();
		List<ForeignId> res = new ArrayList<ForeignId>();
		while (result.next()) {
			res.add(new ForeignId(new AccountId(result.getString(ACCOUNTID_FIELD_NAME)), result.getInt(SOURCEID_FIELD_NAME), result
					.getString(FOREIGN_FIELD_NAME)));
		}
		return res;
	}

	public void unlink(Connection connection, AccountId accId, int sid, String fid) throws DAOException, SQLException {
		PreparedStatement deleteStatement = connection.prepareStatement("delete from " + TABLE_NAME + " where " + ACCOUNTID_FIELD_NAME + " = ? and "
				+ SOURCEID_FIELD_NAME + " = ? and " + FOREIGN_FIELD_NAME + " = ?;");
		deleteStatement.setString(1, accId.getInternalId());
		deleteStatement.setInt(2, sid);
		deleteStatement.setString(3, fid);
		int deleteResult = deleteStatement.executeUpdate();
		System.out.println("DELETE: " + deleteResult);
	}

	public void unlink(Connection connection, int sid, String fid) throws DAOException, SQLException {
		AccountId accid = getAccountIdByForeignId(connection, sid, fid);
		unlink(connection, accid, sid, fid);
	}

}
