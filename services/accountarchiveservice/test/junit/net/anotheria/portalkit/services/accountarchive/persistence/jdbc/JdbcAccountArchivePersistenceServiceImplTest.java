package net.anotheria.portalkit.services.accountarchive.persistence.jdbc;

import net.anotheria.portalkit.services.accountarchive.persistence.ArchivedAccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;

import java.sql.SQLException;

/**
 * @author VKoulakov
 * @since 24.04.14 12:24
 */
public abstract class JdbcAccountArchivePersistenceServiceImplTest {
    protected JdbcAccountArchivePersistenceServiceImpl getService(String env) throws ArchivedAccountPersistenceServiceException {
        ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", env));
        JdbcAccountArchivePersistenceServiceImpl service = new JdbcAccountArchivePersistenceServiceImpl();
        try {
            service.cleanupFromUnitTests();
        } catch (SQLException e) {
            throw new ArchivedAccountPersistenceServiceException(e);
        } catch (DAOException e) {
            throw new ArchivedAccountPersistenceServiceException(e);
        }
        return service;
    }
}
