package net.anotheria.portalkit.services.account.persistence.audit.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.account.AccountAudit;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceService;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * {@link AccountAuditPersistenceService} jdbc implementation.
 *
 * @author ykalapusha
 */
@Monitor(category = "portalkit-persistence-service", subsystem = "account-audit")
public class JDBCAccountAuditPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AccountAuditPersistenceService {

    private AccountAuditDAO accountAuditDAO;

    public JDBCAccountAuditPersistenceServiceImpl() {
        super("pk-jdbc-account-audit");

        accountAuditDAO = new AccountAuditDAO();
        addDaos(accountAuditDAO);
    }

    @Override
    public void saveAccountAudit(AccountAudit accountAudit) throws AccountAuditPersistenceServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            accountAuditDAO.saveAccountAudit(connection, accountAudit);
        } catch (DAOException | SQLException e) {
            throw new AccountAuditPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(connection);
        }
    }


    @Override
    public List<AccountAudit> getAccountAudits(AccountId accountId) throws AccountAuditPersistenceServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            return accountAuditDAO.getAccountAuditsById(connection, accountId);
        } catch (DAOException | SQLException e) {
            throw new AccountAuditPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(connection);
        }
    }
}
