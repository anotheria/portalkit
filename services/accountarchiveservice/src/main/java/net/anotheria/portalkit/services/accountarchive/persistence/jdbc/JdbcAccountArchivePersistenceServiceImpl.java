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
        } catch (SQLException e) {
            throw new ArchivedAccountPersistenceServiceException(e.getMessage(), e);
        } catch (DAOException e) {
            throw new ArchivedAccountPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public ArchivedAccount getAccount(final AccountId id) throws ArchivedAccountPersistenceServiceException {
        return callDao(new SQLConnectionAware<ArchivedAccount>() {
            @Override
            public ArchivedAccount execute(Connection connection) throws SQLException, DAOException {
                return dao.getAccount(connection, id);
            }
        });
    }

    @Override
    public List<ArchivedAccount> getAccounts(final List<AccountId> identities) throws ArchivedAccountPersistenceServiceException {
        return callDao(new SQLConnectionAware<List<ArchivedAccount>>() {
            @Override
            public List<ArchivedAccount> execute(Connection connection) throws SQLException, DAOException {
                return dao.getAccounts(connection, identities);
            }
        });
    }

    @Override
    public List<ArchivedAccount> getAllAccounts() throws ArchivedAccountPersistenceServiceException {
        return callDao(new SQLConnectionAware<List<ArchivedAccount>>() {
            @Override
            public List<ArchivedAccount> execute(Connection connection) throws SQLException, DAOException {
                return dao.getAllAccounts(connection);
            }
        });
    }

    @Override
    public void saveAccount(final ArchivedAccount account) throws ArchivedAccountPersistenceServiceException {
        callDao(new SQLConnectionAware<Void>() {
            @Override
            public Void execute(Connection connection) throws SQLException, DAOException {
                dao.saveAccount(connection, account); return null;
            }
        });
    }

    @Override
    public void deleteAccount(final AccountId id) throws ArchivedAccountPersistenceServiceException {
        callDao(new SQLConnectionAware<Void>() {
            @Override
            public Void execute(Connection connection) throws SQLException, DAOException {
                dao.deleteAccount(connection, id);
                return null;
            }
        });
    }

    @Override
    public AccountId getIdByName(final String name) throws ArchivedAccountPersistenceServiceException {
        return callDao(new SQLConnectionAware<AccountId>() {
            @Override
            public AccountId execute(Connection connection) throws SQLException, DAOException {
                return dao.getIdByName(connection, name);
            }
        });
    }

    @Override
    public String getCustomNoteById(AccountId id) throws ArchivedAccountPersistenceServiceException {
        return callDao(connection -> dao.getCustomNoteById(connection, id));
    }

    @Override
    public void saveCustomNote(AccountId id, String customNote) throws ArchivedAccountPersistenceServiceException {
        callDao(connection -> dao.saveCustomNote(connection, id, customNote));
    }

    @Override
    public AccountId getIdByEmail(final String email) throws ArchivedAccountPersistenceServiceException {
        return callDao(new SQLConnectionAware<AccountId>() {
            @Override
            public AccountId execute(Connection connection) throws SQLException, DAOException {
                return dao.getIdByEmail(connection, email);
            }
        });
    }

    @Override
    public Collection<AccountId> getAllAccountIds() throws ArchivedAccountPersistenceServiceException {
        return callDao(new SQLConnectionAware<Collection<AccountId>>() {
            @Override
            public Collection<AccountId> execute(Connection connection) throws SQLException, DAOException {
                return dao.getAccountIds(connection);
            }
        });
    }

    @Override
    public List<AccountId> getAccountsByType(final int type) throws ArchivedAccountPersistenceServiceException {
        return callDao(new SQLConnectionAware<List<AccountId>>() {
            @Override
            public List<AccountId> execute(Connection connection) throws SQLException, DAOException {
                return dao.getAccountIdsByType(connection, type);
            }
        });
    }

    @Override
    public List<ArchivedAccount> getAccountsByQuery(final ArchivedAccountQuery query) throws ArchivedAccountPersistenceServiceException {
        return callDao(new SQLConnectionAware<List<ArchivedAccount>>() {
            @Override
            public List<ArchivedAccount> execute(Connection connection) throws SQLException, DAOException {
                return dao.getAccountsByQuery(connection, query);
            }
        });
    }
}
