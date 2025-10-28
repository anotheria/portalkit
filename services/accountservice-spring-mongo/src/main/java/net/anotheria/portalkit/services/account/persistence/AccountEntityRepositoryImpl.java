package net.anotheria.portalkit.services.account.persistence;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import net.anotheria.portalkit.services.account.AccountQuery;
import net.anotheria.portalkit.services.common.AccountId;

@Repository
public class AccountEntityRepositoryImpl implements AccountEntityRepositoryCustom {

    private final MongoTemplate mongo;

    AccountEntityRepositoryImpl(MongoTemplate mongo) { this.mongo = mongo; }

    @Override
    public List<AccountEntity> search(AccountQuery q) {
        Query query = new Query(buildCriteria(q));
        List<AccountEntity> items = mongo.find(query, AccountEntity.class);
        return items;
    }

    @Override
    public long countMatching(AccountQuery q) {
        return mongo.count(new Query(buildCriteria(q)), AccountEntity.class);
    }

    private Criteria buildCriteria(AccountQuery q) {
        List<Criteria> cs = new ArrayList<>();

        if (q.getIds() != null && !q.getIds().isEmpty()) {
            cs.add(Criteria.where("_id")
                    .in(q.getIds().stream().map(AccountId::getInternalId).toList()));
        }
        if (hasText(q.getNameMask())) {
            cs.add(Criteria.where("name").regex(mask(q.getNameMask()), "i"));
        }
        if (hasText(q.getEmailMask())) {
            cs.add(Criteria.where("email").regex(mask(q.getEmailMask()), "i"));
        }
        if (hasText(q.getIdMask())) {
            cs.add(Criteria.where("_id").regex(mask(q.getIdMask()), "i"));
        }
        if (q.getTypesIncluded() != null && !q.getTypesIncluded().isEmpty()) {
            cs.add(Criteria.where("type").in(q.getTypesIncluded()));
        }
        if (q.getTypesExcluded() != null && !q.getTypesExcluded().isEmpty()) {
            cs.add(Criteria.where("type").nin(q.getTypesExcluded()));
        }
        // Falls Status ein Bitfeld (long) ist:
        if (q.getStatusesIncluded() != null && !q.getStatusesIncluded().isEmpty()) {
            // alle Bits müssen gesetzt sein
            long mask = combineBits(q.getStatusesIncluded());
            cs.add(Criteria.where("status").bits().allSet((int)mask));//TODO possible precision loss? we have long in the interface.
        }
        if (q.getStatusesExcluded() != null && !q.getStatusesExcluded().isEmpty()) {
            long mask = combineBits(q.getStatusesExcluded());
            cs.add(Criteria.where("status").bits().allClear((int)mask)); //TODO possible precision loss? we have long in the interface.
        }
        if (q.getRegisteredFrom() != null || q.getRegisteredTill() != null) {
            Criteria c = Criteria.where("registered");
            if (q.getRegisteredFrom() != null) c = c.gte(q.getRegisteredFrom());
            if (q.getRegisteredTill() != null) c = c.lte(q.getRegisteredTill());
            cs.add(c);
        }
        if (q.getTenants() != null && !q.getTenants().isEmpty()) {
            cs.add(Criteria.where("tenant").in(q.getTenants()));
        }
        if (hasText(q.getBrand())) {
            cs.add(Criteria.where("brand").is(q.getBrand()));
        }

        return cs.isEmpty() ? new Criteria() : new Criteria().andOperator(cs.toArray(new Criteria[0]));
    }

    private static boolean hasText(String s) { return s != null && !s.isBlank(); }

    // unterstützt * und ? wie Wildcards
    private static String mask(String mask) {
        String esc = java.util.regex.Pattern.quote(mask);
        return esc.replace("\\*", ".*").replace("\\?", ".");
    }

    private static long combineBits(List<Long> flags) {
        long m = 0L; for (Long f : flags) m |= f; return m;
    }

}
