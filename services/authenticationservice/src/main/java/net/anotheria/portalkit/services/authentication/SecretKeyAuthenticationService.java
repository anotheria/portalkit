package net.anotheria.portalkit.services.authentication;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.DeletionService;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 2019-09-06 08:57
 */
@DistributeMe()
@FailBy(strategyClass= RetryCallOnce.class)
public interface SecretKeyAuthenticationService extends Service, DeletionService {
    /**
     * Sets new password for the user.
     *
     * @param id user id.
     * @param password user password.
     * @throws AuthenticationServiceException if error
     */
    void setPassword(AccountId id, String password) throws AuthenticationServiceException;

    /**
     * Returns true if the user can authenticate with the given password.
     *
     * @param id user id.
     * @param password user password.
     * @return boolean
     * @throws AuthenticationServiceException if error
     */
    boolean canAuthenticate(AccountId id, String password) throws AuthenticationServiceException;

    /**
     * Authenticates the user with encrypted token. Returns the id of the authenticated user or throws exception. Note,
     * if the token is one-use token, the token is considered used after the call to this method.
     *
     * @param token login token.
     * @return {@link AccountId}
     * @throws AuthenticationServiceException if error
     */
    AccountId authenticateByEncryptedToken(String token) throws AuthenticationServiceException;

    /**
     * Returns if the user can by authenticate by the token. The difference is that this call doesn't reduce the usability of the token.
     * Use for administration perposses.
     *
     * @param token login token.
     * @return boolean
     * @throws AuthenticationServiceException if error
     */
    boolean canAuthenticateByEncryptedToken(String token) throws AuthenticationServiceException;

    /**
     * Creates a new token with same parameters as parameter token. The returned token is already saved in the db (with
     * all consequences) and can be issued to the user by calling getEncodedAuthString.
     *
     * @param accountId account id.
     * @param prefilledToken    {@link AuthToken}
     * @return {@link EncryptedAuthToken}
     * @throws AuthenticationServiceException if error
     */
    EncryptedAuthToken generateEncryptedToken(AccountId accountId, AuthToken prefilledToken) throws AuthenticationServiceException;

    /**
     * Removes all user tokens from database.
     *
     * @param accountId account id.
     * @throws AuthenticationServiceException if error
     */
    void deleteTokens(AccountId accountId) throws AuthenticationServiceException;

	/**
	 * Removes all user tokens of given type from database.
	 *
	 * @param accountId account id.
	 * @param type token type.
	 * @throws AuthenticationServiceException if error
	 */
	void deleteTokensByType(AccountId accountId, int type) throws AuthenticationServiceException;

    /**
     * Removes one user token from database.
     *
     * @param accountId account id.
     * @param token token.
     * @throws AuthenticationServiceException if error
     */
    void deleteToken(AccountId accountId, String token) throws AuthenticationServiceException;

}
