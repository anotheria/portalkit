package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.entity.Match;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author bvanchuhov
 */
@Service
@Transactional
public class MatchServiceImpl implements MatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchServiceImpl.class);

    private static final String PARAM_OWNER_ID = "ownerId";
    private static final String PARAM_TARGET_ID = "targetId";
    private static final String PARAM_TYPE = "type";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addMatch(AccountId owner, AccountId target, int type) {
        Match match = new Match(owner, target, type);
        match.setCreated(System.currentTimeMillis());

        entityManager.persist(match);
    }

    @Override
    public List<Match> getMatches(AccountId owner) {
        Args.notNull(owner, "owner");

        TypedQuery<Match> query = entityManager.createNamedQuery(Match.JPQL_GET_BY_OWNER, Match.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId());
        return query.getResultList();
    }

    @Override
    public List<Match> getMatchesByType(AccountId owner, int type) {
        Args.notNull(owner, "owner");
        Args.notNull(type, "match type");

        TypedQuery<Match> query = entityManager.createNamedQuery(Match.JPQL_GET_BY_OWNER_TYPE, Match.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TYPE, type);
        return query.getResultList();
    }

    @Override
    public List<Match> getLatestMatchesByType(AccountId owner, int type, int limit) {
        Args.notNull(owner, "owner");
        Args.notNull(type, "type");
        notNegativeOrZero(limit, "limit");

        TypedQuery<Match> query = entityManager.createNamedQuery(Match.JPQL_GET_LATEST_BY_OWNER_TYPE, Match.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setParameter(PARAM_TYPE, type)
                .setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Match> getLatestMatches(AccountId owner, int limit) {
        Args.notNull(owner, "owner");
        notNegativeOrZero(limit, "limit");

        TypedQuery<Match> query = entityManager.createNamedQuery(Match.JPQL_GET_LATEST_BY_OWNER, Match.class)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId())
                .setMaxResults(limit);
        return query.getResultList();
    }

    private int notNegativeOrZero(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " may not be negative or zero");
        }
        return value;
    }

    @Override
    public void deleteMatchesByOwner(AccountId owner) {
        Args.notNull(owner, "owner");

        Query query = entityManager.createNamedQuery(Match.JPQL_DELETE_BY_OWNER)
                .setParameter(PARAM_OWNER_ID, owner.getInternalId());
        int deletedCount = query.executeUpdate();

        LOGGER.info("Deleted {} matches for owner [{}]", deletedCount, owner);
    }

    @Override
    public void deleteMatchesByTarget(AccountId target) {
        Args.notNull(target, "target");

        Query query = entityManager.createNamedQuery(Match.JPQL_DELETE_BY_TARGET)
                .setParameter(PARAM_TARGET_ID, target.getInternalId());
        int deletedCount = query.executeUpdate();

        LOGGER.info("Deleted {} matches for target [{}]", deletedCount, target);
    }
}
