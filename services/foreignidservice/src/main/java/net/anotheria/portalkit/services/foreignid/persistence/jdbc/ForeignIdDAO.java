package net.anotheria.portalkit.services.foreignid.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ForeignId DAO.
 * 
 * @author dagafonov
 * 
 */
public class ForeignIdDAO extends AbstractDAO implements DAO {

	/**
	 * 
	 */
	public static final String TABLE_NAME = "foreignid";
	/**
	 * 
	 */
	public static final String ACCOUNTID_FIELD_NAME = "accid";
	/**
	 * 
	 */
	public static final String SOURCEID_FIELD_NAME = "sourceid";
	/**
	 * 
	 */
	public static final String FOREIGN_FIELD_NAME = "foreignid";

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(ForeignIdDAO.class);

	@Override
	protected String[] getTableNames() {
		return new String[] { TABLE_NAME };
	}

	/**
	 * Links account to foreign system.
	 * 
	 * @param connection
	 * @param accId
	 * @param sid
	 * @param fid
	 * @throws DAOException
	 * @throws SQLException
	 */
	public void link(Connection connection, AccountId accId, int sid, String fid) throws DAOException, SQLException {
		PreparedStatement insertStatement = null;
		try {
			insertStatement = connection.prepareStatement("insert into " + TABLE_NAME + " (" + ACCOUNTID_FIELD_NAME + ", " + SOURCEID_FIELD_NAME
					+ ", " + FOREIGN_FIELD_NAME + ") values (?, ?, ?);");
			insertStatement.setString(1, accId.getInternalId());
			insertStatement.setInt(2, sid);
			insertStatement.setString(3, fid);
			int insertResult = insertStatement.executeUpdate();
		} finally {
			JDBCUtil.close(insertStatement);
		}
	}

	/**
	 * Gets {@link AccountId} by foreign id fields.
	 * 
	 * @param connection
	 * @param sid
	 * @param fid
	 * @return {@link AccountId}
	 * @throws DAOException
	 * @throws SQLException
	 */
	public AccountId getAccountIdByForeignId(Connection connection, int sid, String fid) throws DAOException, SQLException {
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			stat = connection.prepareStatement("SELECT " + ACCOUNTID_FIELD_NAME + " from " + TABLE_NAME + " WHERE " + SOURCEID_FIELD_NAME
					+ " = ? and " + FOREIGN_FIELD_NAME + " = ?;");
			stat.setInt(1, sid);
			stat.setString(2, fid);
			result = stat.executeQuery();
			if (!result.next()) {
				return null;
			}
			AccountId accId = new AccountId(result.getString(1));
			return accId;
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(stat);
		}

	}

	/**
	 * Gets list of linked {@link ForeignId}'s by {@link AccountId}.
	 * 
	 * @param connection
	 * @param accId
	 * @return {@link List<ForeignId>}
	 * @throws DAOException
	 * @throws SQLException
	 */
	public List<ForeignId> getForeignIdsByAccountId(Connection connection, AccountId accId) throws DAOException, SQLException {
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			stat = connection.prepareStatement("SELECT * from " + TABLE_NAME + " WHERE " + ACCOUNTID_FIELD_NAME + " = ?;");
			stat.setString(1, accId.getInternalId());
			result = stat.executeQuery();
			List<ForeignId> res = new ArrayList<ForeignId>();
			while (result.next()) {
				res.add(new ForeignId(new AccountId(result.getString(ACCOUNTID_FIELD_NAME)), result.getInt(SOURCEID_FIELD_NAME), result
						.getString(FOREIGN_FIELD_NAME)));
			}
			return res;
		} finally {
			JDBCUtil.close(result);
			JDBCUtil.close(stat);
		}
	}

	/**
	 * Unlink {@link AccountId} from foreign system.
	 * 
	 * @param connection
	 * @param accId
	 * @param sid
	 * @param fid
	 * @throws DAOException
	 * @throws SQLException
	 */
	public void unlink(Connection connection, AccountId accId, int sid, String fid) throws DAOException, SQLException {
		PreparedStatement deleteStatement = null;
		try {
			deleteStatement = connection.prepareStatement("delete from " + TABLE_NAME + " where " + ACCOUNTID_FIELD_NAME + " = ? and "
					+ SOURCEID_FIELD_NAME + " = ? and " + FOREIGN_FIELD_NAME + " = ?;");
			deleteStatement.setString(1, accId.getInternalId());
			deleteStatement.setInt(2, sid);
			deleteStatement.setString(3, fid);
			int deleteResult = deleteStatement.executeUpdate();
		} finally {
			JDBCUtil.close(deleteStatement);
		}
	}

	/**
	 * 
	 * @param connection
	 * @param sid
	 * @param fid
	 * @throws DAOException
	 * @throws SQLException
	 */
	public void unlink(Connection connection, int sid, String fid) throws DAOException, SQLException {
		AccountId accid = getAccountIdByForeignId(connection, sid, fid);
		unlink(connection, accid, sid, fid);
	}

}
