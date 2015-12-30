package net.anotheria.portalkit.services.relation.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.portalkit.services.relation.persistence.RelationPersistenceService;
import net.anotheria.portalkit.services.relation.storage.Relation;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import net.anotheria.util.log.LogMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * {@link RelationPersistenceService} DAO implementation.
 *
 * @author asamoilich
 */
public class RelationDAO extends AbstractDAO implements DAO {
    /**
     * {@link IdBasedLockManager} instance.
     */
    private IdBasedLockManager<String> lockManager = new SafeIdBasedLockManager<String>();
    /**
     * Logging utility.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RelationDAO.class);
    /**
     * Log prefix.
     */
    private static final String DAO_PREFIX = "Relation-DAO :";

    @Override
    protected String[] getTableNames() {
        return new String[]{RelationPersistenceConstants.TABLE_NAME};
    }

    /**
     * Return relations list for provided owner and partner Ids.
     *
     * @param conn    {@link Connection}
     * @param owner   owner account id
     * @param partner partner account id
     * @return list of relations
     * @throws DAOException on errors
     */
    public List<Relation> getRelations(Connection conn, String owner, String partner) throws DAOException {
        IdBasedLock<String> lock = lockManager.obtainLock(owner);
        lock.lock();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(RelationPersistenceConstants.GET_RELATIONS_BY_OWNER_AND_PARTNER);
            st.setString(1, owner);
            st.setString(2, partner);

            rs = st.executeQuery();
            List<Relation> result = new ArrayList<Relation>();
            while (rs.next()) {
                Relation relation = new Relation(rs.getString(RelationPersistenceConstants.RELATION_NAME_FIELD_NAME));
                relation.setParameter(rs.getString(RelationPersistenceConstants.PARAMETER_FIELD_NAME));
                relation.setCreatedTime(rs.getLong(RelationPersistenceConstants.DAO_CREATED));
                relation.setUpdatedTime(rs.getLong(RelationPersistenceConstants.DAO_UPDATED));
                result.add(relation);
            }
            return result;
        } catch (SQLException e) {
            final String msg = LogMessageUtil.failMsg(e, conn, owner, partner);
            LOG.error(DAO_PREFIX + msg, e);
            throw new DAOException(msg, e);
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
            lock.unlock();
        }
    }

    /**
     * Create or update relation.
     *
     * @param conn     {@link Connection}
     * @param owner    owner account id
     * @param partner  partner account id
     * @param relation {@link Relation}
     * @return created relation
     * @throws DAOException on errors
     */
    public Relation saveRelation(Connection conn, String owner, String partner, Relation relation) throws DAOException {
        IdBasedLock<String> lock = lockManager.obtainLock(owner);
        lock.lock();
        PreparedStatement st = null;
        try {
            //check if record exists...
            Relation oldRelation = getRelation(conn, owner, partner, relation.getName());
            final long currentTime = System.currentTimeMillis();
            if (oldRelation != null) {
                st = conn.prepareStatement(RelationPersistenceConstants.UPDATE_RELATION_SQL);
                st.setString(1, relation.getParameter());
                st.setLong(2, currentTime);
                st.setString(3, owner);
                st.setString(4, partner);
                st.setString(5, relation.getName());
                relation.setUpdatedTime(currentTime);
                relation.setCreatedTime(oldRelation.getCreatedTime());
            } else {
                st = conn.prepareStatement(RelationPersistenceConstants.SQL_CREATE);
                st.setString(1, owner);
                st.setString(2, partner);
                st.setString(3, relation.getName());
                st.setString(4, relation.getParameter());
                st.setLong(5, currentTime);
                st.setLong(6, currentTime);
                relation.setCreatedTime(currentTime);
                relation.setUpdatedTime(currentTime);
            }
            final int result = st.executeUpdate();
            if (result != 1)
                throw new DAOException("saveRelation(conn," + owner + "," + partner + "," + relation.getName() + ") failed.  result returned -> " + result + ", but 1 is expected");

            return relation;
        } catch (SQLException e) {
            final String msg = LogMessageUtil.failMsg(e, conn, owner, partner, relation);
            LOG.error(DAO_PREFIX + msg, e);
            throw new DAOException(msg);
        } finally {
            JDBCUtil.close(st);
            lock.unlock();
        }
    }

    /**
     * Return {@link Relation} if exist or NULL in otherwise.
     *
     * @param con          {@link Connection}
     * @param owner        owner account id
     * @param partner      partner account id
     * @param relationName relation name
     * @return relation
     * @throws DAOException in case of {@link SQLException}
     */
    public Relation getRelation(final Connection con, final String owner, final String partner, final String relationName) throws DAOException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(RelationPersistenceConstants.GET_RELATION);
            //where
            st.setString(1, owner);
            st.setString(2, partner);
            st.setString(3, relationName);

            rs = st.executeQuery();
            if (rs.next()) {
                Relation oldRelation = new Relation(relationName);
                oldRelation.setParameter(rs.getString(RelationPersistenceConstants.PARAMETER_FIELD_NAME));
                oldRelation.setCreatedTime(rs.getLong(RelationPersistenceConstants.DAO_CREATED));
                oldRelation.setUpdatedTime(rs.getLong(RelationPersistenceConstants.DAO_UPDATED));
                return oldRelation;
            }
            return null;

        } catch (SQLException e) {
            final String msg = LogMessageUtil.failMsg(e, con, owner, partner, relationName);
            LOG.error(DAO_PREFIX + msg, e);
            throw new DAOException(msg, e);
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
        }
    }

    /**
     * Remove relation from database.
     *
     * @param conn         {@link Connection}
     * @param owner        owner account id
     * @param partner      partner account id
     * @param relationName relation name
     * @throws DAOException in case of {@link SQLException}
     */
    public void removeRelation(Connection conn, String owner, String partner, String relationName) throws DAOException {
        IdBasedLock<String> lock = lockManager.obtainLock(owner);
        lock.lock();
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(RelationPersistenceConstants.DELETE_ENTRY_SQL);
            st.setString(1, owner);
            st.setString(2, partner);
            st.setString(3, relationName);
            st.executeUpdate();
        } catch (SQLException e) {
            final String msg = LogMessageUtil.failMsg(e, conn, owner, partner, relationName);
            LOG.error(DAO_PREFIX + msg, e);
            throw new DAOException(msg);
        } finally {
            JDBCUtil.close(st);
            lock.unlock();
        }
    }

    /**
     * Return out relations of owner.
     *
     * @param con   {@link Connection}
     * @param owner owner account id
     * @return out relations
     * @throws DAOException on errors
     */
    public Map<AccountId, Map<String, Relation>> getOutRelations(Connection con, String owner) throws DAOException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(RelationPersistenceConstants.GET_OUT_RELATIONS);
            //where
            st.setString(1, owner);
            rs = st.executeQuery();
            Map<AccountId, Map<String, Relation>> result = new HashMap<AccountId, Map<String, Relation>>();
            while (rs.next()) {
                AccountId partnerId = new AccountId(rs.getString(RelationPersistenceConstants.PARTNER_ID_FIELD_NAME));
                Relation relation = new Relation(rs.getString(RelationPersistenceConstants.RELATION_NAME_FIELD_NAME));
                relation.setParameter(rs.getString(RelationPersistenceConstants.PARAMETER_FIELD_NAME));
                relation.setCreatedTime(rs.getLong(RelationPersistenceConstants.DAO_CREATED));
                relation.setUpdatedTime(rs.getLong(RelationPersistenceConstants.DAO_UPDATED));
                Map<String, Relation> relationMap = result.get(partnerId);
                if (relationMap == null) {
                    relationMap = new HashMap<String, Relation>();
                    relationMap.put(relation.getName(), relation);
                    result.put(partnerId, relationMap);
                    continue;
                }
                relationMap.put(relation.getName(), relation);
            }
            return result;

        } catch (SQLException e) {
            final String msg = LogMessageUtil.failMsg(e, con, owner);
            LOG.error(DAO_PREFIX + msg, e);
            throw new DAOException(msg, e);
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
        }
    }

    /**
     * Return incoming relations of owner.
     *
     * @param con   {@link Connection}
     * @param owner owner account id
     * @return incoming relations
     * @throws DAOException on errors
     */
    public Map<AccountId, Map<String, Relation>> getInRelations(Connection con, String owner) throws DAOException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(RelationPersistenceConstants.GET_IN_RELATIONS);
            //where
            st.setString(1, owner);
            rs = st.executeQuery();
            Map<AccountId, Map<String, Relation>> result = new HashMap<AccountId, Map<String, Relation>>();
            while (rs.next()) {
                AccountId partnerId = new AccountId(rs.getString(RelationPersistenceConstants.OWNER_ID_FIELD_NAME));
                Relation relation = new Relation(rs.getString(RelationPersistenceConstants.RELATION_NAME_FIELD_NAME));
                relation.setParameter(rs.getString(RelationPersistenceConstants.PARAMETER_FIELD_NAME));
                relation.setCreatedTime(rs.getLong(RelationPersistenceConstants.DAO_CREATED));
                relation.setUpdatedTime(rs.getLong(RelationPersistenceConstants.DAO_UPDATED));
                Map<String, Relation> relationMap = result.get(partnerId);
                if (relationMap == null) {
                    relationMap = new HashMap<String, Relation>();
                    relationMap.put(relation.getName(), relation);
                    result.put(partnerId, relationMap);
                    continue;
                }
                relationMap.put(relation.getName(), relation);
            }
            return result;

        } catch (SQLException e) {
            final String msg = LogMessageUtil.failMsg(e, con, owner);
            LOG.error(DAO_PREFIX + msg, e);
            throw new DAOException(msg, e);
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(st);
        }
    }

    /**
     * Return DDL meta string array  with all required data for table and indexes creation.
     *
     * @return ddl string.
     */
    public static List<String> getDDL() {
        return Arrays.asList(RelationPersistenceConstants.META_CREATE_TABLE,
                RelationPersistenceConstants.META_OWNER_ID_INDEX,
                RelationPersistenceConstants.META_PARTNER_ID_INDEX,
                RelationPersistenceConstants.META_RELATION_NAME_INDEX);
    }

    /**
     * SQL statements and constants for querying and maintaining current persistence.
     */
    private static final class RelationPersistenceConstants {
        /**
         * Table name constant.
         */
        static final String TABLE_NAME = "relation";
        /**
         * Owner Id db field name.
         */
        static final String OWNER_ID_FIELD_NAME = "ownerId";
        /**
         * Partner Id db field name.
         */
        static final String PARTNER_ID_FIELD_NAME = "partnerId";
        /**
         * Relation name db field name.
         */
        static final String RELATION_NAME_FIELD_NAME = "relationName";
        /**
         * Relation additional parameter db field name.
         */
        static final String PARAMETER_FIELD_NAME = "parameter";
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
         * SQL get entry.
         */
        static final String GET_RELATION = "SELECT * FROM " + TABLE_NAME + SQL_WHERE +
                OWNER_ID_FIELD_NAME + " = ? and " +
                PARTNER_ID_FIELD_NAME + " = ? and " +
                RELATION_NAME_FIELD_NAME + " = ?;";


        /**
         * Table fields constant.
         */
        private static final String TABLE_FIELDS =
                OWNER_ID_FIELD_NAME + FIELD_SEPARATOR +
                        PARTNER_ID_FIELD_NAME + FIELD_SEPARATOR +
                        RELATION_NAME_FIELD_NAME + FIELD_SEPARATOR +
                        PARAMETER_FIELD_NAME + FIELD_SEPARATOR +
                        DAO_CREATED + FIELD_SEPARATOR +
                        DAO_UPDATED;

        /**
         * SQL for user relation entry creation.
         */
        static final String SQL_CREATE = "INSERT INTO " + TABLE_NAME + " (" + TABLE_FIELDS + ") VALUES ( ?, ?, ?, ?, ?, ?);";

        /**
         * Delete entry SQL.
         */
        static final String DELETE_ENTRY_SQL = "DELETE FROM " + TABLE_NAME + SQL_WHERE +
                OWNER_ID_FIELD_NAME + " = ? and " +
                PARTNER_ID_FIELD_NAME + " = ? and " +
                RELATION_NAME_FIELD_NAME + " = ?;";
        /**
         * Update entry SQL.
         */
        static final String UPDATE_RELATION_SQL = "UPDATE " + TABLE_NAME + " SET " +
                PARAMETER_FIELD_NAME + " = ?, " +
                DAO_UPDATED + " = ? " +
                SQL_WHERE + OWNER_ID_FIELD_NAME + " = ? and " +
                PARTNER_ID_FIELD_NAME + " = ? and " +
                RELATION_NAME_FIELD_NAME + " = ?;";
        /**
         * Get relations SQL.
         */
        static final String GET_RELATIONS_BY_OWNER_AND_PARTNER = "SELECT * FROM " + TABLE_NAME +
                SQL_WHERE + OWNER_ID_FIELD_NAME + " = ? and " + PARTNER_ID_FIELD_NAME + " = ?;";
        /**
         * Get out relations SQL.
         */
        static final String GET_OUT_RELATIONS = "SELECT * FROM " + TABLE_NAME + SQL_WHERE + OWNER_ID_FIELD_NAME + " = ?;";
        /**
         * Get in relations SQL.
         */
        static final String GET_IN_RELATIONS = "SELECT * FROM " + TABLE_NAME + SQL_WHERE + PARTNER_ID_FIELD_NAME + " = ?;";

        /**
         * Create table meta.
         */
        static final String META_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                OWNER_ID_FIELD_NAME + " VARCHAR(128) NOT NULL, " +
                PARTNER_ID_FIELD_NAME + " VARCHAR(128) NOT NULL, " +
                RELATION_NAME_FIELD_NAME + " VARCHAR(128) NOT NULL, " +
                PARAMETER_FIELD_NAME + " VARCHAR, " +
                DAO_CREATED + " bigint," +
                DAO_UPDATED + " bigint," +
                "PRIMARY KEY (" + OWNER_ID_FIELD_NAME + "," + PARTNER_ID_FIELD_NAME + "," + RELATION_NAME_FIELD_NAME + ")" +
                ");";
        /**
         * Owner index.
         */
        static final String META_OWNER_ID_INDEX = "CREATE INDEX owner_idx ON " + TABLE_NAME +
                " (" + OWNER_ID_FIELD_NAME + ");";

        /**
         * Partner index.
         */
        static final String META_PARTNER_ID_INDEX = "CREATE INDEX partner_idx ON " + TABLE_NAME +
                " (" + PARTNER_ID_FIELD_NAME + ");";
        /**
         * Relation name index.
         */
        static final String META_RELATION_NAME_INDEX = "CREATE INDEX relation_name_idx ON " + TABLE_NAME +
                " (" + RELATION_NAME_FIELD_NAME + ");";

        /**
         * Constructor.
         */
        private RelationPersistenceConstants() {
            throw new IllegalAccessError("Can't be instantiated");
        }

    }
}
