package net.anotheria.portalkit.services.online.persistence.jdbc;

import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.PersistenceTestScenario;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Ignore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Junit test for JDBC persistence.
 *
 * @author h3llka
 */
@Ignore
public class JDBCActivityPersistenceServiceTest extends PersistenceTestScenario {

    private static final List<String> environments;

    static {
        environments = new ArrayList<String>();
        environments.add("h2");
        environments.add("hsqldb");
        //NOTE - only for local usage and check!
        //environments.add("psql");
    }


    @Override
    protected List<String> getEnvironments() {
        return environments;
    }


    @Override
    protected ActivityPersistenceService getService(String environment) {
        ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", environment));
        JDBCActivityPersistenceServiceImpl service = new JDBCActivityPersistenceServiceImpl();
        try {
            service.cleanupFromUnitTests();
            return service;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
