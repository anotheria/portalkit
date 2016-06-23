package net.anotheria.portalkit.services.common.persistence.mongo;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.configureme.ConfigurationManager;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MongoConnector {
    INSTANCE;

    private final Logger log;

    private final MongoClient mongoClient;

    private MongoConnectorConfig config;

    private Morphia morphia;

    MongoConnector() {
        log = LoggerFactory.getLogger(MongoConnector.class);
        mongoClient = configure();
        morphia = new Morphia();
        morphia.mapPackage("net.anotheria.portalkit.services.common.persistence.mongo.entities");
        connect().ensureIndexes();
    }

    public Datastore connect() {
        return morphia.createDatastore(mongoClient, config.getDbName());
    }

    private MongoClient configure() {

        config = new MongoConnectorConfig();

        try {
            ConfigurationManager.INSTANCE.configure(config);
            log.info("Configured MongoService with " + config.toString());
        } catch (IllegalArgumentException e) {
            log.error("Configuration not found, working with default configuration for localhost Mongo server");
        }

//        MongoCredential credential = MongoCredential.createCredential(config.getLogin(), config.getDbName(), config.getPassword().toCharArray());

        return new MongoClient(new ServerAddress(config.getHost(), Integer.parseInt(config.getPort())));
    }

}
