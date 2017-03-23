package net.anotheria.portalkit.services.relation;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.moskito.aop.annotation.DontMonitor;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.exception.RelationAlreadyExistsException;
import net.anotheria.portalkit.services.relation.exception.RelationNotFoundException;
import net.anotheria.portalkit.services.relation.exception.RelationServiceException;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static net.anotheria.portalkit.services.relation.RelationEntity.*;

/**
 * @author bvanchuhov
 */
@Service
@Transactional
@Monitor(subsystem = "relation", category = "portalkit-service")
public class RelationServiceImpl implements RelationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelationServiceImpl.class);

    private static final String PARAM_OWNER_ID = "ownerId";
    private static final String PARAM_PARTNER_ID = "partnerId";
    private static final String PARAM_RELATION_NAME = "relationName";

    static final Relation NULL_RELATION = new Relation();

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * (owner, partner, relationName) -> Relation cache.
     */
    private Cache<String, Relation> relationCache;

    public RelationServiceImpl() {
        relationCache = Caches.createConfigurableHardwiredCache("pk-cache-relation-service");
    }

    @Override
    public void addRelation(AccountId owner, AccountId partner, String relationName) throws RelationServiceException {
        long currentTime = System.currentTimeMillis();

        Relation relation = new Relation(owner, partner, relationName);
        relation.setUpdated(currentTime);
        relation.setCreated(currentTime);

        addRelation(relation);
    }

    @Override
    public void addRelation(Relation relation) throws RelationServiceException {
        checkRelationNotExists(relation.getOwner(), relation.getPartner(), relation.getRelationName());

        RelationEntity relationEntity = relationBO2relationEntity(relation);
        entityManager.persist(relationEntity);

        relationCache.put(getRelatedCacheKey(relation), relation);
    }

    private void checkRelationNotExists(AccountId owner, AccountId partner, String relationName) throws RelationServiceException {
        if (isRelated(owner, partner, relationName)) {
            throw new RelationAlreadyExistsException(owner, partner, relationName);
        }
    }

    @Override
    public Relation getRelation(AccountId owner, AccountId partner, String relationName) throws RelationServiceException {
        Relation relation = getRelationInternally(owner, partner, relationName);

        if (Objects.equals(relation, NULL_RELATION)) {
            throw new RelationNotFoundException(relation);
        }
        return relation;
    }

    @Nonnull
    private Relation getRelationInternally(AccountId owner, AccountId partner, String relationName) {
        Args.notNull(owner, "owner id");
        Args.notNull(partner, "partner id");
        Args.notEmpty(relationName, "relation name");

        String cacheKey = getRelatedCacheKey(owner, partner, relationName);
        Relation cachedRelation = relationCache.get(cacheKey);
        if (cachedRelation != null) {
            return cachedRelation;
        }

        RelationId relationId = new RelationId(owner, partner, relationName);
        RelationEntity relationEntity = entityManager.find(RelationEntity.class, relationId);
        Relation relation = relationEntity != null ? relationEntity2relationBO(relationEntity) : NULL_RELATION;

        relationCache.put(cacheKey, relation);

        return relation;
    }

    @Override
    public boolean isRelated(AccountId owner, AccountId partner, String relationName) throws RelationServiceException {
        Relation relation = getRelationInternally(owner, partner, relationName);

        return !Objects.equals(relation, NULL_RELATION);
    }

    @Override
    public Set<Relation> getOwnerRelations(AccountId owner) throws RelationServiceException {
        Args.notNull(owner, "owner id");

        List<RelationEntity> userRelationEntities = entityManager.createNamedQuery(JPQL_GET_BY_OWNER, RelationEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .getResultList();

        return relationEntities2relationBOs(userRelationEntities);
    }

    @Override
    public Set<Relation> getOwnerRelations(AccountId owner, String relationName) throws RelationServiceException {
        Args.notNull(owner, "owner id");
        Args.notEmpty(relationName, "relation name");

        List<RelationEntity> userRelationEntities = entityManager.createNamedQuery(JPQL_GET_BY_OWNER_TYPE, RelationEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_RELATION_NAME, relationName)
                .getResultList();

        return relationEntities2relationBOs(userRelationEntities);
    }

    @Override
    public Set<Relation> getPartnerRelations(AccountId partner) throws RelationServiceException {
        Args.notNull(partner, "partner id");

        List<RelationEntity> userRelationEntities = entityManager.createNamedQuery(JPQL_GET_BY_PARTNER, RelationEntity.class)
                .setParameter(PARAM_PARTNER_ID, partner.getInternalId())
                .getResultList();

        return relationEntities2relationBOs(userRelationEntities);
    }

    @Override
    public Set<Relation> getPartnerRelations(AccountId partner, String relationName) throws RelationServiceException {
        Args.notNull(partner, "partner id");
        Args.notEmpty(relationName, "relation name");

        List<RelationEntity> userRelationEntities = entityManager.createNamedQuery(JPQL_GET_BY_PARTNER_TYPE, RelationEntity.class)
                .setParameter(PARAM_PARTNER_ID, partner.getInternalId())
                .setParameter(PARAM_RELATION_NAME, relationName)
                .getResultList();

        return relationEntities2relationBOs(userRelationEntities);
    }

    @Override
    public void deleteRelation(AccountId owner, AccountId partner, String relationName) throws RelationServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(partner, "partner id");
        Args.notEmpty(relationName, "relation name");

        RelationId relationId = new RelationId(owner, partner, relationName);
        RelationEntity relationEntity = entityManager.find(RelationEntity.class, relationId);
        if (relationEntity == null) {
            throw new RelationNotFoundException(owner, partner, relationName);
        }

        entityManager.remove(relationEntity);

        relationCache.put(getRelatedCacheKey(owner, partner, relationName), NULL_RELATION);
    }

    @Override
    public void deleteRelations(AccountId owner, AccountId partner) throws RelationServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(partner, "partner id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_OWNER_PARTNER)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_PARTNER_ID, partner.getInternalId())
                .executeUpdate();

        relationCache.clear();

        LOGGER.info("Deleted {} user relations for owner={}, partner={}", deletedCount, owner, partner);
    }

    @Override
    public void deleteOwnerRelations(AccountId owner) throws RelationServiceException {
        Args.notNull(owner, "owner id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_OWNER)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .executeUpdate();

        relationCache.clear();

        LOGGER.info("Deleted {} user relations for owner={}", deletedCount, owner);
    }

    @Override
    public void deletePartnerRelations(AccountId partner) throws RelationServiceException {
        Args.notNull(partner, "partner id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_PARTNER)
                .setParameter(PARAM_PARTNER_ID, partner.getInternalId())
                .executeUpdate();

        relationCache.clear();

        LOGGER.info("Deleted {} user relations for partner={}", deletedCount, partner);
    }

    @DontMonitor
    private String getRelatedCacheKey(Relation relation) {
        return getRelatedCacheKey(relation.getOwner(), relation.getPartner(), relation.getRelationName());
    }

    @DontMonitor
    private String getRelatedCacheKey(AccountId owner, AccountId partner, String relationName) {
        return owner + "|" + partner + "|" + relationName;
    }

    @DontMonitor
    private RelationId getUserRelationId(Relation relation) {
        return new RelationId(relation.getOwner(), relation.getPartner(), relation.getRelationName());
    }

    @DontMonitor
    private RelationId getUserRelationId(RelationEntity relationEntity) {
        return new RelationId(relationEntity.getOwnerId(), relationEntity.getPartnerId(), relationEntity.getRelationName());
    }

    @DontMonitor
    private RelationEntity relationBO2relationEntity(Relation relation) {
        return new RelationEntity(relation);
    }

    @DontMonitor
    private Relation relationEntity2relationBO(RelationEntity relationEntity) {
        return relationEntity.toUserRelation();
    }

    @DontMonitor
    private Set<Relation> relationEntities2relationBOs(Collection<RelationEntity> userRelationEntities) {
        Set<Relation> results = new HashSet<>();

        for (RelationEntity relationEntity : userRelationEntities) {
            Relation relation = relationEntity2relationBO(relationEntity);
            results.add(relation);
        }

        return results;
    }
}
