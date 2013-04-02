package net.anotheria.portalkit.services.online.persistence.storagebased;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.PersistenceTestScenario;
import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.StorageServiceFactory;
import net.anotheria.portalkit.services.storage.inmemory.GenericInMemoryServiceFactory;
import net.anotheria.portalkit.services.storage.type.StorageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Storage based persistence test!
 *
 * @author h3llka
 */
public class SBActivityPersistenceServiceImplTest extends PersistenceTestScenario {

    /**
     * Inmemory env.
     */
    private static final String INMEMORY = "inmemory";
    /**
     * Mongo env.
     */
    private static final String MONGO = "mongo";
    /**
     * Env stuff.
     */
    private static final List<String> environments;

    static {
        environments = new ArrayList<String>();
        environments.add(INMEMORY);
        //environments.add(MONGO); commented out ! reuires up and running MONGO!
    }


    @Override
    protected List<String> getEnvironments() {
        return environments;
    }


    @Override
    protected ActivityPersistenceService getService(String environment) {

        MetaFactory.reset();
        TestingContextStuff context = TestingContextStuff.getByValue(environment);

        Map<String, Serializable> factoryParameters = new HashMap<String, Serializable>();

        factoryParameters.put(GenericInMemoryServiceFactory.PARAMETER_ENTITY_KEY_FIELD_NAME, SBActivityPersistenceConstants.ACTIVITY_PERSISTENCE_GENERIC_STORAGE_KEY_FIELD_NAME);

        switch (context) {
            case INMEMORY_ENV:
                factoryParameters.put(StorageServiceFactory.PARAMETER_STORAGE_TYPE, StorageType.IN_MEMORY_GENERIC);
                break;
            case MONGO_ENV:
                factoryParameters.put(StorageServiceFactory.PARAMETER_STORAGE_TYPE, StorageType.NO_SQL_MONGO_GENERIC);
                break;
        }
        MetaFactory.addParameterizedFactoryClass(StorageService.class, SBActivityPersistenceConstants.ACTIVITY_PERSISTENCE_GENERIC_STORAGE_NAME, StorageServiceFactory.class, factoryParameters);


        return new SBActivityPersistenceServiceFactory().create();
    }

    /**
     * Context init selectors.
     */
    private static enum TestingContextStuff {
        //FOr inmemory persistence ussage
        INMEMORY_ENV(INMEMORY),
        // Mongo ussage
        MONGO_ENV(MONGO);

        private String value;

        TestingContextStuff(final String constant) {
            this.value = constant;
        }

        public static TestingContextStuff getByValue(final String value) {
            for (TestingContextStuff var : values())
                if (var.value.equals(value))
                    return var;
            return INMEMORY_ENV; // as Default.

        }
    }
}
