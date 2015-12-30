package net.anotheria.portalkit.services.storage.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.anotheria.portalkit.services.storage.mongo.index.Index;

import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.Environment;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.configureme.environments.DynamicEnvironment;
import org.configureme.sources.ConfigurationSourceKey.Format;

/**
 * {@link GenericMongoServiceImpl} configuration.
 * 
 * @author Alexandr Bolbat
 * 
 */
@ConfigureMe(name = "pk-storage-mongo-generic-service-config", allfields = false)
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
	 * {@link GenericMongoServiceConfig} instance.
	 */
	@DontConfigure
	private static volatile GenericMongoServiceConfig instance;

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
	 * Initialize indexes during service initialization.
	 */
	@Configure
	private boolean initializeIndexes = false;

	/**
	 * Collection indexes.
	 */
	@Configure
	private Index[] indexes;

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
	 * Get configured instance of {@link GenericMongoServiceConfig}.
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

	public boolean isInitializeIndexes() {
		return initializeIndexes;
	}

	public void setInitializeIndexes(final boolean aInitializeIndexes) {
		this.initializeIndexes = aInitializeIndexes;
	}

	/**
	 * Get indexes configuration.
	 * 
	 * @return {@link List} of {@link Index}
	 */
	public List<Index> getIndexes() {
		return indexes != null ? Arrays.asList(indexes) : new ArrayList<Index>();
	}

	/**
	 * Set indexes configuration.
	 * 
	 * @param aIndexes
	 *            indexes configuration
	 */
	public void setHosts(final List<Index> aIndexes) {
		this.indexes = aIndexes != null ? aIndexes.toArray(new Index[aIndexes.size()]) : null;
	}

	public void setIndexes(final Index[] aIndexes) {
		this.indexes = aIndexes != null ? aIndexes.clone() : null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collectionName == null) ? 0 : collectionName.hashCode());
		result = prime * result + ((databaseName == null) ? 0 : databaseName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericMongoServiceConfig other = (GenericMongoServiceConfig) obj;
		if (collectionName == null) {
			if (other.collectionName != null)
				return false;
		} else if (!collectionName.equals(other.collectionName))
			return false;
		if (databaseName == null) {
			if (other.databaseName != null)
				return false;
		} else if (!databaseName.equals(other.databaseName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("\n\t" + this.getClass().getSimpleName() + "[\n\t\t");
		builder.append("databaseName=").append(databaseName).append(",\n\t\t");
		builder.append("collectionName=").append(collectionName).append(",\n\t\t");
		builder.append("entityKeyFieldName=").append(entityKeyFieldName).append(",\n\t\t");
		builder.append("initializeIndexes=").append(initializeIndexes).append(",\n\t\t");
		builder.append("indexes=[");
		if (indexes != null)
			for (Index index : indexes)
				builder.append("\n\t\t\t").append(index);
		builder.append("\n\t\t],\n\t");
		builder.append("]\n");
		return builder.toString();
	}

}
