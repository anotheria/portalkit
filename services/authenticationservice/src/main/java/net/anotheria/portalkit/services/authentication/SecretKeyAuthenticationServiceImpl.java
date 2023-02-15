package net.anotheria.portalkit.services.authentication;

import java.util.Iterator;
import java.util.Set;

import org.configureme.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.authentication.encryptors.BlowfishPasswordEncryptionAlgorithm;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * Implementation of the AuthenticationService.
 */
@Monitor(subsystem = "authentication", category = "portalkit-service")
public class SecretKeyAuthenticationServiceImpl implements SecretKeyAuthenticationService {

    /**
     * The currently used password enhashing algorithm.
     */
    private PasswordEncryptionAlgorithm passwordAlgorithm;
    /**
     * Encryption algorithm for accountId.
     */
    private PasswordEncryptionAlgorithm accountIdAlgorithm;
    /**
     * The persistence service.
     */
    private AuthenticationPersistenceService persistenceService;
    /**
     * Application secret.
     */
    private final static String APP_KEY = (char) 56 + 'x' + "8UJlqYBKS4dh";
    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(SecretKeyAuthenticationServiceImpl.class);

    /**
     * Default constructor.
     */
    public SecretKeyAuthenticationServiceImpl() {
        accountIdAlgorithm = new BlowfishPasswordEncryptionAlgorithm();
        accountIdAlgorithm.customize(APP_KEY);

        AuthenticationServiceConfig config = new AuthenticationServiceConfig();
        try {
            ConfigurationManager.INSTANCE.configure(config);
        } catch (IllegalArgumentException e) {
            log.warn("Couldn't find configuration file for auth config (pk-auth) will work with default values");
        }
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
            persistenceService.saveEncryptedPassword(getEncrypted(id), passwordAlgorithm.encryptPassword(password));
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
            storedEncryptedPassword = persistenceService.getEncryptedPassword(getEncrypted(id));
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
                persistenceService.deleteAuthToken(getEncrypted(authToken.getAccountId()), token);
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
        AccountId encryptedAccountId = getEncrypted(accountId);

        EncryptedAuthToken encToken = new EncryptedAuthToken();
        encToken.setAuthToken(newToken);
        encToken.setEncryptedVersion(encryption);
        try {
            if (newToken.isExclusive())
                persistenceService.deleteAuthTokens(encryptedAccountId);

            if (!newToken.isExclusive() && newToken.isExclusiveInType()) {
                //this is maybe suboptimal, but no other chance to fix it otherways for now
                Set<String> tokens = persistenceService.getAuthTokens(encryptedAccountId);
                for (Iterator<String> it = tokens.iterator(); it.hasNext(); ) {
                    String storedToken = it.next();
                    AuthToken t = AuthTokenEncryptors.decrypt(storedToken);
                    if (t.getType() == newToken.getType())
                        persistenceService.deleteAuthToken(getEncrypted(newToken.getAccountId()), storedToken);
                }
            }


            persistenceService.saveAuthToken(encryptedAccountId, encryption);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }


        return encToken;
    }

    @Override
    public void deleteTokens(AccountId accountId) throws AuthenticationServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");
        try {
            persistenceService.deleteAuthTokens(getEncrypted(accountId));
        } catch (AuthenticationPersistenceServiceException e) {
            log.error("Couldn't delete auth tokens for " + accountId);
        }
    }

    @Override
    public void deleteData(AccountId accountId) {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");

        AccountId encrypted = getEncrypted(accountId);
        try {
            persistenceService.deleteAuthTokens(encrypted);
        } catch (AuthenticationPersistenceServiceException e) {
            log.error("Couldn't delete auth tokens for " + accountId);
        }
        try {
            persistenceService.deleteEncryptedPassword(encrypted);
        } catch (AuthenticationPersistenceServiceException e) {
            log.error("Couldn't delete encrypt password for " + accountId);
        }
    }

    @Override
    public void deleteTokensByType(AccountId accountId, int type) throws AuthenticationServiceException {
        Set<String> tokens = null;
        AccountId encrypted = getEncrypted(accountId);
        try {
            tokens = persistenceService.getAuthTokens(encrypted);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException("Can't retrieve tokens for user " + accountId, e);
        }

        for (String token : tokens) {
            AuthToken authToken = decrypt(token);
            if (authToken.getType() == type) {
                try {
                    persistenceService.deleteAuthToken(encrypted, token);
                } catch (AuthenticationPersistenceServiceException e) {
                    log.error("Can't delete token for user " + accountId + ", type " + type + ", token: " + token, e);
                }
            }
        }
    }

    @Override
    public void deleteToken(AccountId accountId, String token) throws AuthenticationServiceException {
        Set<String> tokens = null;
        AccountId encrypted = getEncrypted(accountId);
        try {
            tokens = persistenceService.getAuthTokens(encrypted);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException("Can't retrieve tokens for user " + accountId, e);
        }

        for (String tokenFromDB : tokens) {
            if (tokenFromDB.equals(token)) {
                try {
                    persistenceService.deleteAuthToken(encrypted, token);
                } catch (AuthenticationPersistenceServiceException e) {
                    log.error("Can't delete token for user " + accountId + ", token: " + token, e);
                }
            }
        }
    }

    /*
    @Override
    public void deleteToken(AccountId accountId, String token) throws AuthenticationServiceException {
        Set<String> tokens = null;
        AccountId encrypted = getEncrypted(accountId);
        try {
            tokens = persistenceService.getAuthTokens(encrypted);
        } catch(AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException("Can't retrieve tokens for user "+accountId, e);
        }

        for (String tokenFromDB : tokens) {
            if (tokenFromDB.equals(token)) {
                try {
                    persistenceService.deleteAuthToken(encrypted, token);
                } catch(AuthenticationPersistenceServiceException e) {
                    log.error("Can't delete token for user "+accountId+", token: "+token, e);
                }
            }
        }
    }
*/

    /**
     * Returns new instance of encrypted version of AccountId.
     *
     * @param id to encrypt
     * @return encrypted account id
     */
    private AccountId getEncrypted(AccountId id) {
        return new AccountId(accountIdAlgorithm.encryptPassword(id.getInternalId()));
    }

    @Override
    public EncryptedAuthToken saveEncryptedToken(AccountId accountId, AuthToken prefilledToken) throws AuthenticationServiceException {
        accountId = getEncrypted(accountId);
        AuthToken newToken = (AuthToken) prefilledToken.clone();
        String encryption = AuthTokenEncryptors.encrypt(newToken);

        EncryptedAuthToken encToken = new EncryptedAuthToken();
        encToken.setAuthToken(newToken);
        encToken.setEncryptedVersion(encryption);

        try {
            if (newToken.isExclusive())
                persistenceService.deleteAuthTokens(accountId);

            if (!newToken.isExclusive() && newToken.isExclusiveInType()) {
                for (String token : persistenceService.getAuthTokens(newToken.getAccountId())) {
                    AuthToken t = AuthTokenEncryptors.decrypt(token);
                    if (t.getType() == newToken.getType())
                        persistenceService.deleteAuthToken(newToken.getAccountId(), token);
                }
            }
            persistenceService.saveAuthTokenAdditional(accountId, encToken);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }
        return encToken;
    }

    @Override
    public String getTokenByType(AccountId accountId, int type) throws AuthenticationServiceException {
        accountId = getEncrypted(accountId);
        Set<String> tokens = null;
        try {
            tokens = persistenceService.getAuthTokens(accountId);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException("Can't retrieve tokens for user " + accountId, e);
        }

        for (String token : tokens) {
            AuthToken authToken = decrypt(token);
            if (authToken.getType() == type) {
                return token;
            }
        }
        return null;
    }
}
