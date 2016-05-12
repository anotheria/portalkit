package net.anotheria.portalkit.services.match;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.exception.MatchAlreadyExistsException;
import net.anotheria.portalkit.services.match.exception.MatchNotFoundException;
import net.anotheria.portalkit.services.match.exception.MatchServiceException;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bvanchuhov
 */
@Service
@Transactional
@Monitor(subsystem = "match", category = "portalkit-service")
public class MatchServiceImpl implements MatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchServiceImpl.class);

    private static final String PARAM_OWNER_ID = "ownerId";
    private static final String PARAM_TARGET_ID = "targetId";
    private static final String PARAM_TYPE = "type";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * (owner, target, type) -> isMatched cache.
     */
    private Cache<String, Boolean> isMatchedCache;

    public MatchServiceImpl() {
        isMatchedCache = Caches.createConfigurableHardwiredCache("matchservice-cache");
    }

    @Override
    public void addMatch(AccountId owner, AccountId target, int type) throws MatchAlreadyExistsException {
        Match match = new Match(owner, target, type);
        match.setCreated(System.currentTimeMillis());

        addMatch(match);
    }

    @Override
    public void addMatch(Match match) throws MatchAlreadyExistsException {
        checkMatchNotExists(match);

        MatchEntity matchEntity = matchBO2matchEntity(match);
        entityManager.persist(matchEntity);

        isMatchedCache.put(getMatchedCacheKey(match), true);
    }

    private void checkMatchNotExists(Match match) throws MatchAlreadyExistsException {
        if (isMatched(match)) {
            throw new MatchAlreadyExistsException(match);
        }
    }

    @Override
    public Match getMatch(AccountId owner, AccountId target, int type) throws MatchServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        MatchId matchId = new MatchId(owner.getInternalId(), target.getInternalId(), type);
        MatchEntity matchEntity = entityManager.find(MatchEntity.class, matchId);
        if (matchEntity == null) {
            throw new MatchNotFoundException(owner, target, type);
        }

        return matchEntity2matchBO(matchEntity);
    }

    @Override
    public List<Match> getMatches(AccountId owner) {
        Args.notNull(owner, "owner id");

        TypedQuery<MatchEntity> query = entityManager.createNamedQuery(MatchEntity.JPQL_GET_BY_OWNER, MatchEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId());
        List<MatchEntity> matchEntities = query.getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getTargetMatches(AccountId target) throws MatchServiceException {
        Args.notNull(target, "target id");

        TypedQuery<MatchEntity> query = entityManager.createNamedQuery(MatchEntity.JPQL_GET_BY_TARGET, MatchEntity.class)
                .setParameter(PARAM_TARGET_ID, target.getInternalId());
        List<MatchEntity> matchEntities = query.getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getMatchesByType(AccountId owner, int type) {
        Args.notNull(owner, "owner id");

        TypedQuery<MatchEntity> query = entityManager.createNamedQuery(MatchEntity.JPQL_GET_BY_OWNER_TYPE, MatchEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TYPE, type);
        List<MatchEntity> matchEntities = query.getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getTargetMatchesByType(AccountId target, int type) throws MatchServiceException {
        Args.notNull(target, "target id");

        TypedQuery<MatchEntity> query = entityManager.createNamedQuery(MatchEntity.JPQL_GET_BY_TARGET_TYPE, MatchEntity.class)
                .setParameter(PARAM_TARGET_ID, target.getInternalId())
                .setParameter(PARAM_TYPE, type);
        List<MatchEntity> matchEntities = query.getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getLatestMatchesByType(AccountId owner, int type, int limit) {
        Args.notNull(owner, "owner id");
        notNegativeOrZero(limit, "limit");

        TypedQuery<MatchEntity> query = entityManager.createNamedQuery(MatchEntity.JPQL_GET_LATEST_BY_OWNER_TYPE, MatchEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TYPE, type)
                .setMaxResults(limit);
        List<MatchEntity> matchEntities = query.getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getLatestMatches(AccountId owner, int limit) {
        Args.notNull(owner, "owner id");
        notNegativeOrZero(limit, "limit");

        TypedQuery<MatchEntity> query = entityManager.createNamedQuery(MatchEntity.JPQL_GET_LATEST_BY_OWNER, MatchEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setMaxResults(limit);
        List<MatchEntity> matchEntities = query.getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    private int notNegativeOrZero(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " may not be negative or zero");
        }
        return value;
    }

    @Override
    public void deleteMatches(AccountId owner, AccountId target) throws MatchServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        Query query = entityManager.createNamedQuery(MatchEntity.JPQL_DELETE_BY_OWNER_TARGET)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TARGET_ID, target.getInternalId());
        int deletedCount = query.executeUpdate();

        LOGGER.info("Deleted {} matches for owner={}, target={}", deletedCount, owner, target);
    }

    @Override
    public void deleteMatchesByOwner(AccountId owner) {
        Args.notNull(owner, "owner id");

        Query query = entityManager.createNamedQuery(MatchEntity.JPQL_DELETE_BY_OWNER)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId());
        int deletedCount = query.executeUpdate();

        LOGGER.info("Deleted {} matches for owner={}", deletedCount, owner);
    }

    @Override
    public void deleteMatchesByTarget(AccountId target) {
        Args.notNull(target, "target id");

        Query query = entityManager.createNamedQuery(MatchEntity.JPQL_DELETE_BY_TARGET)
                .setParameter(PARAM_TARGET_ID, target.getInternalId());
        int deletedCount = query.executeUpdate();

        LOGGER.info("Deleted {} matches for target={}", deletedCount, target);
    }

    @Override
    public boolean isMatched(AccountId owner, AccountId target, int type) {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        String cacheKey = getMatchedCacheKey(owner, target, type);
        Boolean cacheValue = isMatchedCache.get(cacheKey);
        if (cacheValue != null) {
            return cacheValue;
        }

        Match match = new Match(owner, target, type);
        boolean persistenceValue = isMatchExistsInternally(match);

        isMatchedCache.put(cacheKey, persistenceValue);

        return persistenceValue;
    }

    private String getMatchedCacheKey(AccountId owner, AccountId target, int type) {
        return owner + "|" + target + "|" + type;
    }

    private String getMatchedCacheKey(Match match) {
        return getMatchedCacheKey(match.getOwner(), match.getTarget(), match.getType());
    }

    private boolean isMatchExistsInternally(Match match) {
        MatchEntity existedMatchEntity = getMatchEntityFromPersistence(match);

        return existedMatchEntity != null;
    }

    private MatchEntity getMatchEntityFromPersistence(Match match) {
        MatchId matchId = new MatchId(match.getOwner().getInternalId(), match.getTarget().getInternalId(), match.getType());
        return entityManager.find(MatchEntity.class, matchId);
    }

    private boolean isMatched(Match match) {
        Args.notNull(match, "match id");

        return isMatched(match.getOwner(), match.getTarget(), match.getType());
    }

    private List<Match> matchEntities2matchBOs(List<MatchEntity> matchEntities) {
        List<Match> results = new ArrayList<>(matchEntities.size());

        for (MatchEntity matchEntity : matchEntities) {
            Match match = matchEntity2matchBO(matchEntity);
            results.add(match);
        }

        return results;
    }

    private Match matchEntity2matchBO(MatchEntity matchEntity) {
        return new Match()
                .setOwner(new AccountId(matchEntity.getOwnerId()))
                .setTarget(new AccountId(matchEntity.getTargetId()))
                .setType(matchEntity.getType())
                .setHidden(matchEntity.isHidden())
                .setCreated(matchEntity.getCreated());
    }

    private MatchEntity matchBO2matchEntity(Match match) {
        return new MatchEntity()
                .setOwnerId(match.getOwner().getInternalId())
                .setTargetId(match.getTarget().getInternalId())
                .setType(match.getType())
                .setHidden(match.isHidden())
                .setCreated(match.getCreated());
    }
}
