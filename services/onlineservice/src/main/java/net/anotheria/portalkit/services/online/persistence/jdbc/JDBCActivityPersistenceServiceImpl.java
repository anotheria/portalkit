package net.anotheria.portalkit.services.online.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.portalkit.services.online.persistence.ActivityNotFoundInPersistenceServiceException;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceServiceException;
import net.anotheria.util.log.LogMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jdbc based {@link ActivityPersistenceService} implementation.
 *
 * @author h3llka
 */
public class JDBCActivityPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements ActivityPersistenceService {
    /**
     * Logging utility instance.
     */
    private static final Logger LOG = LoggerFactory.getLogger(JDBCActivityPersistenceServiceImpl.class);
    /**
     * Basic Log prefix.
     */
    private static final String LOG_PREFIX = "PERSISTENCE_SERVICE ";
    /**
     * Connection obtain error prefix.
     */
    private static final String LOG_PREFIX_CONNECTION_ERROR = "PERSISTENCE_SERVICE, Connection obtain failed : ";
    /**
     * Activity DAO object.
     */
    private ActivityDAO dataAccess;

    /**
     * Constructor.
     */
    JDBCActivityPersistenceServiceImpl() {
        super("pk-jdbc-activity");
        dataAccess = new ActivityDAO();
        addDaos(dataAccess);
    }

    @Override
    public long saveLastLogin(final AccountId account, final long lastLoginTime) throws ActivityPersistenceServiceException {
        if (account == null)
            throw new IllegalArgumentException("account incoming parameter is not valid");

        Connection con = null;
        try {
            con = getConnection();
            return dataAccess.saveLastLogin(con, account, lastLoginTime);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, account, lastLoginTime);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, account, lastLoginTime);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }

    }

    @Override
    public long saveLastActivity(AccountId account, long lastActivityTime) throws ActivityPersistenceServiceException {
        if (account == null)
            throw new IllegalArgumentException("account incoming parameter is not valid");

        Connection con = null;
        try {
            con = getConnection();
            return dataAccess.saveLastActivity(con, account, lastActivityTime);
        } catch (ActivityEntryNotFoundDAOException e) {
            throw new ActivityNotFoundInPersistenceServiceException(account);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, account, lastActivityTime);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, account, lastActivityTime);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public long readLastLogin(AccountId accountId) throws ActivityPersistenceServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("account incoming parameter is not valid");

        Connection con = null;
        try {
            con = getConnection();
            return dataAccess.readLastLogin(con, accountId);
        } catch (ActivityEntryNotFoundDAOException e) {
            throw new ActivityNotFoundInPersistenceServiceException(accountId);

        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, accountId);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, accountId);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public long readLastActivity(AccountId accountId) throws ActivityPersistenceServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("account incoming parameter is not valid");

        Connection con = null;
        try {
            con = getConnection();
            return dataAccess.readLastActivity(con, accountId);
        } catch (ActivityEntryNotFoundDAOException e) {
            throw new ActivityNotFoundInPersistenceServiceException(accountId);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, accountId);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, accountId);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public Map<AccountId, Long> readLastLogin(List<AccountId> accounts) throws ActivityPersistenceServiceException {
        if (accounts == null)
            throw new IllegalArgumentException("accounts incoming parameter is not valid");

        if (accounts.isEmpty())
            return new HashMap<AccountId, Long>();

        Connection con = null;
        try {
            con = getConnection();
            return dataAccess.readLastLogin(con, accounts);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, accounts.size());
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, accounts.size());
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public Map<AccountId, Long> readLastActivity(List<AccountId> accounts) throws ActivityPersistenceServiceException {
        if (accounts == null)
            throw new IllegalArgumentException("accounts incoming parameter is not valid");

        if (accounts.isEmpty())
            return new HashMap<AccountId, Long>();

        Connection con = null;
        try {
            con = getConnection();
            return dataAccess.readLastActivity(con, accounts);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, accounts.size());
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, accounts.size());
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public void deleteActivityEntry(AccountId accountId) throws ActivityPersistenceServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("account incoming parameter is not valid");

        Connection con = null;
        try {
            con = getConnection();
            dataAccess.removeEntry(con, accountId);
        } catch (ActivityEntryNotFoundDAOException e) {
            throw new ActivityNotFoundInPersistenceServiceException(accountId);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, accountId);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, accountId);
            LOG.error(message, e);
            throw new ActivityPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }
}
