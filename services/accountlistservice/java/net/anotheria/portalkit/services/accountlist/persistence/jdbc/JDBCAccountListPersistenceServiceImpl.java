package net.anotheria.portalkit.services.accountlist.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import net.anotheria.portalkit.services.accountlist.AccountIdAdditionalInfo;
import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceService;
import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.util.log.LogMessageUtil;
import org.apache.log4j.Logger;

/**
 * JDBC implementation of AccountListPersistence service.
 *
 * @author dagafonov
 */
public class JDBCAccountListPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AccountListPersistenceService {
    /**
     * Log prefix constant.
     */
    private static final String LOG_PREFIX = " JDBC - PERSISTENCE - SERVICE";
    /**
     * Logging utility instance.
     */
    private static final Logger LOGGER = Logger.getLogger(JDBCAccountListPersistenceServiceImpl.class);
    /**
     * DAO definition.
     */
    private AccountListDAO accountlistDAO;

    /**
     * Default constructor.
     */
    public JDBCAccountListPersistenceServiceImpl() {
        super("pk-jdbc-accountlist");
        accountlistDAO = new AccountListDAO();
        addDaos(accountlistDAO);
    }

    @Override
    public List<AccountIdAdditionalInfo> getList(AccountId owner, String listName) throws AccountListPersistenceServiceException {
        Connection conn = null;
        try {
            conn = getConnection();
            return accountlistDAO.getList(conn, owner, listName);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, owner, listName);
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, owner, listName);
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } finally {
            JDBCUtil.close(conn);
        }
    }

    @Override
    public boolean addToList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListPersistenceServiceException {
        Connection conn = null;
        try {
            conn = getConnection();
            return accountlistDAO.addToList(conn, owner, listName, targets);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, owner, listName, targets.size());
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, owner, listName, targets.size());
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } finally {
            JDBCUtil.close(conn);
        }
    }

    @Override
    public boolean removeFromList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListPersistenceServiceException {
        Connection conn = null;
        try {
            conn = getConnection();
            return accountlistDAO.removeFromList(conn, owner, listName, targets);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, owner, listName, targets.size());
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, owner, listName, targets.size());
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } finally {
            JDBCUtil.close(conn);
        }
    }

    @Override
    public List<AccountIdAdditionalInfo> getReverseList(AccountId target, String listName) throws AccountListPersistenceServiceException {
        Connection conn = null;
        try {
            conn = getConnection();
            return accountlistDAO.getReverseList(conn, target, listName);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, target, listName);
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, target, listName);
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } finally {
            JDBCUtil.close(conn);
        }
    }
    
    @Override
    public boolean updateInList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets)
    		throws AccountListPersistenceServiceException {
    	Connection conn = null;
        try {
            conn = getConnection();
            return accountlistDAO.updateInList(conn, owner, listName, targets);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, owner, listName, targets.size());
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, owner, listName, targets.size());
            LOGGER.error(message, e);
            throw new AccountListPersistenceServiceException(message, e);
        } finally {
            JDBCUtil.close(conn);
        }
    }

}
