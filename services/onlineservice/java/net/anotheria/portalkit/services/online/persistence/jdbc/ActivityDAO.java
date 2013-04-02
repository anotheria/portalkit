package net.anotheria.portalkit.services.online.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.util.StringUtils;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import net.anotheria.util.log.LogMessageUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * {@link net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService} DAO.
 *
 * @author h3llka
 */
public class ActivityDAO extends AbstractDAO implements DAO {
    /**
     * {@link IdBasedLockManager} instance.
     */
    private IdBasedLockManager<AccountId> lockManager = new SafeIdBasedLockManager<AccountId>();

    @Override
    protected String[] getTableNames() {
        return new String[]{ActivityPersistenceConstants.TABLE_NAME};
    }

    /**
     * Execute save last login to DB.
     * In case of create call - new record will be created, otherwise lastLogin & lastActivity properties will be updated.
     *
     * @param con           {@link Connection}
     * @param account       {@link AccountId} as target
     * @param lastLoginTime time to set
     * @return saved time stamp
     * @throws DAOException - in case of {@link SQLException}, or in case when  {@link PreparedStatement#executeUpdate()} result differs from "1"
     */
    public long saveLastLogin(Connection con, AccountId account, long lastLoginTime) throws DAOException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        PreparedStatement st = null;
        try {
            st = createStatementForLastLoginSaveCall(con, account, lastLoginTime);
            final int result = st.executeUpdate();
            if (result != 1)
                throw new DAOException("saveLastLogin(con," + account + "," + lastLoginTime + ") failed.  result returned -> " + result + ", but 1 is expected");

            return lastLoginTime;
        } catch (SQLException sqle) {
            throw new DAOException(LogMessageUtil.failMsg(sqle, con, account, lastLoginTime), sqle);
        } finally {
            JDBCUtil.close(st);
            lock.unlock();
        }
    }

    /**
     * Return {@link PreparedStatement} populated with all required data for Last login save operation.
     *
     * @param con       {@link Connection}
     * @param account   {@link AccountId}
     * @param lastLogin last login property
     * @return {@link PreparedStatement} for further usage
     * @throws DAOException as base DAO exception in case on {@link SQLException}
     */
    private PreparedStatement createStatementForLastLoginSaveCall(Connection con, AccountId account, long lastLogin) throws DAOException {
        try {
            //check if record exists...
            boolean isRecordExists = isExists(con, account);
            //create case
            if (!isRecordExists) {
                PreparedStatement result = con.prepareStatement(ActivityPersistenceConstants.SQL_CREATE);
                //acc id
                result.setString(1, account.getInternalId());
                // last login
                result.setLong(2, lastLogin);
                //last activity
                result.setLong(3, lastLogin);
                //dao created
                result.setLong(4, System.currentTimeMillis());
                //dao updated
                result.setLong(5, 0L);

                return result;
            }
            //update case
            PreparedStatement result = con.prepareStatement(ActivityPersistenceConstants.SQL_UPDATE_LAST_LOGIN);
            //last login
            result.setLong(1, lastLogin);
            //last activity
            result.setLong(2, lastLogin);
            //dao updated
            result.setLong(3, System.currentTimeMillis());


            //where clause
            result.setString(4, account.getInternalId());
            return result;
        } catch (SQLException sqle) {
            throw new DAOException(LogMessageUtil.failMsg(sqle, con, account, lastLogin), sqle);
        }
    }

    /**
     * Update LastActivity time property, both with daoUpdated 'system' property, if possible.
     * In case if there is no such entity found - exception will occur.
     *
     * @param con              {@link Connection}
     * @param account          {@link AccountId}
     * @param lastActivityTime time for update operation
     * @return activity time
     * @throws DAOException in case of {@link SQLException}, {@link ActivityEntryNotFoundDAOException} in case if there is no such data found
     */
    public long saveLastActivity(Connection con, AccountId account, long lastActivityTime) throws DAOException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        PreparedStatement st = null;
        try {
            st = con.prepareStatement(ActivityPersistenceConstants.SQL_UPDATE_LAST_ACTIVITY);
            st.setLong(1, lastActivityTime);
            st.setLong(2, System.currentTimeMillis());
            //where
            st.setString(3, account.getInternalId());

            final int result = st.executeUpdate();
            if (result == 0)
                throw new ActivityEntryNotFoundDAOException(account);

            return lastActivityTime;
        } catch (SQLException e) {
            throw new DAOException(LogMessageUtil.failMsg(e, con, account, lastActivityTime));
        } finally {
            JDBCUtil.close(st);
            lock.unlock();
        }
    }

    /**
     * Read lastLogin for selected {@link AccountId}.
     *
     * @param con       {@link Connection}
     * @param accountId {@link AccountId}
     * @return property value
     * @throws DAOException in case of {@link SQLException}, or {@link ActivityEntryNotFoundDAOException} if there is no such entry found
     */
    public long readLastLogin(Connection con, AccountId accountId) throws DAOException {
        return readProperty(con, accountId, PropertyName.LAST_LOGIN);
    }

    /**
     * Read lastActivity for selected {@link AccountId}.
     *
     * @param con       {@link Connection}
     * @param accountId {@link AccountId}
     * @return property value
     * @throws DAOException in case of {@link SQLException}, or {@link ActivityEntryNotFoundDAOException} if there is no such entry found
     */
    public long readLastActivity(Connection con, AccountId accountId) throws DAOException {
        return readProperty(con, accountId, PropertyName.LAST_ACTIVITY);
    }

    /**
     * Read AccountId to LastLogin mappings, user {@link AccountId} incoming collection for executing IN query.
     *
     * @param con        {@link Connection}
     * @param accountIds {@link AccountId} collection
     * @return AccountId to LastLogin
     * @throws DAOException in case of {@link SQLException}
     */
    public Map<AccountId, Long> readLastLogin(Connection con, List<AccountId> accountIds) throws DAOException {
        return readProperties(con, accountIds, PropertyName.LAST_LOGIN);
    }

    /**
     * Read AccountId to LastActivity mappings, user {@link AccountId} incoming collection for executing IN query.
     *
     * @param con        {@link Connection}
     * @param accountIds {@link AccountId} collection
     * @return AccountId to LastActivity
     * @throws DAOException in case of {@link SQLException}
     */
    public Map<AccountId, Long> readLastActivity(Connection con, List<AccountId> accountIds) throws DAOException {
        return readProperties(con, accountIds, PropertyName.LAST_ACTIVITY);
    }

    /**
     * Remove entry assigned to incoming {@link AccountId}.
     *
     * @param con       {@link Connection}
     * @param accountId {@link AccountId}
     * @throws DAOException on {@link SQLException}
     */
    public void removeEntry(Connection con, AccountId accountId) throws DAOException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        PreparedStatement st = null;
        try {
            st = con.prepareStatement(ActivityPersistenceConstants.DELETE_ENTRY_SQL);
            st.setString(1, accountId.getInternalId());

            st.executeUpdate();

        } catch (SQLException sqle) {
            throw new DAOException(LogMessageUtil.failMsg(sqle, con, accountId), sqle);
        } finally {
            JDBCUtil.close(st);
            lock.unlock();
        }
    }

    /**
     * Return DDL meta sting array  with all required data for  table and indexes creation.
     *
     * @return ddl string.
     */
    public static List<String> getDDL() {
        return Arrays.asList(ActivityPersistenceConstants.META_CREATE_TABLE,
                ActivityPersistenceConstants.META_LAST_LOGIN_INDEX,
                ActivityPersistenceConstants.META_LAST_ACTIVITY_INDEX);
    }

    /**
     * Depending on {@link PropertyName} incoming parameter - executes reading of lastLogin/lastActivity.
     *
     * @param con       {@link Connection}
     * @param accountId {@link AccountId}
     * @param property  {@link PropertyName} to read
     * @return property value
     * @throws DAOException in case of {@link SQLException}, or {@link ActivityEntryNotFoundDAOException} if there is no such entry found
     */
    private long readProperty(final Connection con, final AccountId accountId, final PropertyName property) throws DAOException {
        if (property == null)
            throw new IllegalArgumentException("Undefined property required to be read. PropertyName property parameter is not valid");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(property.getSingleSQLQuery());
            //where
            st.setString(1, accountId.getInternalId());

            rs = st.executeQuery();
            if (rs.next())
                return rs.getLong(property.getResultSetPropertyName());

            throw new ActivityEntryNotFoundDAOException(accountId);
        } catch (SQLException e) {
            throw new DAOException(LogMessageUtil.failMsg(e, con, accountId, property));
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
            lock.unlock();
        }
    }


    /**
     * Depending on {@link PropertyName} incoming parameter - executes reading of lastLogin/lastActivity.
     *
     * @param con      {@link Connection}
     * @param accounts {@link AccountId} collection
     * @param property {@link PropertyName} to read
     * @return AccountId to property value mapping
     * @throws DAOException in case of {@link SQLException}
     */
    private Map<AccountId, Long> readProperties(final Connection con, final List<AccountId> accounts, final PropertyName property) throws DAOException {
        PreparedStatement st = null;
        ResultSet rs = null;
        Map<AccountId, Long> result = new HashMap<AccountId, Long>();
        try {
            List<String> accountList = toStringList(accounts);
            if (accountList == null || accountList.isEmpty())
                return result;
            st = con.prepareStatement(property.getMultipleQuery(accountList));

            rs = st.executeQuery();
            while (rs.next())
                result.put(new AccountId(rs.getString(ActivityPersistenceConstants.ACCOUNT_ID_FIELD_NAME)), rs.getLong(property.resultSetPropertyName));

            return result;
        } catch (SQLException e) {
            throw new DAOException(LogMessageUtil.failMsg(e, con, accounts.size(), property));
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
        }
    }

    /**
     * Map source {@link AccountId} collection to {@link String} collection, which contains only internal ids fro {@link AccountId}.
     * <p/>
     * NOTE :  all not valid Ids  will be  skipped!
     *
     * @param source {@link AccountId}
     * @return collection with internal ids
     */
    private static List<String> toStringList(List<AccountId> source) {
        List<String> result = new ArrayList<String>();
        for (AccountId account : source)
            if (account != null && !StringUtils.isEmpty(account.getInternalId()))
                result.add(account.getInternalId());

        return result;
    }


    /**
     * Return {@code true} if and only if DB record for {@link AccountId} exists.
     *
     * @param con     {@link Connection}
     * @param account {@link AccountId} target account
     * @return boolean flag
     * @throws DAOException in case of {@link SQLException}
     */
    private boolean isExists(final Connection con, final AccountId account) throws DAOException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(ActivityPersistenceConstants.SQL_EXISTS);
            //where
            st.setString(1, account.getInternalId());

            rs = st.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new DAOException(LogMessageUtil.failMsg(e, con, account));
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
        }
    }


    /**
     * Constant with util methods for LastLogin and LastActivity read.
     */
    private static enum PropertyName {
        /**
         * Last login property name.
         */
        LAST_LOGIN(ActivityPersistenceConstants.READ_LAST_LOGIN_SINGLE, ActivityPersistenceConstants.READ_LAST_LOGIN_FOR_IDS, ActivityPersistenceConstants.LAST_LOGIN_FIELD_NAME),
        /**
         * Last property property name.
         */
        LAST_ACTIVITY(ActivityPersistenceConstants.READ_LAST_ACTIVITY_SINGLE, ActivityPersistenceConstants.READ_LAST_ACTIVITY_FOR_IDS, ActivityPersistenceConstants.LAST_ACTIVITY_FIELD_NAME);
        /**
         * Query for single data read.
         */
        private String singleSQLQuery;
        /**
         * Query for multiple data read.
         */
        private String multipleQuery;
        /**
         * Property name in result Set.
         */
        private String resultSetPropertyName;

        PropertyName(String single, String multiple, String propertyName) {
            this.singleSQLQuery = single;
            this.multipleQuery = multiple;
            this.resultSetPropertyName = propertyName;
        }

        public String getSingleSQLQuery() {
            return singleSQLQuery;
        }

        /**
         * Perform query build ( append accountIds collection to the IN - part).
         *
         * @param accounts {@link AccountId} collection for IN part, collection represented by internal ids
         * @return query
         */
        public String getMultipleQuery(List<String> accounts) {
            final String ids = StringUtils.concatenateTokens(accounts, ',', '\'', '\'');
            return String.format(multipleQuery, ids);
        }

        public String getResultSetPropertyName() {
            return resultSetPropertyName;
        }
    }

    /**
     * SQL statements and constants for querying and maintaining current persistence.
     */
    private static final class ActivityPersistenceConstants {
        /**
         * Table name constant.
         */
        static final String TABLE_NAME = "activity";
        /**
         * AccountId db field name.
         */
        static final String ACCOUNT_ID_FIELD_NAME = "accountId";
        /**
         * Last login db field name.
         */
        static final String LAST_LOGIN_FIELD_NAME = "lastLogin";
        /**
         * Last activity db field name.
         */
        static final String LAST_ACTIVITY_FIELD_NAME = "lastActivity";
        /**
         * DAO created 'system' field name.
         */
        static final String DAO_CREATED = "daoCreated";
        /**
         * DAO updated 'system' field name.
         */
        static final String DAO_UPDATED = "daoUpdated";
        /**
         * Separator.
         */
        private static final String FIELD_SEPARATOR = ", ";
        /**
         * SQL WHERE clause.
         */
        private static final String SQL_WHERE = " WHERE ";

        /**
         * Table fields constant.
         */
        private static final String TABLE_FIELDS =
                ACCOUNT_ID_FIELD_NAME + FIELD_SEPARATOR +
                        LAST_LOGIN_FIELD_NAME + FIELD_SEPARATOR +
                        LAST_ACTIVITY_FIELD_NAME + FIELD_SEPARATOR +
                        DAO_CREATED + FIELD_SEPARATOR +
                        DAO_UPDATED;

        /**
         * SQL for activity entry creation.
         * NOTE - updated entry will be skipped.
         */
        static final String SQL_CREATE = "INSERT INTO " + TABLE_NAME + " (" + TABLE_FIELDS + ") VALUES ( ?, ?, ?, ?, ?);";

        /**
         * SQL for LaStLogin update.
         * Note - last activity will be updated also.
         */
        static final String SQL_UPDATE_LAST_LOGIN = "UPDATE " + TABLE_NAME + " SET " +
                LAST_LOGIN_FIELD_NAME + " = ?, " +
                LAST_ACTIVITY_FIELD_NAME + " = ?, " +
                DAO_UPDATED + " = ? " +
                SQL_WHERE + ACCOUNT_ID_FIELD_NAME + " = ?;";

        /**
         * SQL for update.
         */
        static final String SQL_UPDATE_LAST_ACTIVITY = "UPDATE " + TABLE_NAME + " SET " +
                LAST_ACTIVITY_FIELD_NAME + " = ?, " +
                DAO_UPDATED + " = ? " +
                SQL_WHERE + ACCOUNT_ID_FIELD_NAME + " = ?;";

        /**
         * Delete entry SQL.
         */
        static final String DELETE_ENTRY_SQL = "DELETE FROM " + TABLE_NAME + SQL_WHERE + ACCOUNT_ID_FIELD_NAME + " = ?;";
        /**
         * Single last login read.
         */
        static final String READ_LAST_LOGIN_SINGLE = "SELECT " + ACCOUNT_ID_FIELD_NAME + FIELD_SEPARATOR + LAST_LOGIN_FIELD_NAME + " FROM " + TABLE_NAME + SQL_WHERE + ACCOUNT_ID_FIELD_NAME + " = ?;";
        /**
         * Last login login for multiple Accounts.  + ASC sort by last login.
         * NOTE - requires String formatting - for IN query injection!
         */
        static final String READ_LAST_LOGIN_FOR_IDS = "SELECT " + ACCOUNT_ID_FIELD_NAME + FIELD_SEPARATOR + LAST_LOGIN_FIELD_NAME + " FROM " + TABLE_NAME + SQL_WHERE + ACCOUNT_ID_FIELD_NAME + " IN (%s)  ORDER BY "
                + LAST_LOGIN_FIELD_NAME + " ASC;";

        /**
         * Single last activity read.
         */
        static final String READ_LAST_ACTIVITY_SINGLE = "SELECT " + ACCOUNT_ID_FIELD_NAME + FIELD_SEPARATOR + LAST_ACTIVITY_FIELD_NAME + " FROM " + TABLE_NAME + SQL_WHERE + ACCOUNT_ID_FIELD_NAME + " = ?;";
        /**
         * Last login activity for multiple Accounts.  + ASC sort by last activity.
         * NOTE - requires String formatting - for IN query injection!
         */
        static final String READ_LAST_ACTIVITY_FOR_IDS = "SELECT " + ACCOUNT_ID_FIELD_NAME + FIELD_SEPARATOR + LAST_ACTIVITY_FIELD_NAME + " FROM " + TABLE_NAME + SQL_WHERE + ACCOUNT_ID_FIELD_NAME + " IN (%s)  ORDER BY "
                + LAST_ACTIVITY_FIELD_NAME + " ASC;";

        /**
         * SQL is entry exists.
         */
        static final String SQL_EXISTS = "SELECT COUNT(" + ACCOUNT_ID_FIELD_NAME + ") FROM " + TABLE_NAME + SQL_WHERE + ACCOUNT_ID_FIELD_NAME + " = ?;";

        /**
         * Create table meta.
         */
        static final String META_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                ACCOUNT_ID_FIELD_NAME + " VARCHAR(128) NOT NULL, " +
                LAST_LOGIN_FIELD_NAME + " bigint," +
                LAST_ACTIVITY_FIELD_NAME + " bigint," +
                DAO_CREATED + " bigint," +
                DAO_UPDATED + " bigint," +
                "PRIMARY KEY (" + ACCOUNT_ID_FIELD_NAME + ")" +
                ");";
        /**
         * Last login index.
         */
        static final String META_LAST_LOGIN_INDEX = "CREATE INDEX lastLogin_idx ON " + TABLE_NAME +
                " (" + LAST_LOGIN_FIELD_NAME + ");";

        /**
         * Last activity index.
         */
        static final String META_LAST_ACTIVITY_INDEX = "CREATE INDEX lastActivity_idx ON " + TABLE_NAME +
                " (" + LAST_ACTIVITY_FIELD_NAME + ");";

        /**
         * Constructor.
         */
        private ActivityPersistenceConstants() {
            throw new IllegalAccessError("Can't be instantiated");
        }

    }

}
