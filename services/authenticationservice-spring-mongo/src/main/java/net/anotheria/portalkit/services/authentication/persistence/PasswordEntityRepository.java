package net.anotheria.portalkit.services.authentication.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * {@link MongoRepository} for {@link PasswordEntity}.
 *
 * @author ykalapusha
 * @since 27.10.2025
 */
public interface PasswordEntityRepository extends MongoRepository<PasswordEntity, String> {
    /**
     * Finds the password for a given account ID.
     *
     * @param accountId the account ID whose password should be retrieved
     * @return an {@link Optional} containing the password if found, or empty if not found
     */
    @Query(value = "{ '_id': ?0 }", fields = "{ 'password': 1, '_id': 0 }")
    Optional<String> findPasswordByAccountId(String accountId);

    /**
     * Delete all tokens for given account id.
     *
     * @param accountId account id parameter for deletion
     */
    void  deleteByAccountId(String accountId);

    /**
     * Find all ids.
     *
     * @return list of ids in persistence
     */
    @Query(value ="{}", fields = "{ '_id' : 1 }")
    List<String> findAllIds();
}
