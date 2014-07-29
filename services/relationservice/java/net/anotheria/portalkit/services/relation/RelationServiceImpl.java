package net.anotheria.portalkit.services.relation;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.dualcrud.CrudServiceException;
import net.anotheria.anoprise.dualcrud.ItemNotFoundException;
import net.anotheria.anoprise.dualcrud.Query;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.persistence.RelationPersistenceService;
import net.anotheria.portalkit.services.relation.persistence.RelationPersistenceServiceException;
import net.anotheria.portalkit.services.relation.storage.Relation;
import net.anotheria.portalkit.services.relation.storage.RelationQueries;
import net.anotheria.portalkit.services.relation.storage.RelationQuery;
import net.anotheria.portalkit.services.relation.storage.UserRelationData;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link RelationService} implementation.
 *
 * @author asamoilich
 */
public class RelationServiceImpl implements RelationService {
    /**
     * Logging util instance.
     */
    private static final Logger LOG = Logger.getLogger(RelationServiceImpl.class);
    /**
     * Lock key delimiter.
     */
    private static final String DELIMITER = "_relation_";
    /**
     * {@link RelationPersistenceService} instance.
     */
    private RelationPersistenceService persistence;
    /**
     * {@link IdBasedLockManager} instance.
     */
    private IdBasedLockManager<CacheKey> lockManager;
    /**
     * Cache of user relations.
     */
    private final Cache<CacheKey, UserRelationData> userRelationDataCache;
    /**
     * Cache for outgoing relations.
     */
    private final Cache<AccountId, List<UserRelationData>> userRelationOutCache;
    /**
     * Cache for incoming relations.
     */
    private final Cache<AccountId, List<UserRelationData>> userRelationInCache;

    /**
     * Constructor.
     */
    public RelationServiceImpl() {
        userRelationDataCache = Caches.createHardwiredCache("user-relation-data");
        userRelationOutCache = Caches.createHardwiredCache("user-out-relations");
        userRelationInCache = Caches.createHardwiredCache("user-in-relations");
        try {
            persistence = MetaFactory.get(RelationPersistenceService.class);
        } catch (MetaFactoryException e) {
            LOG.fatal("RelationPersistenceService init failed", e);
            throw new RuntimeException("RelationPersistenceService init failed", e);
        }
        lockManager = new SafeIdBasedLockManager<CacheKey>();
    }


    @Override
    public UserRelationData addRelation(AccountId ownerId, AccountId partnerId, Relation relation) throws RelationServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        if (partnerId == null)
            throw new IllegalArgumentException("partnerId incoming parameter is not valid");
        if (relation == null)
            throw new IllegalArgumentException("relation incoming parameter is not valid");

        CacheKey cacheKey = new CacheKey(ownerId.getInternalId(), partnerId.getInternalId());
        IdBasedLock<CacheKey> lock = lockManager.obtainLock(cacheKey);
        lock.lock();
        try {
            Relation savedRelation = persistence.addRelation(ownerId, partnerId, relation);
            clearInOutRelationCaches(ownerId, partnerId);
            UserRelationData relationData = userRelationDataCache.get(cacheKey);
            if (relationData == null) {
                relationData = new UserRelationData(ownerId, partnerId, savedRelation);
                userRelationDataCache.put(cacheKey, relationData);
                return relationData;
            }
            relationData.getRelationMap().put(savedRelation.getName(), savedRelation);
            return relationData;
        } catch (RelationPersistenceServiceException ex) {
            throw new RelationServiceException("persistence.addRelation failed.", ex);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public UserRelationData create(UserRelationData toCreate) throws CrudServiceException {
        if (toCreate == null)
            throw new IllegalArgumentException("Incoming relation to create is NULL");
        AccountId ownerId = new AccountId(toCreate.getOwnerId());
        try {
            return addRelation(ownerId, toCreate.getPartner(), toCreate.getRelationToCreate());
        } catch (RelationServiceException e) {
            throw new CrudServiceException("Create failed.", e);
        }
    }

    @Override
    public UserRelationData read(String relationId) throws CrudServiceException, ItemNotFoundException {
        if (relationId == null || relationId.isEmpty() || !relationId.contains(DELIMITER))
            throw new IllegalArgumentException("Invalid incoming parameter");
        String ownerId = relationId.substring(0, relationId.indexOf(DELIMITER));
        String partnerId = relationId.substring(relationId.indexOf(DELIMITER) + DELIMITER.length(), relationId.length());
        try {
            return getRelationData(new AccountId(ownerId), new AccountId(partnerId));
        } catch (RelationServiceException e) {
            throw new CrudServiceException("Read failed", e);
        }
    }

    @Override
    public UserRelationData update(UserRelationData toUpdate) throws CrudServiceException {
        if (toUpdate == null)
            throw new IllegalArgumentException("Incoming relation to create is NULL");
        AccountId ownerId = new AccountId(toUpdate.getOwnerId());
        try {
            return addRelation(ownerId, toUpdate.getPartner(), toUpdate.getRelationToCreate());
        } catch (RelationServiceException e) {
            throw new CrudServiceException("Update failed.", e);
        }
    }

    @Override
    public void delete(UserRelationData toDelete) throws CrudServiceException {
        if (toDelete == null)
            throw new IllegalArgumentException("Incoming relation to create is NULL");
        AccountId ownerId = new AccountId(toDelete.getOwnerId());

        try {
            removeRelation(ownerId, toDelete.getPartner(), toDelete.getRelationToCreate().getName());
        } catch (RelationServiceException e) {
            throw new CrudServiceException("Delete failed", e);
        }
    }

    @Override
    public UserRelationData save(UserRelationData toSave) throws CrudServiceException {
        if (toSave == null)
            throw new IllegalArgumentException("Incoming relation to create is NULL");
        AccountId ownerId = new AccountId(toSave.getOwnerId());
        try {
            return addRelation(ownerId, toSave.getPartner(), toSave.getRelationToCreate());
        } catch (RelationServiceException e) {
            throw new CrudServiceException("Save failed.", e);
        }
    }

    @Override
    public boolean exists(UserRelationData relation) throws CrudServiceException {
        return false;
    }

    @Override
    public List<UserRelationData> query(Query q) throws CrudServiceException {
        RelationQuery relationQuery = RelationQuery.class.cast(q);
        RelationQueries queries = RelationQueries.getByName(relationQuery.getName());
        try {
            switch (queries) {
                case IN:
                    return getInRelationsData(relationQuery.getAccountId());
                case OUT:
                    return getOutRelationsData(relationQuery.getAccountId());
            }

        } catch (RelationServiceException e) {
            throw new CrudServiceException("Query failed.", e);
        }

        return new ArrayList<UserRelationData>();
    }

    @Override
    public UserRelationData getRelationData(AccountId ownerId, AccountId partnerId) throws RelationServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        if (partnerId == null)
            throw new IllegalArgumentException("partnerId incoming parameter is not valid");

        CacheKey cacheKey = new CacheKey(ownerId.getInternalId(), partnerId.getInternalId());
        IdBasedLock<CacheKey> lock = lockManager.obtainLock(cacheKey);
        lock.lock();
        try {
            UserRelationData relationData = userRelationDataCache.get(cacheKey);
            if (relationData == null) {
                List<Relation> relations = persistence.getRelations(ownerId, partnerId);
                relationData = new UserRelationData(ownerId, partnerId);
                for (Relation relation : relations)
                    relationData.getRelationMap().put(relation.getName(), relation);
                userRelationDataCache.put(cacheKey, relationData);
                return relationData;
            }
            return relationData;
        } catch (RelationPersistenceServiceException ex) {
            throw new RelationServiceException("persistence.getRelations failed.", ex);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public UserRelationData removeRelation(AccountId ownerId, AccountId partnerId, String relationName) throws RelationServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        if (partnerId == null)
            throw new IllegalArgumentException("partnerId incoming parameter is not valid");
        if (relationName == null || relationName.isEmpty())
            throw new IllegalArgumentException("relationName incoming parameter is not valid");
        CacheKey cacheKey = new CacheKey(ownerId.getInternalId(), partnerId.getInternalId());
        IdBasedLock<CacheKey> lock = lockManager.obtainLock(cacheKey);
        lock.lock();
        try {
            UserRelationData userRelationData = getRelationData(ownerId, partnerId);
            Relation relation = userRelationData.getRelationMap().get(relationName);
            if (relation == null)
                return userRelationData;
            userRelationData.getRelationMap().remove(relationName);
            persistence.removeRelation(ownerId, partnerId, relationName);
            userRelationDataCache.put(cacheKey, userRelationData);
            clearInOutRelationCaches(ownerId, partnerId);
            return userRelationData;
        } catch (RelationPersistenceServiceException ex) {
            throw new RelationServiceException("persistence.removeRelation failed.", ex);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Clear in and out relations.
     *
     * @param owner   {@link AccountId} of owner
     * @param partner {@link AccountId} of partner
     */
    private void clearInOutRelationCaches(AccountId owner, AccountId partner) {
        userRelationInCache.remove(partner);
        userRelationOutCache.remove(owner);
    }

    @Override
    public List<UserRelationData> getOutRelationsData(AccountId ownerId) throws RelationServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        List<UserRelationData> cachedData = userRelationOutCache.get(ownerId);
        if (cachedData != null)
            return cachedData;
        try {
            List<UserRelationData> outRelations = persistence.getOutRelations(ownerId);
            userRelationOutCache.put(ownerId, outRelations);
            return outRelations;
        } catch (RelationPersistenceServiceException ex) {
            throw new RelationServiceException("persistence.getOutRelations failed.", ex);
        }
    }

    @Override
    public List<UserRelationData> getInRelationsData(AccountId ownerId) throws RelationServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        List<UserRelationData> cachedData = userRelationInCache.get(ownerId);
        if (cachedData != null)
            return cachedData;
        try {
            List<UserRelationData> outRelations = persistence.getInRelations(ownerId);
            userRelationInCache.put(ownerId, outRelations);
            return outRelations;
        } catch (RelationPersistenceServiceException ex) {
            throw new RelationServiceException("persistence.getInRelations failed.", ex);
        }
    }

    @Override
    public int getRelationCount(AccountId ownerId, String relationName) throws RelationServiceException {
        if (ownerId == null)
            throw new IllegalArgumentException("ownerId incoming parameter is not valid");
        int count = 0;
        List<UserRelationData> relations = getOutRelationsData(ownerId);
        for (UserRelationData relationData : relations) {
            for (Map.Entry<String, Relation> entry : relationData.getRelationMap().entrySet()) {
                if (entry.getValue().getName().equals(relationName))
                    count++;
            }
        }
        return count;
    }

    /**
     * Cache key representation.
     */
    private static class CacheKey {

        /**
         * The owner id.
         */
        private String ownerId;
        /**
         * Partner id.
         */
        private String partnerId;

        public CacheKey(String ownerId, String partnerId) {
            if (ownerId == null || ownerId.length() == 0)
                throw new IllegalArgumentException("OwnerId can't be null");
            if (partnerId == null || partnerId.length() == 0)
                throw new IllegalArgumentException("PartnerId can't be null");
            this.ownerId = ownerId;
            this.partnerId = partnerId;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof CacheKey && ownerId.equals(((CacheKey) o).ownerId) && partnerId.equals(((CacheKey) o).partnerId);
        }

        @Override
        public int hashCode() {
            int result = ownerId != null ? ownerId.hashCode() : 0;
            result = 31 * result + (partnerId != null ? partnerId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "CacheKey{" +
                    "ownerId='" + ownerId + '\'' +
                    ", partnerId='" + partnerId + '\'' +
                    '}';
        }
    }
}
