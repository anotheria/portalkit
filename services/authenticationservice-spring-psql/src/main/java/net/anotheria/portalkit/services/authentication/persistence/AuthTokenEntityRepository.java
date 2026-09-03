package net.anotheria.portalkit.services.authentication.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for {@link AuthTokenEntity}. The bulk-delete methods are explicit
 * {@code @Modifying} queries (and transactional) so they run as single SQL statements.
 */
public interface AuthTokenEntityRepository extends JpaRepository<AuthTokenEntity, String> {

    /**
     * Delete entity by token value.
     *
     * @param token token id for deletion
     */
    @Modifying
    @Transactional
    @Query("delete from AuthTokenEntity t where t.token = ?1")
    void deleteByToken(String token);

    /**
     * Delete all tokens for given account id.
     *
     * @param accountId account id parameter for deletion
     */
    @Modifying
    @Transactional
    @Query("delete from AuthTokenEntity t where t.accountId = ?1")
    void deleteByAccountId(String accountId);

    /**
     * Deletes all authentication tokens matching the specified account ID and type.
     *
     * @param accountId the ID of the account whose tokens should be deleted
     * @param type the type of tokens to delete
     */
    @Modifying
    @Transactional
    @Query("delete from AuthTokenEntity t where t.accountId = ?1 and t.type = ?2")
    void deleteByAccountIdAndType(String accountId, int type);

    /**
     * Returns all tokens for the given account ID and token type.
     *
     * @param accountId the ID of the account whose tokens should be retrieved
     * @param type the token type
     * @return a list of tokens associated with the account and type
     */
    @Query("select t from AuthTokenEntity t where t.accountId = ?1 and t.type = ?2")
    List<AuthTokenEntity> findTokensByAccountIdAndType(String accountId, int type);

    /**
     * Find all token ids.
     *
     * @return list of token ids in persistence
     */
    @Query("select t.token from AuthTokenEntity t")
    List<String> findAllIds();

    /**
     * Returns all tokens of the given account.
     *
     * @param accountId the account id.
     * @return the stored tokens, never null.
     */
    @Query("select t from AuthTokenEntity t where t.accountId = ?1")
    List<AuthTokenEntity> findByAccountId(String accountId);

    /**
     * Returns the tokens of the given type, windowed by the given pageable. A token type which is used for
     * logins matches millions of rows, so this is never called unbounded.
     *
     * @param type     the token type.
     * @param pageable the window, see {@link OffsetPageable} - an arbitrary offset does not map onto a page
     *                 number, so a plain PageRequest cannot express it.
     * @return the stored tokens, never null.
     */
    @Query("select t from AuthTokenEntity t where t.type = ?1")
    List<AuthTokenEntity> findByType(int type, Pageable pageable);

    /**
     * Sets the last used timestamp of the given token, but only if the stored one is null or older than the
     * given threshold. The condition is part of the statement so that a token which is used constantly costs one
     * update per interval and not one per authentication.
     *
     * @param token           the token which was used.
     * @param timestamp       the timestamp to store.
     * @param onlyIfOlderThan store only if the currently stored value is null or smaller than this.
     */
    @Modifying
    @Transactional
    @Query("update AuthTokenEntity t set t.lastUsedAt = ?2 where t.token = ?1 and (t.lastUsedAt is null or t.lastUsedAt < ?3)")
    void updateLastUsed(String token, long timestamp, long onlyIfOlderThan);
}
