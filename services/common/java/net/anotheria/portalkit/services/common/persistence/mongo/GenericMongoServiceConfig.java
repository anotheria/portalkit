package net.anotheria.portalkit.services.common.persistence.mongo;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.Environment;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.configureme.environments.DynamicEnvironment;
import org.configureme.sources.ConfigurationSourceKey.Format;

/**
 * {@link GenericMongoService} configuration.
 * 
 * @author Alexandr Bolbat
 * 
 */
@ConfigureMe(name = "pk-mongo-generic-service-config", allfields = false)
public final class GenericMongoServiceConfig implements Serializable {

	/**
	 * Generated SerialVersionUID.
	 */
	@DontConfigure
	private static final long serialVersionUID = -1137186520917278525L;

	/**
	 * {@link Logger} instance.
	 */
	@DontConfigure
	private static final Logger LOGGER = Logger.getLogger(GenericMongoServiceConfig.class);

	/**
	 * Synchronization object.
	 */
	@DontConfigure
	private static final Object LOCK = new Object();

	/**
	 * {@link GenericMongoServiceConfig} instance
	 */
	@DontConfigure
	private static GenericMongoServiceConfig instance;

	/**
	 * Database name.
	 */
	@Configure
	private String databaseName;

	/**
	 * Collection name.
	 */
	@Configure
	private String collectionName;

	/**
	 * Entity key field name.
	 */
	@Configure
	private String entityKeyFieldName;

	/**
	 * Default constructor.
	 * 
	 * @param configuration
	 *            configuration name
	 * @param environment
	 *            configuration environment
	 */
	private GenericMongoServiceConfig(final String configuration, final Environment environment) {
		try {
			if (configuration == null || configuration.trim().isEmpty()) {
				ConfigurationManager.INSTANCE.configure(this, environment);
			} else {
				ConfigurationManager.INSTANCE.configureAs(this, environment, configuration, Format.JSON);
			}
		} catch (RuntimeException e) {
			LOGGER.warn("GenericMongoServiceConfig(conf:" + configuration + ", env: " + environment + ") Configuration fail[" + e.getMessage()
					+ "]. Relaying on defaults.");
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(e);

		}

		LOGGER.info("GenericMongoServiceConfig(conf:" + configuration + ", env: " + environment + ") Configured with[" + this.toString() + "]");
	}

	/**
	 * Get configured instance of {@link GenericMongoServiceConfig}.
	 * 
	 * @return {@link GenericMongoServiceConfig}
	 */
	public static GenericMongoServiceConfig getInstance() {
		return getInstanceForConfiguration(null);
	}

	/**
	 * Get configured instance of {@link GenericMongoServiceConfig}.
	 * 
	 * @param configuration
	 *            configuration name, can be <code>null</code> or empty
	 * @return {@link GenericMongoServiceConfig}
	 */
	public static GenericMongoServiceConfig getInstanceForConfiguration(final String configuration) {
		return getInstance(configuration, null);
	}

	/**
	 * Get configured instance of {@link GenericMongoServiceConfig}.
	 * 
	 * @param environment
	 *            environment name, can be <code>null</code> or empty
	 * @return {@link GenericMongoServiceConfig}
	 */
	public static GenericMongoServiceConfig getInstanceForEnvironment(final String environment) {
		return getInstance(null, environment);
	}

	/**
	 * get configured instance of {@link GenericMongoServiceConfig}
	 * 
	 * @param configuration
	 *            configuration name, can be <code>null</code> or empty
	 * @param environment
	 *            environment name, can be <code>null</code> or empty
	 * @return {@link GenericMongoServiceConfig}
	 */
	public static GenericMongoServiceConfig getInstance(final String configuration, final String environment) {
		if ((configuration == null || configuration.trim().isEmpty()) && (environment == null || environment.trim().isEmpty())) {
			if (instance != null)
				return instance;

			synchronized (LOCK) {
				if (instance == null)
					instance = new GenericMongoServiceConfig(null, ConfigurationManager.INSTANCE.getDefaultEnvironment());
			}

			return instance;
		}

		Environment env = ConfigurationManager.INSTANCE.getDefaultEnvironment();
		if (environment != null && !environment.trim().isEmpty())
			env = DynamicEnvironment.parse(environment);

		return new GenericMongoServiceConfig(configuration, env);
	}

	public String getDatabaseName() {
		return databaseName != null ? databaseName : "";
	}

	public void setDatabaseName(final String aDatabaseName) {
		this.databaseName = aDatabaseName;
	}

	public String getCollectionName() {
		return collectionName != null ? collectionName : "";
	}

	public void setCollectionName(final String aCollectionName) {
		this.collectionName = aCollectionName;
	}

	public String getEntityKeyFieldName() {
		return entityKeyFieldName != null ? entityKeyFieldName : "";
	}

	public void setEntityKeyFieldName(final String aEntityKeyFieldName) {
		this.entityKeyFieldName = aEntityKeyFieldName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tGenericMongoServiceConfig [\n\t databaseName=");
		builder.append(databaseName);
		builder.append(", collectionName=");
		builder.append(collectionName);
		builder.append(", entityKeyFieldName=");
		builder.append(entityKeyFieldName);
		builder.append("\n\t]\n");
		return builder.toString();
	}

}
