package net.anotheria.portalkit.services.pushtoken.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceService;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceServiceException;

import java.util.List;

/**
 * JDBC implementation of PushTokenPersistenceService.
 */
public class JDBCPushTokenPersistenceServiceImpl implements PushTokenPersistenceService {

    @Override
    public List<String> getAllTokensByAccountId(AccountId accountId) throws PushTokenPersistenceServiceException {
        return null;
    }

    @Override
    public String saveTokenForAccount(AccountId accountId, String token) throws PushTokenPersistenceServiceException {
        return null;
    }

    @Override
    public AccountId deleteToken(String token) throws PushTokenPersistenceServiceException {
        return null;
    }
}
