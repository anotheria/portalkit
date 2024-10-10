package net.anotheria.portalkit.services.account.persistence.note.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.account.AccountNote;
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

@Monitor(category = "DAO", subsystem = "account-note")
public class AccountNoteDAO extends AbstractDAO implements DAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountNoteDAO.class);
    private static final String TABLE_NAME = "account_note";

    @Override
    protected String[] getTableNames() {
        return new String[]{TABLE_NAME};
    }

    public void saveAccountNote(Connection connection, AccountNote accountNote) throws DAOException, SQLException {
        String insert = "INSERT INTO " + TABLE_NAME + " (timestamp, author, text, accountId) VALUES (?,?,?,?)";

        PreparedStatement insertStatement = null;
        try {
            insertStatement = connection.prepareStatement(insert);

            insertStatement.setLong(1, accountNote.getTimestamp());
            insertStatement.setString(2, accountNote.getAuthor());
            insertStatement.setString(3, accountNote.getText());
            insertStatement.setString(4, accountNote.getAccountId().getInternalId());

            insertStatement.executeUpdate();
        } finally {
            JDBCUtil.close(insertStatement);
        }
    }

    public AccountNote updateAccountNote(Connection connection, AccountNote accountNote) throws DAOException, SQLException {
        String update = "UPDATE " + TABLE_NAME + " SET author = ?, text = ? WHERE id = ?";

        PreparedStatement updateStatement = null;
        try {
            updateStatement = connection.prepareStatement(update);

            updateStatement.setString(1, accountNote.getAuthor());
            updateStatement.setString(2, accountNote.getText());
            updateStatement.setLong(3, accountNote.getId());
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DAOException("Update failed, no rows affected.");
            }
            return accountNote;
        } finally {
            JDBCUtil.close(updateStatement);
        }
    }

    public void deleteAccountNote(Connection connection, long id) throws DAOException, SQLException {
        String delete = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        PreparedStatement deleteStatement = null;
        try {
            deleteStatement = connection.prepareStatement(delete);
            deleteStatement.setLong(1, id);
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Delete failed, no rows affected.");
            }
        } finally {
            JDBCUtil.close(deleteStatement);
        }
    }


    public AccountNote getAccountNoteById(Connection connection, long id) throws DAOException, SQLException {
        String selectQuery = "SELECT id, timestamp, author, text, accountId FROM " + TABLE_NAME + " WHERE id=?;";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(selectQuery);
            st.setLong(1, id);
            rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            AccountNote accountNote = new AccountNote();
            accountNote.setId(rs.getLong("id"));
            accountNote.setAccountId(new AccountId(rs.getString("accountId")));
            accountNote.setTimestamp(rs.getLong("timestamp"));
            accountNote.setAuthor(rs.getString("author"));
            accountNote.setText(rs.getString("text"));
            return accountNote;
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
        }
    }

    public List<AccountNote> getNotesByAccountId(Connection connection, AccountId accountId) throws DAOException, SQLException {
        List<AccountNote> result = new ArrayList<>();

        String selectQuery = "SELECT id, accountId, timestamp, author, text FROM " + TABLE_NAME + " WHERE accountId=?;";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(selectQuery);
            st.setString(1, accountId.getInternalId());
            rs = st.executeQuery();

            while (rs.next()) {
                AccountNote accountNote = new AccountNote();
                accountNote.setId(rs.getLong("id"));
                accountNote.setAccountId(new AccountId(rs.getString("accountId")));
                accountNote.setTimestamp(rs.getLong("timestamp"));
                accountNote.setAuthor(rs.getString("author"));
                accountNote.setText(rs.getString("text"));

                result.add(accountNote);
            }
            return result;
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
        }
    }
}
