package net.anotheria.portalkit.services.authentication.persistence;

import net.anotheria.portalkit.services.authentication.AuthToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

/**
 * {@link MongoRepository} for {@link AuthTokenEntity}.
 *
 * @author ykalapusha
 * @since 27.10.2025
 */
public interface AuthTokenEntityRepository extends MongoRepository<AuthTokenEntity, String> {
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
     * Returns all token values for the given account ID and token type.
     *
     * @param accountId the ID of the account whose tokens should be retrieved
     * @param type the token type
     * @return a list of token strings associated with the account and type
     */
    @Query(value = "{ 'accountId': ?0, 'type': ?1 }")
    List<AuthTokenEntity> findTokensByAccountIdAndType(String accountId, int type);
    /**
     * Find all ids.
     *
     * @return list of ids in persistence
     */
    @Query(value ="{}", fields = "{ '_id' : 1 }")
    List<String> findAllIds();

    /**
     * Returns all tokens of the given account.
     *
     * @param accountId the account id.
     * @return the stored tokens, never null.
     */
    List<AuthTokenEntity> findByAccountId(String accountId);

    /**
     * Returns the tokens of the given type, windowed by the given pageable.
     *
     * @param type     the token type.
     * @param pageable the window, see OffsetPageable.
     * @return the stored tokens, never null.
     */
    List<AuthTokenEntity> findByType(int type, Pageable pageable);

    /**
     * Sets the last used timestamp of the given token, but only if the stored one is older than the given
     * threshold or not present at all. The condition is part of the query so that a token which is used
     * constantly costs one update per interval and not one per authentication. Documents written before the
     * field existed do not carry it, and mongo does not match a missing field with a comparison, hence the
     * explicit exists clause.
     *
     * @param token           the token which was used.
     * @param timestamp       the timestamp to store.
     * @param onlyIfOlderThan store only if the currently stored value is smaller than this.
     */
    @Query("{ 'token': ?0, $or: [ { 'lastUsedAt': { $lt: ?2 } }, { 'lastUsedAt': { $exists: false } } ] }")
    @Update("{ '$set': { 'lastUsedAt': ?1 } }")
    void updateLastUsed(String token, long timestamp, long onlyIfOlderThan);
}
