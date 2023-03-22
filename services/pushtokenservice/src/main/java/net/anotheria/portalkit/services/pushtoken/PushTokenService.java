package net.anotheria.portalkit.services.pushtoken;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;

/**
 * PushToken service is used to store and manage data about push-notification tokens.
 * One user (accountId) can have many push-notification tokens.
 * Every token is unique. So two users cannot have the same tokens.
 */
@DistributeMe
@FailBy(strategyClass = RetryCallOnce.class)
public interface PushTokenService extends Service {

    /**
     * Returns a list of tokens that are belonged to provided user.
     *
     * @param accountId user whom push-notification tokens want to retrieve
     * @return list of tokens
     */
    List<String> getAllByAccountId(AccountId accountId) throws PushTokenServiceException;

    /**
     * Adds a provided token to the list of user's tokens
     *
     * @param accountId account that will have a new or one more token
     * @param token     token to add
     * @return added token
     */
    String addTokenForAccount(AccountId accountId, String token) throws PushTokenServiceException;

    /**
     * Deletes a token. After deletion, no user will have this token.
     *
     * @param token token to delete
     * @return returns account that was owner of token
     */
    AccountId removeToken(String token) throws PushTokenServiceException;

    /**
     * Deletes all tokens that are belonged to provided account.
     *
     * @param accountId account whom tokens will be deleted
     */
    void removeAllFromAccount(AccountId accountId) throws PushTokenServiceException;

}
