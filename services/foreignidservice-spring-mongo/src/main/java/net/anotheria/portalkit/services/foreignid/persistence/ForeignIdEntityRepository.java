package net.anotheria.portalkit.services.foreignid.persistence;

import net.anotheria.portalkit.services.common.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * ForeignIdEntityRepository — {@link MongoRepository} for {@link ForeignIdEntity}.
 *
 * @author ykalapusha
 * @since 16.12.2025
 */
public interface ForeignIdEntityRepository extends MongoRepository<ForeignIdEntity, ForeignIdEntityId> {
    /**
     * Find all foreign ids by account id.
     *
     * @param accountId account id parameter for search
     * @return list of foreign ids linked to account id
     */
    List<ForeignIdEntity> findByAccountId(String accountId);

    /**
     * Remove all foreign ids by account id.
     *
     * @param accountId account id parameter for removal
     */
    void removeByAccountId(String accountId);
}
