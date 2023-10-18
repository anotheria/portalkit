package net.anotheria.portalkit.services.personaldata.storage;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import net.anotheria.portalkit.services.personaldata.PersonalDataServiceConfig;
import net.anotheria.util.StringUtils;

import java.util.Collections;

/**
 * @author Vlad Lukjanenko
 */
public class MongoConnector {

    /**
     * {@link MongoConnector} instance.
     */
    private static MongoConnector INSTANCE = null;

    /**
     * {@link MongoClient} instance.
     */
    private MongoClient mongo;

    /**
     * Collection name.
     */
    private String databaseName;

    /**
     * {@link Datastore} instance.
     */
    private Datastore datastore;


    /**
     * Default constructor.
     */
    private MongoConnector() {
        PersonalDataServiceConfig config = PersonalDataServiceConfig.getInstance();

        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder();

        if (StringUtils.isEmpty(config.getConnectionString())) {
            settingsBuilder.applyToClusterSettings(builder -> {
                builder.hosts(Collections.singletonList(new ServerAddress(config.getHost(), config.getPort())));
            });
        } else {
            settingsBuilder.applyToClusterSettings(builder -> builder.applyConnectionString(new ConnectionString(config.getConnectionString())))
                    .applyToSslSettings(builder -> builder.enabled(true));
        }
        mongo = MongoClients.create(settingsBuilder.build());
        databaseName = config.getDatabase();
        datastore = Morphia.createDatastore(mongo, databaseName);
    }


    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Returns {@link MongoConnector} instance.
     */
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
     */
    public static Datastore getDatabase() {
        return getInstance().datastore;
    }
}
