package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of the AuthenticationService.
 *
 * @author lrosenberg
 * @since 13.12.12 09:30
 */
@Monitor(subsystem = "portalkit")
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * The currently used password enhashing algorithm.
     */
    private PasswordEncryptionAlgorithm passwordAlgorithm;
    /**
     * The persistence service.
     */
    private AuthenticationPersistenceService persistenceService;

    /**
     * Logger.
     */
    private static Logger log = Logger.getLogger(AuthenticationServiceImpl.class);

    /**
     * Default constructor.
     */
    public AuthenticationServiceImpl() {
        AuthenticationServiceConfig config = new AuthenticationServiceConfig();
        //initialize with configureme.
        try {
            passwordAlgorithm = PasswordEncryptionAlgorithm.class.cast(Class.forName(config.getPasswordAlgorithm()).newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Can't operate without configured and available PasswordEncryptionAlgorithm (config=" + config.getPasswordAlgorithm() + ")", e);
        }

        passwordAlgorithm.customize(config.getPasswordKey());

        //note, this will work with a) only one AuthenticationPersistenceService impl or b) configured metafactory
        try {
            persistenceService = MetaFactory.get(AuthenticationPersistenceService.class);
        } catch (MetaFactoryException e) {
            throw new IllegalStateException("Can't work without a persistence service", e);
        }

    }

    @Override
    public void setPassword(AccountId id, String password) throws AuthenticationServiceException {
        if (id == null)
            throw new IllegalArgumentException("id can't be null");
        try {
            persistenceService.saveEncryptedPassword(id, passwordAlgorithm.encryptPassword(password));
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }
    }

    @Override
    public boolean canAuthenticate(AccountId id, String password) throws AuthenticationServiceException {
        if (id == null)
            throw new IllegalArgumentException("id can't be null");
        String storedEncryptedPassword = null;
        try {
            storedEncryptedPassword = persistenceService.getEncryptedPassword(id);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }
        return storedEncryptedPassword != null && password != null && storedEncryptedPassword.equals(passwordAlgorithm.encryptPassword(password));
    }

    @Override
    public AccountId authenticateByEncryptedToken(String token) throws AuthenticationServiceException {
        try {
            boolean tokenExists = persistenceService.authTokenExists(token);
            if (!tokenExists)
                throw new AuthTokenNotFoundException();
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }

        AuthToken authToken = decrypt(token);

        if (authToken.isExpired())
            throw new AuthTokenExpiredException();

        if (!authToken.isMultiUse()) {
            try {
                persistenceService.deleteAuthToken(authToken.getAccountId(), token);
            } catch (AuthenticationPersistenceServiceException e) {
                log.warn("Couldn't delete used auth token " + token + " for " + authToken.getAccountId());
            }
        }

        return authToken.getAccountId();
    }

    @Override
    public boolean canAuthenticateByEncryptedToken(String token) throws AuthenticationServiceException {
        try {
            boolean tokenExists = persistenceService.authTokenExists(token);
            if (!tokenExists)
                return false;
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }

        AuthToken authToken = decrypt(token);
        return !authToken.isExpired();

    }

    /**
     * Internal helper abstraction.
     *
     * @param token token to decrypt.
     * @return decrypted AuthToken.
     */
    private AuthToken decrypt(String token) {
        return AuthTokenEncryptors.decrypt(token);
    }


    @Override
    public EncryptedAuthToken generateEncryptedToken(AccountId accountId, AuthToken prefilledToken) throws AuthenticationServiceException {

        AuthToken newToken = (AuthToken) prefilledToken.clone();
        String encryption = AuthTokenEncryptors.encrypt(newToken);

        EncryptedAuthToken encToken = new EncryptedAuthToken();
        encToken.setAuthToken(newToken);
        encToken.setEncryptedVersion(encryption);

        try {
            if (newToken.isExclusive())
                persistenceService.deleteAuthTokens(accountId);

            if (!newToken.isExclusive() && newToken.isExclusiveInType()) {
                //this is maybe suboptimal, but no other chance to fix it otherways for now
                Set<String> tokens = persistenceService.getAuthTokens(newToken.getAccountId());
                for (Iterator<String> it = tokens.iterator(); it.hasNext(); ) {
                    String storedToken = it.next();
                    AuthToken t = AuthTokenEncryptors.decrypt(storedToken);
                    if (t.getType() == newToken.getType())
                        persistenceService.deleteAuthToken(newToken.getAccountId(), storedToken);
                }
            }


            persistenceService.saveAuthToken(accountId, encryption);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }


        return encToken;
    }

    @Override
    public void deleteData(AccountId accountId) {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");
        try {
            persistenceService.deleteAuthTokens(accountId);
        } catch (AuthenticationPersistenceServiceException e) {
            log.error("Couldn't delete auth tokens for " + accountId);
        }
        try {
            persistenceService.deleteEncryptedPassword(accountId);
        } catch (AuthenticationPersistenceServiceException e) {
            log.error("Couldn't delete encrypt password for " + accountId);
        }
    }
}
