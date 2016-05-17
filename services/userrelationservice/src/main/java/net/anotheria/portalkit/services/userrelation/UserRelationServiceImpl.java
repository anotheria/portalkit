package net.anotheria.portalkit.services.userrelation;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationAlreadyExistsException;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationNotFoundException;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationServiceException;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.anotheria.portalkit.services.userrelation.UserRelationEntity.*;

/**
 * @author bvanchuhov
 */
@Service
@Transactional
@Monitor(subsystem = "userrelation", category = "portalkit-service")
public class UserRelationServiceImpl implements UserRelationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRelationServiceImpl.class);

    private static final String PARAM_OWNER_ID = "ownerId";
    private static final String PARAM_TARGET_ID = "targetId";
    private static final String PARAM_RELATION_NAME = "relationName";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * (owner, target, relationName) -> isRelated cache.
     */
    private Cache<String, Boolean> isRelatedCache;

    public UserRelationServiceImpl() {
        isRelatedCache = Caches.createConfigurableHardwiredCache("userrelationservice-cache");
    }

    private static String getRelatedCacheKey(UserRelation userRelation) {
        return getRelatedCacheKey(userRelation.getOwner(), userRelation.getTarget(), userRelation.getRelationName());
    }

    private static String getRelatedCacheKey(AccountId owner, AccountId target, String relationName) {
        return owner + "|" + target + "|" + relationName;
    }

    private static UserRelationId getUserRelationId(UserRelation userRelation) {
        return new UserRelationId(userRelation.getOwner(), userRelation.getTarget(), userRelation.getRelationName());
    }

    private static UserRelationId getUserRelationId(UserRelationEntity userRelationEntity) {
        return new UserRelationId(userRelationEntity.getOwnerId(), userRelationEntity.getTargetId(), userRelationEntity.getRelationName());
    }

    private static UserRelationEntity relationBO2relationEntity(UserRelation userRelation) {
        return new UserRelationEntity(userRelation);
    }

    private static UserRelation relationEntity2relationBO(UserRelationEntity userRelationEntity) {
        return userRelationEntity.toUserRelation();
    }

    private static Set<UserRelation> relationEntities2relationBOs(Collection<UserRelationEntity> userRelationEntities) {
        Set<UserRelation> results = new HashSet<>();

        for (UserRelationEntity userRelationEntity : userRelationEntities) {
            UserRelation userRelation = relationEntity2relationBO(userRelationEntity);
            results.add(userRelation);
        }

        return results;
    }

    @Override
    public void addRelation(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException {
        long currentTime = System.currentTimeMillis();

        UserRelation userRelation = new UserRelation(owner, target, relationName);
        userRelation.setUpdated(currentTime);
        userRelation.setCreated(currentTime);

        addRelation(userRelation);
    }

    @Override
    public void addRelation(UserRelation userRelation) throws UserRelationServiceException {
        checkRelationNotExists(userRelation.getOwner(), userRelation.getTarget(), userRelation.getRelationName());

        UserRelationEntity userRelationEntity = relationBO2relationEntity(userRelation);
        entityManager.persist(userRelationEntity);

        isRelatedCache.put(getRelatedCacheKey(userRelation), true);
    }

    private void checkRelationNotExists(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException {
        if (isRelated(owner, target, relationName)) {
            throw new UserRelationAlreadyExistsException(owner, target, relationName);
        }
    }

    @Override
    public UserRelation getRelation(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");
        Args.notEmpty(relationName, "relation name");

        UserRelationId userRelationId = new UserRelationId(owner, target, relationName);
        UserRelationEntity userRelationEntity = entityManager.find(UserRelationEntity.class, userRelationId);
        if (userRelationEntity == null) {
            throw new UserRelationNotFoundException(owner, target, relationName);
        }

        return relationEntity2relationBO(userRelationEntity);
    }

    @Override
    public boolean isRelated(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");
        Args.notEmpty(relationName, "relation name");

        String cacheKey = getRelatedCacheKey(owner, target, relationName);
        Boolean cacheValue = isRelatedCache.get(cacheKey);
        if (cacheValue != null) {
            return cacheValue;
        }

        UserRelationId userRelationId = new UserRelationId(owner, target, relationName);
        boolean persistenceValue = isRelationExistsInternally(userRelationId);

        isRelatedCache.put(cacheKey, persistenceValue);

        return persistenceValue;
    }

    private boolean isRelationExistsInternally(UserRelationId userRelationId) {
        UserRelationEntity userRelationEntity = entityManager.find(UserRelationEntity.class, userRelationId);

        return userRelationEntity != null;
    }

    @Override
    public Set<UserRelation> getOwnerRelations(AccountId owner) throws UserRelationServiceException {
        Args.notNull(owner, "owner id");

        List<UserRelationEntity> userRelationEntities = entityManager.createNamedQuery(JPQL_GET_BY_OWNER, UserRelationEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .getResultList();

        return relationEntities2relationBOs(userRelationEntities);
    }

    @Override
    public Set<UserRelation> getOwnerRelations(AccountId owner, String relationName) throws UserRelationServiceException {
        Args.notNull(owner, "owner id");
        Args.notEmpty(relationName, "relation name");

        List<UserRelationEntity> userRelationEntities = entityManager.createNamedQuery(JPQL_GET_BY_OWNER_TYPE, UserRelationEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_RELATION_NAME, relationName)
                .getResultList();

        return relationEntities2relationBOs(userRelationEntities);
    }

    @Override
    public Set<UserRelation> getTargetRelations(AccountId target) throws UserRelationServiceException {
        Args.notNull(target, "target id");

        List<UserRelationEntity> userRelationEntities = entityManager.createNamedQuery(JPQL_GET_BY_TARGET, UserRelationEntity.class)
                .setParameter(PARAM_TARGET_ID, target.getInternalId())
                .getResultList();

        return relationEntities2relationBOs(userRelationEntities);
    }

    @Override
    public Set<UserRelation> getTargetRelations(AccountId target, String relationName) throws UserRelationServiceException {
        Args.notNull(target, "target id");
        Args.notEmpty(relationName, "relation name");

        List<UserRelationEntity> userRelationEntities = entityManager.createNamedQuery(JPQL_GET_BY_TARGET_TYPE, UserRelationEntity.class)
                .setParameter(PARAM_TARGET_ID, target.getInternalId())
                .setParameter(PARAM_RELATION_NAME, relationName)
                .getResultList();

        return relationEntities2relationBOs(userRelationEntities);
    }

    @Override
    public void deleteRelation(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");
        Args.notEmpty(relationName, "relation name");

        UserRelationId userRelationId = new UserRelationId(owner, target, relationName);
        UserRelationEntity userRelationEntity = entityManager.find(UserRelationEntity.class, userRelationId);
        if (userRelationEntity == null) {
            throw new UserRelationNotFoundException(owner, target, relationName);
        }

        entityManager.remove(userRelationEntity);
    }

    @Override
    public void deleteRelations(AccountId owner, AccountId target) throws UserRelationServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_OWNER_TARGET)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TARGET_ID, target.getInternalId())
                .executeUpdate();

        LOGGER.info("Deleted {} user relations for owner={}, target={}", deletedCount, owner, target);
    }

    @Override
    public void deleteOwnerRelations(AccountId owner) throws UserRelationServiceException {
        Args.notNull(owner, "owner id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_OWNER)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .executeUpdate();

        LOGGER.info("Deleted {} user relations for owner={}", deletedCount, owner);

    }

    @Override
    public void deleteTargetRelations(AccountId target) throws UserRelationServiceException {
        Args.notNull(target, "target id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_TARGET)
                .setParameter(PARAM_TARGET_ID, target.getInternalId())
                .executeUpdate();

        LOGGER.info("Deleted {} user relations for target={}", deletedCount, target);
    }
}
