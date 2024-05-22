package net.anotheria.portalkit.services.common.persistence.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.configureme.ConfigurationManager;

/**
 * Base mongo persistence service
 * <p>
 * Created by Roman Stetsiuk on 7/1/16.
 */
public abstract class BaseMongoPersistenceServiceImpl implements BaseMongoPersistenceService {
    private MongoClient mongoClient;
    private MongoConnectorConfig config;

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
        connect();
    }

    protected Datastore connect() {
        return Morphia.createDatastore(mongoClient, config.getDbName());
    }

    private MongoClient configure() {
        if (configName == null || configName.isEmpty())
            throw new IllegalStateException("Config not set");

        config = new MongoConnectorConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configName);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.applyConnectionString(new ConnectionString(config.getUri())))
                .applyToSslSettings(builder -> builder.enabled(true))
                .build();

        return MongoClients.create(clientSettings);
    }

}
