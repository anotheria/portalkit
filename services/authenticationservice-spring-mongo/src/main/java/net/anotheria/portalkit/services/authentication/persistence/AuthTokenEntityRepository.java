package net.anotheria.portalkit.services.authentication.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * {@link MongoRepository} for {@link AuthTokenEntity}.
 *
 * @author ykalapusha
 * @since 27.10.2025
 */
public interface AuthTokenEntityRepository extends MongoRepository<AuthTokenEntity, String> {
    /**
     * Check if token stored in database.
     *
     * @param token token for check
     * @return {@code true} - if token exists, {@code false} - otherwise
     */
    boolean existsByToken(String token);

    /**
     * Delete entity by token value.
     *
     * @param token token id for deletion
     */
    void deleteByToken(String token);

    /**
     * Delete all tokens for given account id.
     *
     * @param accountId account id parameter for deletion
     */
    void  deleteByAccountId(String accountId);

    /**
     * Deletes all authentication tokens matching the specified account ID and type.
     *
     * @param accountId the ID of the account whose tokens should be deleted
     * @param type the type of tokens to delete
     */
    void deleteByAccountIdAndType(String accountId, int type);

    /**
     * Deletes authentication token matching the specified account ID and token.
     *
     * @param accountId the ID of the account whose tokens should be deleted
     * @param token the token to delete
     */
    void deleteByAccountIdAndToken(String accountId, String token);

    /**
     * Returns all token values for the given account ID and token type.
     *
     * @param accountId the ID of the account whose tokens should be retrieved
     * @param type the token type
     * @return a list of token strings associated with the account and type
     */
    @Query(value = "{ 'accountId': ?0, 'type': ?1 }", fields = "{ '_id': 1 }")
    List<String> findTokensByAccountIdAndType(String accountId, int type);

    /**
     * Find all ids.
     *
     * @return list of ids in persistence
     */
    @Query(value ="{}", fields = "{ '_id' : 1 }")
    List<String> findAllIds();
}
