package net.anotheria.portalkit.services.online.persistence.inmemory;

import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.PersistenceTestScenario;

import java.util.Arrays;
import java.util.List;

/**
 * Junit test for InMemory persistence.
 *
 * @author h3llka
 */
public class InMemoryActivityPersistenceServiceTest extends PersistenceTestScenario {


    @Override
    protected List<String> getEnvironments() {
        return Arrays.asList("any-env-for-this-test");
    }

    @Override
    protected ActivityPersistenceService getService(String environment) {
        return new InMemoryActivityPersistenceServiceImpl();
    }


}


