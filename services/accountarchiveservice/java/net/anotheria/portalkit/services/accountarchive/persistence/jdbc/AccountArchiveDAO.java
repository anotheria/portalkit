package net.anotheria.portalkit.services.accountarchive.persistence.jdbc;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.anotheria.portalkit.services.accountarchive.ArchivedAccount;
import net.anotheria.portalkit.services.accountarchive.ArchivedAccountQuery;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author VKoulakov
 * @since 22.04.14 11:59
 */
public class AccountArchiveDAO extends AbstractDAO implements DAO {
    public static final String TABLE_NAME = "account_archive";
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountArchiveDAO.class);

    @Override
    protected String[] getTableNames() {
        return new String[]{TABLE_NAME};
    }

    public ArchivedAccount getAccount(Connection connection, AccountId id) throws DAOException, SQLException {
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = connection.prepareStatement("SELECT id, name, email, type, regts, status, tenant, deleted_at, deleted_note from " + TABLE_NAME + " WHERE id = ?;");
            stat.setString(1, id.getInternalId());
            result = stat.executeQuery();
            if (!result.next()) {
                return null;
            }

            return mapArchivedAccount(id, result);
        } finally {
            JDBCUtil.close(result);
            JDBCUtil.close(stat);
        }
    }

    private ArchivedAccount mapArchivedAccount(final AccountId id, final ResultSet result) throws SQLException {
        ArchivedAccount acc = new ArchivedAccount(id);
        acc.setName(result.getString("name"));
        acc.setEmail(result.getString("email"));
        acc.setRegistrationTimestamp(result.getLong("regts"));
        acc.setType(result.getInt("type"));
        acc.setStatus(result.getLong("status"));
        acc.setTenant(result.getString("tenant"));
        acc.setDeletionTimestamp(result.getLong("deleted_at"));
        acc.setDeletionNote(result.getString("deleted_note"));
        return acc;
    }

    public List<ArchivedAccount> getAccounts(Connection connection, List<AccountId> accountIdList) throws SQLException {
        if (accountIdList == null || accountIdList.isEmpty()) {
            return Collections.emptyList();
        }
        PreparedStatement query = null;
        ResultSet resultSet = null;
        List<ArchivedAccount> result = new ArrayList<ArchivedAccount>();
        try {
            List<String> transform = Lists.transform(accountIdList, new Function<AccountId, String>() {
                @Override
                public String apply(AccountId input) {
                    return input.getInternalId();
                }
            });
            String[] identities = transform.toArray(new String[transform.size()]);
            StringBuilder sb = new StringBuilder("SELECT id, name, email, type, regts, status, tenant, deleted_at, deleted_note from " + TABLE_NAME + " WHERE id IN (");
            int listSize = identities.length;
            for(int i = 0; i < listSize; i++){
                sb.append("'").append(identities[i]).append("'");
                if (i < listSize - 1) sb.append(",");
            }
            sb.append(")");
            query = connection.prepareStatement(sb.toString());
//            query.setArray(1, connection.createArrayOf("varchar", identities));
            resultSet = query.executeQuery();
            while (resultSet.next()) {
                result.add(mapArchivedAccount(new AccountId(resultSet.getString("id")), resultSet));
            }
        } finally {
            JDBCUtil.close(resultSet);
            JDBCUtil.close(query);
        }
        return result;
    }

    public List<ArchivedAccount> getAllAccounts(Connection connection) throws SQLException{
        PreparedStatement query = null;
        ResultSet resultSet = null;
        List<ArchivedAccount> result = new ArrayList<ArchivedAccount>();
        try{
            query = connection.prepareStatement("SELECT id, name, email, type, regts, status, tenant, deleted_at, deleted_note from " + TABLE_NAME);
            resultSet = query.executeQuery();
            while (resultSet.next()){
                result.add(mapArchivedAccount(new AccountId(resultSet.getString("id")), resultSet));
            }
        }finally {
            JDBCUtil.close(resultSet);
            JDBCUtil.close(query);
        }
        return result;
    }

    protected boolean createAccount(Connection connection, ArchivedAccount account) throws SQLException {
        try {
            ArchivedAccount found = getAccount(connection, account.getId());
            if (found != null) {
                return false;
            }
        } catch (DAOException ignored) {
        }

        String insert = "INSERT INTO " + TABLE_NAME + "(id, name, email, type, regts, status, tenant, " + ATT_DAO_CREATED + "," + ATT_DAO_UPDATED + ",deleted_at,deleted_note) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement query = connection.prepareStatement(insert);
        query.setString(1, account.getId().getInternalId());
        query.setString(2, account.getName());
        query.setString(3, account.getEmail());
        query.setInt(4, account.getType());
        query.setLong(5, account.getRegistrationTimestamp());
        query.setLong(6, account.getStatus());
        query.setString(7, account.getTenant());
        query.setLong(8, System.currentTimeMillis());
        query.setLong(9, 0);
        query.setLong(10, account.getDeletionTimestamp());
        query.setString(11, account.getDeletionNote());
        return query.executeUpdate() == 1;
    }

    protected boolean updateAccount(Connection connection, ArchivedAccount account) throws SQLException, DAOException {
        String update = "UPDATE " + TABLE_NAME + " set name = ?, email = ?, type = ?, regts = ?, status = ?, tenant = ?, deleted_at = ?, deleted_note = ?, "
                + ATT_DAO_UPDATED + " = ? WHERE id = ?";
        PreparedStatement query = connection.prepareStatement(update);
        query.setString(1, account.getName());
        query.setString(2, account.getEmail());
        query.setInt(3, account.getType());
        query.setLong(4, account.getRegistrationTimestamp());
        query.setLong(5, account.getStatus());
        query.setString(6, account.getTenant());
        query.setLong(7, account.getDeletionTimestamp());
        query.setString(8, account.getDeletionNote());
        query.setLong(9, System.currentTimeMillis());
        query.setString(10, account.getId().getInternalId());
        int updated = query.executeUpdate();
        if (updated > 1) {
            throw new DAOException("There is more than 1 records update by Query: " + query);
        }
        return updated == 1;
    }

    public void saveAccount(Connection connection, ArchivedAccount account) throws SQLException, DAOException {
        if (!updateAccount(connection, account)) {
            createAccount(connection, account);
        }
    }

    public void deleteAccount(Connection connection, AccountId id) throws SQLException, DAOException {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        PreparedStatement delete = connection.prepareStatement(query);
        delete.setString(1, id.getInternalId());
        int affected = delete.executeUpdate();
        if (affected != 1) {
            throw new DAOException("There is more than 1 records update by Query: " + query);
        }
    }

    public AccountId getIdByName(Connection connection, String name) throws SQLException, DAOException {
        return getIdByField(connection, "name", name);
    }

    protected AccountId getIdByField(Connection connection, String fieldName, String fieldValue) throws SQLException, DAOException {
        String sql = "SELECT id FROM " + TABLE_NAME + " WHERE " + fieldName + " = ?";
        PreparedStatement select = null;
        ResultSet result = null;
        try {
            select = connection.prepareStatement(sql);
            select.setString(1, fieldValue);
            result = select.executeQuery();
            if (!result.next())
                return null;
            return new AccountId(result.getString(1));
        } finally {
            JDBCUtil.close(result);
            JDBCUtil.close(select);
        }
    }

    public AccountId getIdByEmail(Connection connection, String email) throws SQLException, DAOException {
        return getIdByField(connection, "email", email);
    }

    public Collection<AccountId> getAccountIds(Connection connection) throws SQLException {
        Collection<AccountId> result = new ArrayList<AccountId>();
        PreparedStatement query = connection.prepareStatement("select id from " + TABLE_NAME);
        ResultSet rs = query.executeQuery();
        try {
            while (rs.next()) {
                result.add(new AccountId(rs.getString(1)));
            }
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(query);
        }
        return result;
    }

    public List<AccountId> getAccountIdsByType(Connection connection, int type) throws SQLException {
        List<AccountId> result = new ArrayList<AccountId>();
        String sql = "SELECT id FROM " + TABLE_NAME + " WHERE type = ?";
        PreparedStatement select = null;
        ResultSet rs = null;
        try {
            select = connection.prepareStatement(sql);
            select.setInt(1, type);
            rs = select.executeQuery();
            while (rs.next()) {
                result.add(new AccountId(rs.getString("id")));
            }
            return result;
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(select);
        }
    }

    public List<ArchivedAccount> getAccountsByQuery(Connection connection, ArchivedAccountQuery query) throws SQLException {
        final String sqlSelectPart = "SELECT id, name, email, type, status, regts, deleted_at, deleted_note FROM " + TABLE_NAME;
        final String sqlWherePart = " WHERE 1=1";
        final String sqlOrderPart = " ORDER BY regts DESC";
        // general selection part
        final StringBuilder sqlRawQuery = new StringBuilder(sqlSelectPart);
        // filtering part
        sqlRawQuery.append(sqlWherePart);
        if (query.getRegisteredFrom() != null)
            sqlRawQuery.append(" AND regts >= ").append(query.getRegisteredFrom());
        if (query.getRegisteredTill() != null)
            sqlRawQuery.append(" AND regts <= ").append(query.getRegisteredTill());
        if (!query.getIds().isEmpty()) {
            final String ids = StringUtils.concatenateTokens(query.getIds(), ',', '\'', '\'');
            sqlRawQuery.append(" AND id IN (").append(ids).append(")");
        }
        if (!StringUtils.isEmpty(query.getEmailMask()))
            sqlRawQuery.append(" AND email LIKE '").append(query.getEmailMask()).append("'");
        if (!StringUtils.isEmpty(query.getNameMask()))
            sqlRawQuery.append(" AND name LIKE '").append(query.getNameMask()).append("'");
        if (!StringUtils.isEmpty(query.getIdMask()))
            sqlRawQuery.append(" AND id LIKE '").append(query.getIdMask()).append("'");
        if (!query.getTypesIncluded().isEmpty()) {
            final String types = StringUtils.concatenateTokens(query.getTypesIncluded(), ",");
            sqlRawQuery.append(" AND type IN (").append(types).append(")");
        }
        if (!query.getTypesExcluded().isEmpty()) {
            final String types = StringUtils.concatenateTokens(query.getTypesExcluded(), ",");
            sqlRawQuery.append(" AND type NOT IN (").append(types).append(")");
        }

        if (query.getDeletedFrom() != null) {
            sqlRawQuery.append(" AND deleted_at >= ").append(query.getDeletedFrom());
        }

        if (query.getDeletedTill() != null) {
            sqlRawQuery.append(" AND deleted_at <= ").append(query.getDeletedTill());
        }
        // ordering part
        sqlRawQuery.append(sqlOrderPart);

        final String sqlQuery = sqlRawQuery.toString();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getAccountsByQuery(Connection, AccountQuery) executing with query[" + sqlQuery + "].");

        final List<ArchivedAccount> rawResult = new ArrayList<ArchivedAccount>();
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sqlQuery);
            while (rs.next()) {
                ArchivedAccount account = new ArchivedAccount(new AccountId(rs.getString("id")));
                account.setName(rs.getString("name"));
                account.setEmail(rs.getString("email"));
                account.setType(rs.getInt("type"));
                account.setStatus(rs.getLong("status"));
                account.setRegistrationTimestamp(rs.getLong("regts"));
                account.setDeletionTimestamp(rs.getLong("deleted_at"));
                account.setDeletionNote(rs.getString("deleted_note"));
                rawResult.add(account);
            }
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(statement);
        }

        if (rawResult.isEmpty()) // no results
            return rawResult;
        if (query.getStatusesIncluded().isEmpty() && query.getStatusesExcluded().isEmpty()) // no filtering by status
            return rawResult;

        final List<ArchivedAccount> result = new ArrayList<ArchivedAccount>();
        // now we should exclude accounts by status field - we can't do this in the query because value stored as long bitmap
        for (final ArchivedAccount account : rawResult) {
            boolean skip = false;
            for (final Long status : query.getStatusesIncluded())
                if (!account.hasStatus(status)) { // skipping account if it doesn't have required status
                    skip = true;
                    break;
                }
            if (skip)
                continue;

            for (final Long status : query.getStatusesExcluded())
                if (account.hasStatus(status)) { // skipping account if it have doesn't required status
                    skip = true;
                    break;
                }
            if (skip)
                continue;

            result.add(account);
        }

        return result;
    }
}
