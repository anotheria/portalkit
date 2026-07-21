package net.anotheria.portalkit.services.authentication.persistence;

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
}
