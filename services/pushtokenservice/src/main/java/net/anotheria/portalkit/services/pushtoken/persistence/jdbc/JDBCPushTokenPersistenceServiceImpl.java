package net.anotheria.portalkit.services.pushtoken.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceService;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC implementation of PushTokenPersistenceService.
 */
public class JDBCPushTokenPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements PushTokenPersistenceService {

    /**
     * {@link PushTokenDAO} instance.
     */
    private PushTokenDAO dao;

    public JDBCPushTokenPersistenceServiceImpl() {
        super("pk-jdbc-pushtoken");

        dao = new PushTokenDAO();
        addDaos(dao);
    }

    @Override
    public List<String> getAllTokensByAccountId(AccountId accountId) throws PushTokenPersistenceServiceException {
        Connection conn = null;
        try {
            conn = getConnection();
            return dao.getAllByAccountId(conn, accountId);
        } catch (DAOException | SQLException e) {
            throw new PushTokenPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn);
        }
    }

    @Override
    public String saveTokenForAccount(AccountId accountId, String token) throws PushTokenPersistenceServiceException {
        Connection conn = null;
        try {
            conn = getConnection();
            dao.saveForAccount(conn, accountId, token);
            return token;
        } catch (DAOException | SQLException e) {
            throw new PushTokenPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn);
        }
    }

    @Override
    public AccountId deleteToken(String token) throws PushTokenPersistenceServiceException {
        Connection conn = null;
        try {
            conn = getConnection();
            String accountId = dao.deleteToken(conn, token);
            return accountId == null ? null : new AccountId(accountId);
        } catch (DAOException | SQLException e) {
            throw new PushTokenPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn);
        }
    }
}
