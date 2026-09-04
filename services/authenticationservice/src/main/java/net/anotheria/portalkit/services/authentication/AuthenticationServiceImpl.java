package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.integrity.IntegrityCheckHelper;
import net.anotheria.portalkit.services.common.integrity.IntegrityCheckResult;
import org.configureme.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the AuthenticationService.
 *
 * @author lrosenberg
 * @since 13.12.12 09:30
 */
@Monitor(subsystem = "authentication", category = "portalkit-service")
public class AuthenticationServiceImpl implements AuthenticationService, EntityManagingService {

    /**
     * The currently used password enhashing algorithm.
     */
    private PasswordEncryptionAlgorithm passwordAlgorithm;
    /**
     * The persistence service.
     */
    private AuthenticationPersistenceService persistenceService;
    /**
     * How old the stored last used timestamp of a token has to be before it is written again, in millis. See
     * AuthenticationServiceConfig#getLastUsedUpdateIntervalInHours().
     */
    private long lastUsedUpdateIntervalInMillis;

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    /**
     * Default constructor.
     */
    public AuthenticationServiceImpl() {
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
        lastUsedUpdateIntervalInMillis = config.getLastUsedUpdateIntervalInHours() * 60L * 60L * 1000L;

        //note, this will work with a) only one AuthenticationPersistenceService impl or b) configured metafactory
        try {
            persistenceService = MetaFactory.get(AuthenticationPersistenceService.class);
        } catch (MetaFactoryException e) {
            throw new IllegalStateException("Can't work without a persistence service", e);
        }
        EntityManagingServices.createEntityCounter(this, "AuthTokens");
    }

    @Override
    public int getEntityCount(String s) {
        switch (s) {
            case "AuthTokens":
                try {
                    return Long.valueOf(persistenceService.authTokensCount()).intValue();
                } catch (AuthenticationPersistenceServiceException e) {
                    log.error(e.getMessage());
                    return 0;
                }
        }
        return 0;
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
        //everything which can be decided from the token itself is decided first, so a forged, manipulated or
        //expired token costs no database query at all.
        AuthToken authToken = decryptOrReject(token);

        if (authToken.isExpired())
            throw new AuthTokenExpiredException();

        try {
            boolean tokenExists = persistenceService.authTokenExists(token);
            if (!tokenExists)
                throw new AuthTokenNotFoundException();
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }

        if (!authToken.isMultiUse()) {
            try {
                persistenceService.deleteAuthToken(authToken.getAccountId(), token);
            } catch (AuthenticationPersistenceServiceException e) {
                log.warn("Couldn't delete used auth token " + token + " for " + authToken.getAccountId());
            }
        } else {
            //a single use token is gone anyway, tracking when it was used would only produce a write followed by a delete.
            updateLastUsed(token);
        }

        return authToken.getAccountId();
    }

    @Override
    public boolean canAuthenticateByEncryptedToken(String token) throws AuthenticationServiceException {
        AuthToken authToken;
        try {
            authToken = decrypt(token);
        } catch (RuntimeException e) {
            //not authentic, so not something this service issued - no database query needed to say no.
            return false;
        }

        if (authToken.isExpired())
            return false;

        try {
            return persistenceService.authTokenExists(token);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }

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

    /**
     * Decrypts the token and rejects it if it is not authentic. With one of the authenticated algorithms this is
     * a proof that the token was minted with the configured phrase, so a forged or manipulated token is rejected
     * here, before any persistence is touched. Anything which does not decrypt was not issued by this service and
     * is reported as not found - a caller can not tell a forgery from a revoked token, and does not need to.
     *
     * @param token the token to check.
     * @return the decrypted token.
     * @throws AuthTokenNotFoundException if the token is not authentic.
     */
    private AuthToken decryptOrReject(String token) throws AuthTokenNotFoundException {
        try {
            return decrypt(token);
        } catch (RuntimeException e) {
            //a failing tag check, a broken encoding or an algorithm shortcut which is not configured - all of
            //them mean the same thing here. The token itself is never logged, it is a secret.
            log.debug("Rejecting a token which can't be decrypted: " + e.getMessage());
            throw new AuthTokenNotFoundException();
        }
    }

    /**
     * Marks the given token as used now, unless the stored timestamp is younger than the configured interval.
     * Failing to write it is not a reason to fail the authentication which just succeeded, so an error is logged
     * and swallowed.
     *
     * @param token the token which was successfully authenticated with.
     */
    private void updateLastUsed(String token) {
        long now = System.currentTimeMillis();
        try {
            persistenceService.updateLastUsed(token, now, now - lastUsedUpdateIntervalInMillis);
        } catch (AuthenticationPersistenceServiceException e) {
            log.warn("Couldn't update the last used timestamp of an auth token", e);
        }
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


            persistenceService.saveAuthToken(accountId, encToken);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException(e);
        }


        return encToken;
    }

    @Override
    @Deprecated
    public EncryptedAuthToken saveEncryptedToken(AccountId accountId, AuthToken prefilledToken) throws AuthenticationServiceException {
        return generateEncryptedToken(accountId, prefilledToken);
    }

    @Override
    public List<TokenInventoryEntry> getTokenInventoryByAccount(AccountId accountId) throws AuthenticationServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("accountId can't be null");
        try {
            return persistenceService.getTokenInventoryByAccount(accountId);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException("Can't retrieve the token inventory of " + accountId, e);
        }
    }

    @Override
    public List<TokenInventoryEntry> getTokenInventoryByType(int type, int limit, int offset) throws AuthenticationServiceException {
        if (limit <= 0)
            throw new IllegalArgumentException("limit has to be greater than zero");
        if (offset < 0)
            throw new IllegalArgumentException("offset can't be negative");
        try {
            return persistenceService.getTokenInventoryByType(type, limit, offset);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException("Can't retrieve the token inventory of type " + type, e);
        }
    }

    @Override
    public void deleteTokens(AccountId accountId) throws AuthenticationServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");
        try {
            persistenceService.deleteAuthTokens(accountId);
        } catch (AuthenticationPersistenceServiceException e) {
            log.error("Couldn't delete auth tokens for " + accountId);
        }
    }

    @Override
    public void deleteUserData(AccountId accountId) {
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

    @Override
    public String describeData() {
        return "authenticationService";
    }

    @Override
    public void deleteTokensByType(AccountId accountId, int type) throws AuthenticationServiceException {
        Set<String> tokens = null;
        try {
            tokens = persistenceService.getAuthTokens(accountId);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException("Can't retrieve tokens for user " + accountId, e);
        }

        for (String token : tokens) {
            AuthToken authToken = decrypt(token);
            if (authToken.getType() == type) {
                try {
                    persistenceService.deleteAuthToken(accountId, token);
                } catch (AuthenticationPersistenceServiceException e) {
                    log.error("Can't delete token for user " + accountId + ", type " + type + ", token: " + token, e);
                }
            }
        }
    }

    @Override
    public void deleteToken(AccountId accountId, String token) throws AuthenticationServiceException {
        Set<String> tokens = null;
        try {
            tokens = persistenceService.getAuthTokens(accountId);
        } catch (AuthenticationPersistenceServiceException e) {
            throw new AuthenticationServiceException("Can't retrieve tokens for user " + accountId, e);
        }

        for (String tokenFromDB : tokens) {
            if (tokenFromDB.equals(token)) {
                try {
                    persistenceService.deleteAuthToken(accountId, token);
                } catch (AuthenticationPersistenceServiceException e) {
                    log.error("Can't delete token for user " + accountId + ", token: " + token, e);
                }
            }
        }
    }

    @Override
    public String getTokenByType(AccountId accountId, int type) throws AuthenticationServiceException {
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
