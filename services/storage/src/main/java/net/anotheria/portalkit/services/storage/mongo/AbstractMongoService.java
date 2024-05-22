package net.anotheria.portalkit.services.storage.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.anotheria.portalkit.services.storage.exception.StorageRuntimeException;
import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract storage service implementation for Mongo.
 * 
 * @author Alexandr Bolbat
 */
public abstract class AbstractMongoService {

	/**
	 * {@link Logger} instance.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractMongoService.class);

	/**
	 * Service initialization status.
	 */
	private final AtomicBoolean serviceInitialized = new AtomicBoolean(false);

	/**
	 * Mongo client initialization status.
	 */
	private final AtomicBoolean mongoClientInitialized = new AtomicBoolean(false);

	/**
	 * {@link AbstractMongoServiceConfig} instance.
	 */
	private final AbstractMongoServiceConfig configuration;

	/**
	 * {@link MongoClientConfig} instance.
	 */
	private final MongoClientConfig mongoClientConfiguration;

	/**
	 * {@link MongoClient} instance.
	 */
	private MongoClient mongoClient;

	/**
	 * Default constructor.
	 * 
	 * @param conf
	 *            abstract service configuration name, can be <code>null</code>
	 * @param clientConf
	 *            mongo client configuration name, can be <code>null</code>
	 * @param env
	 *            configuration environment, can be <code>null</code>
	 */
	protected AbstractMongoService(final String conf, final String clientConf, final String env) {
		this.configuration = AbstractMongoServiceConfig.getInstance(conf, env);
		this.mongoClientConfiguration = MongoClientConfig.getInstance(clientConf, env);
	}

	/**
	 * General service initialization.
	 */
	protected synchronized void initialize() {
		if (serviceInitialized.get())
			return;
		MongoClientOptions options = MongoClientUtil.getOptions(mongoClientConfiguration);
		MongoClientSettings settings;

		if (StringUtils.isEmpty(mongoClientConfiguration.getConnectionString())) {
			settings = MongoClientSettings.builder()
					.applyToClusterSettings(builder -> builder.hosts(MongoClientUtil.getAddresses(mongoClientConfiguration)))
					.applyToSocketSettings(builder -> {
						builder.connectTimeout(options.getConnectTimeout(), TimeUnit.MILLISECONDS);
						builder.readTimeout(options.getSocketTimeout(), TimeUnit.MILLISECONDS);
					})
					.applyToConnectionPoolSettings(builder -> builder.maxSize(options.getConnectionsPerHost()))
					.readPreference(options.getReadPreference())
					.writeConcern(options.getWriteConcern())
					.build();
		} else {
			settings = MongoClientSettings.builder()
					.applyConnectionString(new ConnectionString(mongoClientConfiguration.getConnectionString()))
					.applyToSslSettings(builder -> builder.enabled(true))
					.applyToSocketSettings(builder -> {
						builder.connectTimeout(options.getConnectTimeout(), TimeUnit.MILLISECONDS);
						builder.readTimeout(options.getSocketTimeout(), TimeUnit.MILLISECONDS);
					})
					.applyToConnectionPoolSettings(builder -> builder.maxSize(options.getConnectionsPerHost()))
					.readPreference(options.getReadPreference())
					.writeConcern(options.getWriteConcern())
					.build();
		}
		mongoClient = MongoClients.create(settings);

		Thread initializationVerifier = new Thread(() -> {
			mongoClient.listDatabaseNames(); // now this thread will wait mongo client if it initialization still in progress
            mongoClientInitialized.set(true);
        });
		initializationVerifier.start();

		long initStartTime = System.currentTimeMillis();
		while (!mongoClientInitialized.get())
			try {
				Thread.sleep(configuration.getInitWaitInterval());

				if (System.currentTimeMillis() > initStartTime + configuration.getInitMaxTime()) {
					initializationVerifier.interrupt();
					mongoClient.close();
					throw new StorageRuntimeException("Can't initialize Mongo client.");
				}
			} catch (InterruptedException e) {
				initializationVerifier.interrupt();
				mongoClient.close();
			}

		// performing authentication if required
//		DB database = mongoClientConfiguration.getDatabase(getDBName());
//		if (database.isAuthenticate()) {
//			boolean authenticated = mongoClient.getDB(getDBName()).authenticate(database.getUsername(), database.getPassword().toCharArray());
//			if (!authenticated)
//				throw new StorageRuntimeException("Can't authenticate for database[" + database.getName() + "] with username[" + database.getUsername() + "].");
//		}

		serviceInitialized.set(true);
	}

	protected MongoClientConfig getMongoClientConfiguration() {
		return mongoClientConfiguration;
	}

	/**
	 * Get {@link MongoClient}.
	 * 
	 * @return {@link MongoClient}
	 */
	protected MongoClient getMongoClient() {
		if (!serviceInitialized.get())
			throw new StorageRuntimeException("Mongo client not initialized.");

		return mongoClient;
	}

	/**
	 * Return database name for current service instance.
	 * 
	 * @return {@link String}
	 */
	protected abstract String getDBName();

}
