package net.anotheria.portalkit.services.pushtoken.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

/**
 * PushToken persistence service.
 */
public interface PushTokenPersistenceService {

    /**
     * Searches and returns for a list of tokens that are belonged to provided user
     *
     * @param accountId account whom tokens want to find and return
     * @return list of tokens. Empty if user has no token at all.
     */
    List<String> getAllTokensByAccountId(AccountId accountId) throws PushTokenPersistenceServiceException;

    /**
     * Saves token for a provided account.
     *
     * @param accountId account that will have a new or one more token
     * @param token     token to bind with account
     * @return saved token
     */
    String saveTokenForAccount(AccountId accountId, String token) throws PushTokenPersistenceServiceException;

    /**
     * Deletes a token. No user will have it. Deletes token completely from storage.
     *
     * @param token token to delete
     * @return account who had this token before. Null if token wasn't in use.
     */
    AccountId deleteToken(String token) throws PushTokenPersistenceServiceException;

    /**
     * Deletes all tokens from account
     *
     * @param accountId account whom tokens will be removed
     */
    void deleteAllFromAccount(AccountId accountId) throws PushTokenPersistenceServiceException;

}
