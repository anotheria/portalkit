package net.anotheria.portalkit.services.match;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.exception.MatchAlreadyExistsException;
import net.anotheria.portalkit.services.match.exception.MatchNotFoundException;
import net.anotheria.portalkit.services.match.exception.MatchServiceException;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static net.anotheria.portalkit.services.match.MatchEntity.*;

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
    /**
     * Data cache. Maps {@link AccountId} to {@link List} of {@link Match} according to ownerId of {@link Match}.
     */
    private Cache<AccountId, List<Match>> ownersCache;
    /**
     * Data cache. Maps {@link AccountId} to {@link List} of {@link Match} according to targetId of {@link Match}.
     */
    private Cache<AccountId, List<Match>> targetsCache;
    /**
     * Lock manager.
     */
    private IdBasedLockManager<AccountId> accountsLockManager = new SafeIdBasedLockManager<AccountId>();


    public MatchServiceImpl() {
        isMatchedCache = Caches.createConfigurableHardwiredCache("pk-cache-match-service");
        ownersCache = Caches.createConfigurableSoftReferenceCache("pk-cache-match-service");
        targetsCache = Caches.createConfigurableSoftReferenceCache("pk-cache-match-service");
    }

    @Override
    public void addMatch(AccountId owner, AccountId target, int type) throws MatchAlreadyExistsException {
        Match match = new Match(owner, target, type);
        match.setCreated(System.currentTimeMillis());

        addMatch(match);
    }

    @Override
    public void addMatch(Match match) throws MatchAlreadyExistsException {
        Args.notNull(match, "match id");

        if (isMatched(match.getOwner(), match.getTarget(), match.getType())) {
            throw new MatchAlreadyExistsException(match);
        }

        MatchEntity matchEntity = matchBO2matchEntity(match);
        entityManager.persist(matchEntity);

        isMatchedCache.put(getMatchedCacheKey(match), true);

        IdBasedLock<AccountId> lock = accountsLockManager.obtainLock(match.getOwner());
        lock.lock();
        try {
            putInCache(ownersCache, match.getOwner(), match, JPQL_GET_BY_OWNER, PARAM_OWNER_ID);
            putInCache(targetsCache, match.getTarget(), match, JPQL_GET_BY_TARGET, PARAM_TARGET_ID);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Match getMatch(AccountId owner, AccountId target, int type) throws MatchServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        IdBasedLock<AccountId> lock = accountsLockManager.obtainLock(owner);
        lock.lock();
        try {

            Match match = null;
            MatchId matchId = new MatchId(owner, target, type);
            List<Match> fromCache = ownersCache.get(owner);

            if (fromCache != null) {
                match = fromCache.stream().filter(x -> x.getTarget().equals(target) && x.getType() == type).findFirst().orElse(null);
            }

            if (match != null) {
                return match;
            }

            MatchEntity matchEntity = entityManager.find(MatchEntity.class, matchId);
            if (matchEntity == null) {
                throw new MatchNotFoundException(owner, target, type);
            }

            match = matchEntity2matchBO(matchEntity);
            putInCache(ownersCache, match.getOwner(), match, JPQL_GET_BY_OWNER, PARAM_OWNER_ID);
            putInCache(targetsCache, match.getTarget(), match, JPQL_GET_BY_TARGET, PARAM_TARGET_ID);
            return match;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Match> getMatches(AccountId owner) {
        return getMatches(owner, ownersCache, JPQL_GET_BY_OWNER, PARAM_OWNER_ID);
    }

    @Override
    public List<Match> getTargetMatches(AccountId target) throws MatchServiceException {
        return getMatches(target, targetsCache, JPQL_GET_BY_TARGET, PARAM_TARGET_ID);
    }


    private List<Match> getMatches(AccountId accountId, Cache<AccountId, List<Match>> cache, String namedQuery, String paramAccount) {
        Args.notNull(accountId, "account id");

        IdBasedLock<AccountId> lock = accountsLockManager.obtainLock(accountId);
        lock.lock();
        try {
            List<Match> matches = cache.get(accountId);
            if (matches != null)
                return matches;

            matches = getMatchesInternally(accountId, namedQuery, paramAccount);
            putInCache(cache, accountId, matches, namedQuery, paramAccount);
            return matches;
        } finally {
            lock.unlock();
        }
    }

    private List<Match> getMatchesInternally(AccountId accountId, String namedQuery, String paramAccount) {
        TypedQuery<MatchEntity> query = entityManager.createNamedQuery(namedQuery, MatchEntity.class)
                .setParameter(paramAccount, accountId.getInternalId());
        List<MatchEntity> matchEntities = query.getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getMatchesByType(AccountId owner, int type) {
        Args.notNull(owner, "owner id");

        TypedQuery<MatchEntity> query = entityManager.createNamedQuery(JPQL_GET_BY_OWNER_TYPE, MatchEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TYPE, type);
        List<MatchEntity> matchEntities = query.getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getTargetMatchesByType(AccountId target, int type) throws MatchServiceException {
        Args.notNull(target, "target id");

        List<MatchEntity> matchEntities = entityManager.createNamedQuery(JPQL_GET_BY_TARGET_TYPE, MatchEntity.class)
                .setParameter(PARAM_TARGET_ID, target.getInternalId())
                .setParameter(PARAM_TYPE, type)
                .getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getLatestMatchesByType(AccountId owner, int type, int limit) {
        Args.notNull(owner, "owner id");
        notNegativeOrZero(limit, "limit");

        List<MatchEntity> matchEntities = entityManager.createNamedQuery(JPQL_GET_LATEST_BY_OWNER_TYPE, MatchEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TYPE, type)
                .setMaxResults(limit)
                .getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    @Override
    public List<Match> getLatestMatches(AccountId owner, int limit) {
        Args.notNull(owner, "owner id");
        notNegativeOrZero(limit, "limit");

        List<MatchEntity> matchEntities = entityManager.createNamedQuery(JPQL_GET_LATEST_BY_OWNER, MatchEntity.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setMaxResults(limit)
                .getResultList();

        return matchEntities2matchBOs(matchEntities);
    }

    private int notNegativeOrZero(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " may not be negative or zero");
        }
        return value;
    }

    @Override
    public void deleteMatch(AccountId owner, AccountId target, int type) throws MatchServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        MatchId matchId = new MatchId(owner, target, type);
        MatchEntity matchEntity = entityManager.find(MatchEntity.class, matchId);
        if (matchEntity == null) {
            throw new MatchNotFoundException(owner, target, type);
        }

        entityManager.remove(matchEntity);

        isMatchedCache.put(getMatchedCacheKey(owner, target, type), false);
        ownersCache.remove(owner);
        targetsCache.remove(target);
    }

    @Override
    public void deleteMatches(AccountId owner, AccountId target) throws MatchServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_OWNER_TARGET)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TARGET_ID, target.getInternalId())
                .executeUpdate();

        isMatchedCache.clear();
        ownersCache.remove(owner);
        targetsCache.remove(target);

        LOGGER.info("Deleted {} matches for owner={}, target={}", deletedCount, owner, target);
    }

    @Override
    public void deleteMatchesByOwner(AccountId owner) {
        Args.notNull(owner, "owner id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_OWNER)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .executeUpdate();

        isMatchedCache.clear();
        ownersCache.remove(owner);

        LOGGER.info("Deleted {} matches for owner={}", deletedCount, owner);
    }

    @Override
    public void deleteMatchesByTarget(AccountId target) {
        Args.notNull(target, "target id");

        int deletedCount = entityManager.createNamedQuery(JPQL_DELETE_BY_TARGET)
                .setParameter(PARAM_TARGET_ID, target.getInternalId())
                .executeUpdate();

        isMatchedCache.clear();
        targetsCache.remove(target);

        LOGGER.info("Deleted {} matches for target={}", deletedCount, target);
    }

    @Override
    public void hideMatch(AccountId owner, AccountId target, int type) throws MatchServiceException {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        Match match = getMatch(owner, target, type);
        match.setHidden(true);
        entityManager.merge(matchBO2matchEntity(match));

        isMatchedCache.put(getMatchedCacheKey(owner, target, type), false);
        ownersCache.remove(owner);
        targetsCache.remove(target);
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
        MatchEntity matchEntity = getMatchEntityFromPersistence(match);

        return matchEntity != null;
    }

    private MatchEntity getMatchEntityFromPersistence(Match match) {
        MatchId matchId = new MatchId(match.getOwner(), match.getTarget(), match.getType());
        return entityManager.find(MatchEntity.class, matchId);
    }

    private List<Match> matchEntities2matchBOs(List<MatchEntity> matchEntities) {
        List<Match> results = new ArrayList<>(matchEntities.size());

        for (MatchEntity matchEntity : matchEntities) {
            Match match = matchEntity2matchBO(matchEntity);
            results.add(match);
        }

        return results;
    }

    //this method is unsafe, it should be called from locked areas only.
    private void putInCache(Cache<AccountId, List<Match>> cache, AccountId accountId, Match match, String namedQuery, String paramAccount) {
        List<Match> list = cache.get(accountId);
        if (list == null || list.isEmpty()) {
            list = getMatchesInternally(accountId, namedQuery, paramAccount);
            cache.put(accountId, list);
        }
        list.add(match);
    }

    //this method is unsafe, it should be called from locked areas only.
    private void putInCache(Cache<AccountId, List<Match>> cache, AccountId accountId, List<Match> matches, String namedQuery, String paramAccount) {
        List<Match> list = cache.get(accountId);
        if (list == null || list.isEmpty()) {
            list = getMatchesInternally(accountId, namedQuery, paramAccount);;
            cache.put(accountId, list);
        }
        list.addAll(matches);
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
