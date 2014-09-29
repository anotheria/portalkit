package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import org.distributeme.annotation.DistributeMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 11.12.12 15:56
 */
@DistributeMe()
public interface AuthenticationService extends Service {

    /**
     * Sets new password for the user.
     *
     * @param id
     * @param password
     * @throws AuthenticationServiceException
     */
    void setPassword(AccountId id, String password) throws AuthenticationServiceException;

    /**
     * Returns true if the user can authenticate with the given password.
     *
     * @param id
     * @param password
     * @return
     * @throws AuthenticationServiceException
     */
    boolean canAuthenticate(AccountId id, String password) throws AuthenticationServiceException;

    /**
     * Authenticates the user with encrypted token. Returns the id of the authenticated user or throws exception. Note,
     * if the token is one-use token, the token is considered used after the call to this method.
     *
     * @param token
     * @return
     * @throws AuthenticationServiceException
     */
    AccountId authenticateByEncryptedToken(String token) throws AuthenticationServiceException;

    /**
     * Returns if the user can by authenticate by the token. The difference is that this call doesn't reduce the usability of the token.
     * Use for administration perposses.
     *
     * @param token
     * @return
     * @throws AuthenticationServiceException
     */
    boolean canAuthenticateByEncryptedToken(String token) throws AuthenticationServiceException;

    /**
     * Creates a new token with same parameters as parameter token. The returned token is already saved in the db (with
     * all consequences) and can be issued to the user by calling getEncodedAuthString.
     *
     * @param accountId
     * @param prefilledToken
     * @return
     * @throws AuthenticationServiceException
     */
    EncryptedAuthToken generateEncryptedToken(AccountId accountId, AuthToken prefilledToken) throws AuthenticationServiceException;

    /**
     * Remove auth tokens and password by provided accountId.
     *
     * @param accountId provided {@link AccountId}
     * @throws AuthenticationServiceException on errors
     */
    void deleteAuthenticationData(AccountId accountId) throws AuthenticationServiceException;
}
