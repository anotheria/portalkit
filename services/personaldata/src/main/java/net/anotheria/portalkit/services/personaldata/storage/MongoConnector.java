package net.anotheria.portalkit.services.personaldata.storage;

import com.mongodb.MongoClient;
import net.anotheria.portalkit.services.personaldata.PersonalDataServiceConfig;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * @author Vlad Lukjanenko
 */
public class MongoConnector {

    /**
     * {@link MongoConnector} instance.
     * */
    private static MongoConnector INSTANCE = null;

    /**
     * {@link MongoClient} instance.
     * */
    private MongoClient mongo;

    /**
     * {@link Morphia} instance.
     * */
    private Morphia morphia;

    /**
     * Collection name.
     * */
    private String databaseName;

    /**
     * {@link Datastore} instance.
     * */
    private Datastore datastore;


    /**
     * Default constructor.
     * */
    private MongoConnector() {

        PersonalDataServiceConfig config = PersonalDataServiceConfig.getInstance();

        mongo = new MongoClient(config.getHost(), config.getPort());
        morphia = new Morphia();
        morphia.mapPackage("net.anotheria.portalkit.services.personaldata");
        databaseName = config.getDatabase();
        datastore = morphia.createDatastore(mongo, databaseName);
        datastore.ensureIndexes();
    }


    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Returns {@link MongoConnector} instance.
     * */
    private static MongoConnector getInstance() {

        if (INSTANCE == null) {

            synchronized (MongoConnector.class) {

                if (INSTANCE == null) {
                    INSTANCE = new MongoConnector();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * Returns {@link Datastore} instance.
     * */
    public static Datastore getDatabase() {
        return getInstance().datastore;
    }
}
