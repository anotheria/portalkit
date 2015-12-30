package net.anotheria.portalkit.services.relation.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.portalkit.services.relation.persistence.RelationPersistenceService;
import net.anotheria.portalkit.services.relation.persistence.RelationPersistenceServiceException;
import net.anotheria.portalkit.services.relation.storage.Relation;
import net.anotheria.portalkit.services.relation.storage.UserRelationData;
import net.anotheria.util.log.LogMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link RelationPersistenceService} implementation.
 *
 * @author asamoilich
 */
public class JDBCRelationPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements RelationPersistenceService {
    /**
     * Logging utility instance.
     */
    private static final Logger LOG = LoggerFactory.getLogger(JDBCRelationPersistenceServiceImpl.class);
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
    private RelationDAO dataAccess;

    /**
     * Constructor.
     */
    JDBCRelationPersistenceServiceImpl() {
        super("pk-jdbc-relation");
        dataAccess = new RelationDAO();
        addDaos(dataAccess);
    }

    @Override
    public Relation addRelation(AccountId ownerId, AccountId partnerId, Relation relation) throws RelationPersistenceServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        if (partnerId == null)
            throw new IllegalArgumentException("partnerId incoming parameter is not valid");
        if (relation == null)
            throw new IllegalArgumentException("relation incoming parameter is not valid");

        Connection con = null;
        try {
            con = getConnection();
            return dataAccess.saveRelation(con, ownerId.getInternalId(), partnerId.getInternalId(), relation);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, ownerId, partnerId, relation);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, ownerId, partnerId, relation);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public List<Relation> getRelations(AccountId ownerId, AccountId partnerId) throws RelationPersistenceServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        if (partnerId == null)
            throw new IllegalArgumentException("partnerId incoming parameter is not valid");
        Connection con = null;
        try {
            con = getConnection();
            return dataAccess.getRelations(con, ownerId.getInternalId(), partnerId.getInternalId());
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, ownerId, partnerId);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, ownerId, partnerId);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public void removeRelation(AccountId ownerId, AccountId partnerId, String relationName) throws RelationPersistenceServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        if (partnerId == null)
            throw new IllegalArgumentException("partnerId incoming parameter is not valid");
        if (relationName == null || relationName.isEmpty())
            throw new IllegalArgumentException("relationName incoming parameter is not valid");
        Connection con = null;
        try {
            con = getConnection();
            dataAccess.removeRelation(con, ownerId.getInternalId(), partnerId.getInternalId(), relationName);
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, ownerId, partnerId);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, ownerId, partnerId);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public List<UserRelationData> getOutRelations(AccountId ownerId) throws RelationPersistenceServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        Connection con = null;
        try {
            con = getConnection();
            Map<AccountId, Map<String, Relation>> outRelations = dataAccess.getOutRelations(con, ownerId.getInternalId());
            List<UserRelationData> result = new ArrayList<UserRelationData>();
            for (Map.Entry<AccountId, Map<String, Relation>> entry : outRelations.entrySet()) {
                UserRelationData relationData = new UserRelationData(ownerId, entry.getKey());
                relationData.getRelationMap().putAll(entry.getValue());
                result.add(relationData);
            }
            return result;
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, ownerId);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, ownerId);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }

    @Override
    public List<UserRelationData> getInRelations(AccountId ownerId) throws RelationPersistenceServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        Connection con = null;
        try {
            con = getConnection();
            Map<AccountId, Map<String, Relation>> outRelations = dataAccess.getInRelations(con, ownerId.getInternalId());
            List<UserRelationData> result = new ArrayList<UserRelationData>();
            for (Map.Entry<AccountId, Map<String, Relation>> entry : outRelations.entrySet()) {
                UserRelationData relationData = new UserRelationData(entry.getKey(), ownerId);
                relationData.getRelationMap().putAll(entry.getValue());
                result.add(relationData);
            }
            return result;
        } catch (DAOException e) {
            final String message = LOG_PREFIX + LogMessageUtil.failMsg(e, ownerId);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(message, e);
        } catch (SQLException e) {
            final String message = LOG_PREFIX_CONNECTION_ERROR + LogMessageUtil.failMsg(e, ownerId);
            LOG.error(message, e);
            throw new RelationPersistenceServiceException(e.getMessage(), e);
        } finally {
            JDBCUtil.close(con);
        }
    }
}
