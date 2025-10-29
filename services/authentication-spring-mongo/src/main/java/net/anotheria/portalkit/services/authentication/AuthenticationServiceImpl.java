package net.anotheria.portalkit.services.authentication;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.authentication.encryptors.BlowfishPasswordEncryptionAlgorithm;
import net.anotheria.portalkit.services.authentication.persistence.AuthTokenEntity;
import net.anotheria.portalkit.services.authentication.persistence.AuthTokenEntityRepository;
import net.anotheria.portalkit.services.authentication.persistence.PasswordEntity;
import net.anotheria.portalkit.services.authentication.persistence.PasswordEntityRepository;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.integrity.IntegrityCheckHelper;
import net.anotheria.portalkit.services.common.integrity.IntegrityCheckResult;
import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private PasswordEntityRepository passwordEntityRepository;
    /**
     * {@link AuthTokenEntityRepository} instance.
     */
    private AuthTokenEntityRepository authTokenEntityRepository;


    /**
     * Default constructor.
     */
    public AuthenticationServiceImpl() {
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
        EntityManagingServices.createEntityCounter(this, "AuthTokens");
        EntityManagingServices.createEntityCounter(this, "AuthPasswords");
    }

    public void setPasswordEntityRepository(PasswordEntityRepository passwordEntityRepository) {
        this.passwordEntityRepository = passwordEntityRepository;
    }

    public void setAuthTokenEntityRepository(AuthTokenEntityRepository authTokenEntityRepository) {
        this.authTokenEntityRepository = authTokenEntityRepository;
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
            String storedEncryptedPassword = passwordEntityRepository.findPasswordByAccountId(getEncrypted(id).getInternalId()).orElse(null);
            return storedEncryptedPassword != null && storedEncryptedPassword.equals(passwordAlgorithm.encryptPassword(password));
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to check password for user: " + id, e);
        }
    }

    @Override
    public AccountId authenticateByEncryptedToken(String token) throws AuthenticationServiceException {
        if (StringUtils.isEmpty(token))
            throw new IllegalArgumentException("token can't be empty");

        if (!authTokenEntityRepository.existsByToken(token))
            throw new AuthTokenNotFoundException();

        AuthToken authToken = decrypt(token);
        if (authToken.isExpired())
            throw new AuthTokenExpiredException();

        if (!authToken.isMultiUse()) {
            try {
                authTokenEntityRepository.deleteByToken(token);
            } catch (Exception e) {
                log.warn("Couldn't delete used auth token {} for {}", token,  authToken.getAccountId());
            }
        }

        return authToken.getAccountId();
    }

    @Override
    public boolean canAuthenticateByEncryptedToken(String token) throws AuthenticationServiceException {
        if (StringUtils.isEmpty(token))
            throw new IllegalArgumentException("token can't be empty");

        try {
            if (!authTokenEntityRepository.existsByToken(token))
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
            passwordEntityRepository.deleteByAccountId(encrypted.getInternalId());
        } catch (Exception e) {
            log.error("Unable to delete password for account: {}", accountId, e);
        }
    }

    @Override
    public String describeData() {
        return "authenticationService";
    }

    @Override
    public IntegrityCheckResult performIntegrityCheck(IntegrityCheckHelper helper) throws Exception {
        //TODO. Please, implement me
        return null;
    }

    @Override
    public void deleteTokensByType(AccountId accountId, int type) throws AuthenticationServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");

        try {
            authTokenEntityRepository.deleteByAccountIdAndType(getEncrypted(accountId).getInternalId(), type);
        } catch (Exception e) {
            log.error("Unable to delete auth tokens for account: {} and type {}", accountId, type, e);
            throw new AuthenticationServiceException("Unable to delete tokens for account: " + accountId + " and type: " + type, e);
        }
    }

    @Override
    public void deleteToken(AccountId accountId, String token) throws AuthenticationServiceException {
        if (StringUtils.isEmpty(token))
            throw new IllegalArgumentException("token can't be empty");
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");

        try {
            authTokenEntityRepository.deleteByAccountIdAndToken(getEncrypted(accountId).getInternalId(), token);
        } catch (Exception e) {
            log.error("Unable to delete auth token for account: {}", accountId, e);
        }
    }

    @Override
    public String getTokenByType(AccountId accountId, int type) throws AuthenticationServiceException {
        if  (accountId == null)
            throw new IllegalArgumentException("Incoming accountId is NULL.");

        try {
            List<String> tokens = authTokenEntityRepository.findTokensByAccountIdAndType(getEncrypted(accountId).getInternalId(), type);
            if (tokens.size() > 1)
                log.warn("Multiple auth tokens for account: {} and type: {}", accountId, type);

            return tokens.get(0);
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
