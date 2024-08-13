package net.anotheria.portalkit.services.account.persistence.note.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.account.AccountNote;
import net.anotheria.portalkit.services.account.persistence.note.AccountNotePersistenceService;
import net.anotheria.portalkit.services.account.persistence.note.AccountNotePersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Monitor(category = "portalkit-persistence-service", subsystem = "account-note")
public class JDBCAccountNotePersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AccountNotePersistenceService {

    private AccountNoteDAO accountNoteDAO;

    public JDBCAccountNotePersistenceServiceImpl() {
        super("pk-jdbc-account-note");

        accountNoteDAO = new AccountNoteDAO();
        addDaos(accountNoteDAO);
    }

    @Override
    public void saveAccountNote(AccountNote accountNote) throws AccountNotePersistenceServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            accountNoteDAO.saveAccountNote(connection, accountNote);
        } catch (DAOException | SQLException e) {
            throw new AccountNotePersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(connection);
        }
    }

    @Override
    public List<AccountNote> getNotesByAccountId(AccountId accountId) throws AccountNotePersistenceServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            return accountNoteDAO.getNotesByAccountId(connection, accountId);
        } catch (DAOException | SQLException e) {
            throw new AccountNotePersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(connection);
        }
    }

    @Override
    public AccountNote getAccountNoteById(long id) throws AccountNotePersistenceServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            return accountNoteDAO.getAccountNoteById(connection, id);
        } catch (DAOException | SQLException e) {
            throw new AccountNotePersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(connection);
        }
    }
}
