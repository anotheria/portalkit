package net.anotheria.portalkit.services.common.persistence.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.configureme.ConfigurationManager;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Base mongo persistence service
 *
 * Created by Roman Stetsiuk on 7/1/16.
 */
public abstract class BaseMongoPersistenceServiceImpl implements BaseMongoPersistenceService {
    private MongoClient mongoClient;
    private MongoConnectorConfig config;
    private Morphia morphia;


    /**
     * Name of the configuration.
     */
    private String configName;

    protected BaseMongoPersistenceServiceImpl() {
        this(null);
    }

    protected BaseMongoPersistenceServiceImpl(String aConfigName) {
        configName = aConfigName;
        init();
    }

    /**
     * Initialize data source.
     */
    @Override
    public void init() {
        mongoClient = configure();
        morphia = new Morphia();
        morphia.mapPackage("net.anotheria.portalkit.services.common.persistence.mongo.entities");
        connect().ensureIndexes();
    }

    protected Datastore connect() {
        return morphia.createDatastore(mongoClient, config.getDbName());
    }

    private MongoClient configure() {
        if (configName == null || configName.isEmpty())
            throw new IllegalStateException("Config not set");

        config = new MongoConnectorConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configName);
        MongoClientURI uri = new MongoClientURI(config.getUri());

        return new MongoClient(uri);
    }

}
