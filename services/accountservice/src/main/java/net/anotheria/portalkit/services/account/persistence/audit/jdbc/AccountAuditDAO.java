package net.anotheria.portalkit.services.account.persistence.audit.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.account.AccountAudit;
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

/**
 * DAO class for account audit object.
 *
 * @author ykalapusha
 */
@Monitor(category = "DAO", subsystem = "account-audit")
public class AccountAuditDAO extends AbstractDAO implements DAO {
    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountAuditDAO.class);
    /**
     * Name of the table in the database.
     */
    private static final String TABLE_NAME = "account_audit";

    @Override
    protected String[] getTableNames() {
        return new String[] {TABLE_NAME};
    }

    void saveAccountAudit(Connection connection, AccountAudit accountAudit) throws DAOException, SQLException {
        String insert = "INSERT INTO " + TABLE_NAME + " (accountid, oldStatus, newStatus, statusRemoved, statusAdded, timestamp) VALUES (?,?,?,?,?,?)";

        PreparedStatement insertStatement = null;
        try {
            insertStatement = connection.prepareStatement(insert);

            insertStatement.setString(1, accountAudit.getAccountId().getInternalId());
            insertStatement.setLong(2, accountAudit.getOldStatus());
            insertStatement.setLong(3, accountAudit.getNewStatus());
            insertStatement.setLong(4, accountAudit.getStatusRemoved());
            insertStatement.setLong(5, accountAudit.getStatusAdded());
            insertStatement.setLong(6, accountAudit.getCreated());

            insertStatement.executeUpdate();
        } finally {
            JDBCUtil.close(insertStatement);
        }
    }


    List<AccountAudit> getAccountAuditsById (Connection connection, AccountId accountId) throws DAOException, SQLException {
        List<AccountAudit> result = new ArrayList<>();

        String selectQuery = "SELECT id, accountId, oldStatus, newStatus, statusRemoved, statusAdded, timestamp FROM " + TABLE_NAME + " WHERE accountId=?;";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(selectQuery);
            st.setString(1, accountId.getInternalId());
            rs = st.executeQuery();

            while (rs.next()) {
                AccountAudit accountAudit = new AccountAudit();
                accountAudit.setId(rs.getLong("id"));
                accountAudit.setAccountId(new AccountId(rs.getString("accountid")));
                accountAudit.setOldStatus(rs.getLong("oldstatus"));
                accountAudit.setNewStatus(rs.getLong("newstatus"));
                accountAudit.setStatusRemoved(rs.getLong("statusremoved"));
                accountAudit.setStatusAdded(rs.getLong("statusadded"));
                accountAudit.setCreated(rs.getLong("timestamp"));

                result.add(accountAudit);
            }
            return result;
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
        }
    }
}
