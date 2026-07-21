package net.anotheria.portalkit.services.account.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.anotheria.portalkit.services.account.AccountQuery;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom fragment implementing {@link AccountEntityRepositoryCustom#search(AccountQuery)} via the JPA
 * Criteria API. Ports the semantics of the legacy {@code AccountDAO.getAccountsByQuery}, including the
 * status bitmask post-filtering that cannot be expressed in SQL (status is a {@code long} bitfield).
 */
@Repository
public class AccountEntityRepositoryImpl implements AccountEntityRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<AccountEntity> search(AccountQuery query) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AccountEntity> cq = cb.createQuery(AccountEntity.class);
        Root<AccountEntity> root = cq.from(AccountEntity.class);

        cq.select(root)
                .where(buildPredicates(cb, root, query).toArray(new Predicate[0]))
                .orderBy(cb.desc(root.get("registrationTimestamp")));

        List<AccountEntity> rawResult = em.createQuery(cq).getResultList();
        return filterByStatus(rawResult, query);
    }

    @Override
    @Transactional(readOnly = true)
    public long countMatching(AccountQuery query) {
        // status filtering cannot be pushed into SQL, so count is computed on the filtered result.
        return search(query).size();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<AccountEntity> root, AccountQuery q) {
        List<Predicate> predicates = new ArrayList<>();

        if (q.getRegisteredFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("registrationTimestamp"), q.getRegisteredFrom()));
        if (q.getRegisteredTill() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("registrationTimestamp"), q.getRegisteredTill()));

        if (q.getIds() != null && !q.getIds().isEmpty()) {
            List<String> ids = new ArrayList<>();
            for (AccountId id : q.getIds())
                ids.add(id.getInternalId());
            predicates.add(root.get("id").in(ids));
        }
        if (!StringUtils.isEmpty(q.getEmailMask()))
            predicates.add(cb.like(root.get("email"), q.getEmailMask()));
        if (!StringUtils.isEmpty(q.getNameMask()))
            predicates.add(cb.like(root.get("name"), q.getNameMask()));
        if (!StringUtils.isEmpty(q.getIdMask()))
            predicates.add(cb.like(root.get("id"), q.getIdMask()));
        if (q.getTypesIncluded() != null && !q.getTypesIncluded().isEmpty())
            predicates.add(root.get("type").in(q.getTypesIncluded()));
        if (q.getTypesExcluded() != null && !q.getTypesExcluded().isEmpty())
            predicates.add(cb.not(root.get("type").in(q.getTypesExcluded())));
        if (q.getTenants() != null && !q.getTenants().isEmpty())
            predicates.add(root.get("tenant").in(q.getTenants()));
        if (!StringUtils.isEmpty(q.getBrand()))
            predicates.add(cb.equal(root.get("brand"), q.getBrand()));

        return predicates;
    }

    private List<AccountEntity> filterByStatus(List<AccountEntity> rawResult, AccountQuery query) {
        if (rawResult.isEmpty())
            return rawResult;
        boolean noIncluded = query.getStatusesIncluded() == null || query.getStatusesIncluded().isEmpty();
        boolean noExcluded = query.getStatusesExcluded() == null || query.getStatusesExcluded().isEmpty();
        if (noIncluded && noExcluded)
            return rawResult;

        List<AccountEntity> result = new ArrayList<>();
        for (AccountEntity entity : rawResult) {
            long status = entity.getStatus();
            boolean skip = false;

            if (!noIncluded) {
                for (Long required : query.getStatusesIncluded()) {
                    if ((status & required) != required) { // account doesn't have a required status
                        skip = true;
                        break;
                    }
                }
            }
            if (skip)
                continue;

            if (!noExcluded) {
                for (Long forbidden : query.getStatusesExcluded()) {
                    if ((status & forbidden) == forbidden) { // account has a forbidden status
                        skip = true;
                        break;
                    }
                }
            }
            if (skip)
                continue;

            result.add(entity);
        }
        return result;
    }
}
