package net.anotheria.portalkit.services.pushtoken.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PushTokenDAO extends AbstractDAO implements DAO {

    /**
     *
     */
    public static final String TABLE_NAME = "push_token";
    /**
     *
     */
    public static final String ACCOUNTID_FIELD_NAME = "accid";
    /**
     *
     */
    public static final String TOKEN_FIELD_NAME = "token";

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(PushTokenDAO.class);

    @Override
    protected String[] getTableNames() {
        return new String[]{TABLE_NAME};
    }

    public List<String> getAllByAccountId(Connection connection, AccountId accountId) throws DAOException, SQLException {
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = connection.prepareStatement("SELECT * from " + TABLE_NAME + " WHERE " + ACCOUNTID_FIELD_NAME + " = ?;");
            stat.setString(1, accountId.getInternalId());
            result = stat.executeQuery();
            List<String> res = new ArrayList<>();
            while (result.next()) {
                res.add(result.getString(TOKEN_FIELD_NAME));
            }
            return res;
        } finally {
            JDBCUtil.close(result);
            JDBCUtil.close(stat);
        }
    }

    public void saveForAccount(Connection connection, AccountId accountId, String token) throws DAOException, SQLException {
        PreparedStatement stat = null;
        try {
            stat = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (accid, token) VALUES (?, ?);");
            stat.setString(1, accountId.getInternalId());
            stat.setString(2, token);
            stat.executeUpdate();
        } finally {
            JDBCUtil.close(stat);
        }
    }

    public void deleteToken(Connection connection, String token) throws DAOException, SQLException {
        PreparedStatement stat = null;
        try {
            stat = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE " + TOKEN_FIELD_NAME + " = ?;");
            stat.setString(1, token);
            stat.executeUpdate();
        } finally {
            JDBCUtil.close(stat);
        }
    }
}
