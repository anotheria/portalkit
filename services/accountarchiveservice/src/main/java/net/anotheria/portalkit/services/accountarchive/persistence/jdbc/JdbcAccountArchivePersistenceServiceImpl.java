package net.anotheria.portalkit.services.accountarchive.persistence.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.accountarchive.ArchivedAccount;
import net.anotheria.portalkit.services.accountarchive.ArchivedAccountQuery;
import net.anotheria.portalkit.services.accountarchive.persistence.AccountArchivePersistenceService;
import net.anotheria.portalkit.services.accountarchive.persistence.ArchivedAccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * {@link AccountArchivePersistenceService} factory for JDBC implementation.
 * @author VKoulakov
 * @since 22.04.14 11:36
 */
@Monitor(subsystem = "portalkit")
public class JdbcAccountArchivePersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AccountArchivePersistenceService {


    private final AccountArchiveDAO dao;

    interface SQLConnectionAware<T>{
        T execute(Connection connection) throws SQLException, DAOException;
    }

    public JdbcAccountArchivePersistenceServiceImpl() {
        super("pk-jdbc-account");
        dao = new AccountArchiveDAO();
        addDaos(dao);
    }

    private <T> T callDao(SQLConnectionAware<T> command) throws ArchivedAccountPersistenceServiceException{
        Connection con = null;
        try {
            con = getConnection();
            return command.execute(con);
        } catch (DAOException | SQLException e) {
            throw new ArchivedAccountPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public ArchivedAccount getAccount(final AccountId id) throws ArchivedAccountPersistenceServiceException {
        return callDao(connection -> dao.getAccount(connection, id));
    }

    @Override
    public List<ArchivedAccount> getAccounts(final List<AccountId> identities) throws ArchivedAccountPersistenceServiceException {
        return callDao(connection -> dao.getAccounts(connection, identities));
    }

    @Override
    public List<ArchivedAccount> getAllAccounts() throws ArchivedAccountPersistenceServiceException {
        return callDao(dao::getAllAccounts);
    }

    @Override
    public void saveAccount(final ArchivedAccount account) throws ArchivedAccountPersistenceServiceException {
        callDao((SQLConnectionAware<Void>) connection -> {
            dao.saveAccount(connection, account); return null;
        });
    }

    @Override
    public void deleteAccount(final AccountId id) throws ArchivedAccountPersistenceServiceException {
        callDao((SQLConnectionAware<Void>) connection -> {
            dao.deleteAccount(connection, id);
            return null;
        });
    }

    @Override
    public AccountId getIdByName(final String name) throws ArchivedAccountPersistenceServiceException {
        return callDao(connection -> dao.getIdByName(connection, name));
    }

    @Override
    public String getCustomNote(AccountId id) throws ArchivedAccountPersistenceServiceException {
        return callDao(connection -> dao.getCustomNote(connection, id));
    }

    @Override
    public void saveCustomNote(AccountId id, String customNote) throws ArchivedAccountPersistenceServiceException {
        callDao(connection -> dao.saveCustomNote(connection, id, customNote));
    }

    @Override
    public AccountId getIdByEmail(final String email) throws ArchivedAccountPersistenceServiceException {
        return callDao(connection -> dao.getIdByEmail(connection, email));
    }

    @Override
    public Collection<AccountId> getAllAccountIds() throws ArchivedAccountPersistenceServiceException {
        return callDao(dao::getAccountIds);
    }

    @Override
    public List<AccountId> getAccountsByType(final int type) throws ArchivedAccountPersistenceServiceException {
        return callDao(connection -> dao.getAccountIdsByType(connection, type));
    }

    @Override
    public List<ArchivedAccount> getAccountsByQuery(final ArchivedAccountQuery query) throws ArchivedAccountPersistenceServiceException {
        return callDao(connection -> dao.getAccountsByQuery(connection, query));
    }
}
