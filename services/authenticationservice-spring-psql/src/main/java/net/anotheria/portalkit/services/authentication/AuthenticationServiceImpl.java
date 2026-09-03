package net.anotheria.portalkit.services.authentication;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.authentication.encryptors.BlowfishPasswordEncryptionAlgorithm;
import net.anotheria.portalkit.services.authentication.persistence.AuthTokenEntity;
import net.anotheria.portalkit.services.authentication.persistence.AuthTokenEntityRepository;
import net.anotheria.portalkit.services.authentication.persistence.OffsetPageable;
import net.anotheria.portalkit.services.authentication.persistence.PasswordEntity;
import net.anotheria.portalkit.services.authentication.persistence.PasswordEntityRepository;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the AuthenticationService.
 *
 * @author lrosenberg
 * @since 13.12.12 09:30
 */
@Monitor(subsystem = "authentication", category = "portalkit-service")
public class AuthenticationServiceImpl implements AuthenticationService, EntityManagingService {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    /**
     * The currently used password enhashing algorithm.
     */
    private final PasswordEncryptionAlgorithm passwordAlgorithm;
    /**
     * Encryption algorithm for accountId.
     */
    private final PasswordEncryptionAlgorithm accountIdAlgorithm;
    /**
     * Application secret.
     */
    private final static String APP_KEY = (char) 56 + 'x' + "8UJlqYBKS4dh";
    /**
     * {@link PasswordEntityRepository} instance.
     */
    private final PasswordEntityRepository passwordEntityRepository;
    /**
     * {@link AuthTokenEntityRepository} instance.
     */
    private final AuthTokenEntityRepository authTokenEntityRepository;
    /**
     * How old the stored last used timestamp of a token has to be before it is written again, in millis. See
     * {@link AuthenticationServiceConfig#getLastUsedUpdateIntervalInHours()}.
     */
    private final long lastUsedUpdateIntervalInMillis;

    /**
     * Default constructor.
     */
    public AuthenticationServiceImpl(PasswordEntityRepository passwordEntityRepository, AuthTokenEntityRepository authTokenEntityRepository) {
        this.passwordEntityRepository = passwordEntityRepository;
        this.authTokenEntityRepository = authTokenEntityRepository;

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
        lastUsedUpdateIntervalInMillis = config.getLastUsedUpdateIntervalInHours() * 60L * 60L * 1000L;
        EntityManagingServices.createEntityCounter(this, "AuthTokens");
        EntityManagingServices.createEntityCounter(this, "AuthPasswords");
    }

    @Override
    public int getEntityCount(String s) {
        switch (s) {
            case "AuthTokens":
                try {
                    return authTokenEntityRepository.findAllIds().size();
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return 0;
                }
            case "AuthPasswords":
                try {
                    return passwordEntityRepository.findAllIds().size();
                }  catch (Exception e) {
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
        if (StringUtils.isEmpty(password))
            throw new IllegalArgumentException("password can't be empty");

        try {
            PasswordEntity passwordEntity = new PasswordEntity();
            passwordEntity.setAccountId(getEncrypted(id).getInternalId());
            passwordEntity.setPassword(passwordAlgorithm.encryptPassword(password));
            passwordEntityRepository.save(passwordEntity);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to save passwor for user: " + id, e);
        }
    }

    @Override
    public boolean canAuthenticate(AccountId id, String password) throws AuthenticationServiceException {
        if (id == null)
            throw new IllegalArgumentException("id can't be null");
        if (StringUtils.isEmpty(password))
            throw new IllegalArgumentException("password can't be empty");

        try {
            PasswordEntity storedEncryptedPassword = passwordEntityRepository.findById(getEncrypted(id).getInternalId()).orElse(null);
            return storedEncryptedPassword != null && !StringUtils.isEmpty(storedEncryptedPassword.getPassword())
                    && storedEncryptedPassword.getPassword().equals(passwordAlgorithm.encryptPassword(password));
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to check password for user: " + id, e);
        }
    }

    @Override
    public AccountId authenticateByEncryptedToken(String token) throws AuthenticationServiceException {
        if (StringUtils.isEmpty(token))
            throw new IllegalArgumentException("token can't be empty");

        if (!authTokenEntityRepository.existsById(token))
            throw new AuthTokenNotFoundException();

        AuthToken authToken = decrypt(token);
        if (authToken.isExpired())
            throw new AuthTokenExpiredException();

        if (!authToken.isMultiUse()) {
            try {
                authTokenEntityRepository.deleteById(token);
            } catch (Exception e) {
                log.warn("Couldn't delete used auth token {} for {}", token,  authToken.getAccountId());
            }
        } else {
            //a single use token is gone anyway, tracking when it was used would only produce a write followed by a delete.
            updateLastUsed(token);
        }

        return authToken.getAccountId();
    }

    @Override
    public boolean canAuthenticateByEncryptedToken(String token) throws AuthenticationServiceException {
        if (StringUtils.isEmpty(token))
            throw new IllegalArgumentException("token can't be empty");

        try {
            if (!authTokenEntityRepository.existsById(token))
                return false;

            AuthToken authToken = decrypt(token);
            return !authToken.isExpired();
        } catch (Exception e) {
            log.warn("Unable to authenticate by encrypted token. {}", e.getMessage());
            throw new AuthenticationServiceException("Unable to authenticate by encrypted token password for user: " + token, e);
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
     * Marks the given token as used now, unless the stored timestamp is younger than the configured interval.
     * Failing to write it is not a reason to fail the authentication which just succeeded, so an error is logged
     * and swallowed.
     *
     * @param token the token which was successfully authenticated with.
     */
    private void updateLastUsed(String token) {
        long now = System.currentTimeMillis();
        try {
            authTokenEntityRepository.updateLastUsed(token, now, now - lastUsedUpdateIntervalInMillis);
        } catch (Exception e) {
            log.warn("Couldn't update the last used timestamp of an auth token", e);
        }
    }

    /**
     * Maps a stored token to an inventory entry.
     *
     * @param entity    the stored token.
     * @param accountId the account id to report, which is the plain one where the caller supplied it and the
     *                  stored, encrypted one otherwise.
     * @return the inventory entry.
     */
    private TokenInventoryEntry toInventoryEntry(AuthTokenEntity entity, AccountId accountId) {
        long created = entity.getDaoCreated() == null ? TokenInventoryEntry.TIMESTAMP_UNKNOWN : entity.getDaoCreated();
        long lastUsed = entity.getLastUsedAt() == null ? TokenInventoryEntry.TIMESTAMP_UNKNOWN : entity.getLastUsedAt();

        return new TokenInventoryEntry(accountId, entity.getType(), TokenObfuscator.obfuscate(entity.getToken()),
                created, lastUsed, entity.getExpiryTimestamp(), entity.isMultiUse(), entity.isExclusive(),
                entity.isExclusiveInType());
    }

    @Override
    public List<TokenInventoryEntry> getTokenInventoryByAccount(AccountId accountId) throws AuthenticationServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("accountId can't be null");
        try {
            //tokens are stored with the plain account id here (AuthToken.toEntity uses it), the same form
            //getTokenByType and deleteTokensByType query with. See the note on getEncrypted usage in this class.
            List<AuthTokenEntity> stored = authTokenEntityRepository.findByAccountId(accountId.getInternalId());
            List<TokenInventoryEntry> ret = new ArrayList<>(stored.size());
            for (AuthTokenEntity entity : stored)
                ret.add(toInventoryEntry(entity, accountId));
            return ret;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to retrieve the token inventory of " + accountId, e);
        }
    }

    @Override
    public List<TokenInventoryEntry> getTokenInventoryByType(int type, int limit, int offset) throws AuthenticationServiceException {
        if (limit <= 0)
            throw new IllegalArgumentException("limit has to be greater than zero");
        if (offset < 0)
            throw new IllegalArgumentException("offset can't be negative");
        try {
            List<AuthTokenEntity> stored = authTokenEntityRepository.findByType(type,
                    new OffsetPageable(offset, limit, Sort.by("token")));
            List<TokenInventoryEntry> ret = new ArrayList<>(stored.size());
            for (AuthTokenEntity entity : stored)
                ret.add(toInventoryEntry(entity, new AccountId(entity.getAccountId())));
            return ret;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to retrieve the token inventory of type " + type, e);
        }
    }


    @Override
    public EncryptedAuthToken generateEncryptedToken(AuthToken prefilledToken) throws AuthenticationServiceException {

        AuthToken newToken = (AuthToken) prefilledToken.clone();
        String encryption = AuthTokenEncryptors.encrypt(newToken);

        EncryptedAuthToken encToken = new EncryptedAuthToken();
        encToken.setAuthToken(newToken);
        encToken.setEncryptedVersion(encryption);

        AccountId encryptedAccountId = getEncrypted(newToken.getAccountId());
        try {
            if (newToken.isExclusive())
                authTokenEntityRepository.deleteByAccountId(encryptedAccountId.getInternalId());

            if (!newToken.isExclusive() && newToken.isExclusiveInType())
                authTokenEntityRepository.deleteByAccountIdAndType(encryptedAccountId.getInternalId(), newToken.getType());

            AuthTokenEntity entity = newToken.toEntity();
            entity.setToken(encryption);
            authTokenEntityRepository.save(entity);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to generate encrypted token for id: " + newToken.getAccountId(), e);
        }
        return encToken;
    }


    @Override
    public void deleteTokens(AccountId accountId) throws AuthenticationServiceException {
        try {
            authTokenEntityRepository.deleteByAccountId(getEncrypted(accountId).getInternalId());
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to delete tokens for account: " + accountId, e);
        }
    }

    @Override
    public void deleteUserData(AccountId accountId) {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");

        AccountId encrypted = getEncrypted(accountId);
        try {
            authTokenEntityRepository.deleteByAccountId(encrypted.getInternalId());
        } catch (Exception e) {
            log.error("Unable to delete auth tokens for account: {}", accountId, e);
        }
        try {
            passwordEntityRepository.deleteById(encrypted.getInternalId());
        } catch (Exception e) {
            log.error("Unable to delete password for account: {}", accountId, e);
        }
    }

    @Override
    public String describeData() {
        return "authenticationService";
    }


    @Override
    public void deleteTokensByType(AccountId accountId, int type) throws AuthenticationServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");

        try {
            authTokenEntityRepository.deleteByAccountIdAndType(accountId.getUUID().toString(), type);
        } catch (Exception e) {
            log.error("Unable to delete auth tokens for account: {} and type {}", accountId, type, e);
            throw new AuthenticationServiceException("Unable to delete tokens for account: " + accountId + " and type: " + type, e);
        }
    }

    @Override
    public void deleteToken(String token) throws AuthenticationServiceException {
        if (StringUtils.isEmpty(token))
            throw new IllegalArgumentException("token can't be empty");


        try {
            authTokenEntityRepository.deleteById(token);
        } catch (Exception e) {
            log.error("Unable to delete auth token.", e);
        }
    }

    @Override
    public String getTokenByType(AccountId accountId, int type) throws AuthenticationServiceException {
        if  (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");

        try {
            List<AuthTokenEntity> tokens = authTokenEntityRepository.findTokensByAccountIdAndType(accountId.getUUID().toString(), type);
            if (tokens.size() > 1)
                log.warn("Multiple auth tokens for account: {} and type: {}", accountId, type);

            return tokens.get(0).getToken();
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to get auth token for account: " + accountId, e);

        }
    }

    /**
     * Returns new instance of encrypted version of AccountId.
     *
     * @param id to encrypt
     * @return encrypted account id
     */
    private AccountId getEncrypted(AccountId id) {
        return new AccountId(accountIdAlgorithm.encryptPassword(id.getInternalId()));
    }
}
