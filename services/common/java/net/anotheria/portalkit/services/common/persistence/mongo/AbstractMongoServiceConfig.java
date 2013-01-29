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
 * {@link AbstractMongoService} configuration.
 * 
 * @author Alexandr Bolbat
 */
@ConfigureMe(name = "pk-mongo-abstract-service-config", allfields = false)
public final class AbstractMongoServiceConfig implements Serializable {

	/**
	 * Generated SerialVersionUID.
	 */
	@DontConfigure
	private static final long serialVersionUID = -1137186520917278525L;

	/**
	 * {@link Logger} instance.
	 */
	@DontConfigure
	private static final Logger LOGGER = Logger.getLogger(AbstractMongoServiceConfig.class);

	/**
	 * Synchronization object.
	 */
	@DontConfigure
	private static final Object LOCK = new Object();

	/**
	 * {@link AbstractMongoServiceConfig} instance
	 */
	@DontConfigure
	private static AbstractMongoServiceConfig instance;

	/**
	 * Maximum service initialization time in milliseconds.
	 */
	@Configure
	private long initMaxTime = 1000 * 5;

	/**
	 * Maximum service initialization time in milliseconds.
	 */
	@Configure
	private long initWaitInterval = 100;

	/**
	 * Default constructor.
	 * 
	 * @param configuration
	 *            configuration name
	 * @param environment
	 *            configuration environment
	 */
	private AbstractMongoServiceConfig(final String configuration, final Environment environment) {
		try {
			if (configuration == null || configuration.trim().isEmpty()) {
				ConfigurationManager.INSTANCE.configure(this, environment);
			} else {
				ConfigurationManager.INSTANCE.configureAs(this, environment, configuration, Format.JSON);
			}
		} catch (RuntimeException e) {
			LOGGER.warn("AbstractMongoServiceConfig(conf:" + configuration + ", env: " + environment + ") Configuration fail[" + e.getMessage()
					+ "]. Relaying on defaults.");
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(e);

		}

		LOGGER.info("AbstractMongoServiceConfig(conf:" + configuration + ", env: " + environment + ") Configured with[" + this.toString() + "]");
	}

	/**
	 * Get configured instance of {@link AbstractMongoServiceConfig}.
	 * 
	 * @return {@link AbstractMongoServiceConfig}
	 */
	public static AbstractMongoServiceConfig getInstance() {
		return getInstanceForConfiguration(null);
	}

	/**
	 * Get configured instance of {@link AbstractMongoServiceConfig}.
	 * 
	 * @param configuration
	 *            configuration name, can be <code>null</code> or empty
	 * @return {@link AbstractMongoServiceConfig}
	 */
	public static AbstractMongoServiceConfig getInstanceForConfiguration(final String configuration) {
		return getInstance(configuration, null);
	}

	/**
	 * Get configured instance of {@link AbstractMongoServiceConfig}.
	 * 
	 * @param environment
	 *            environment name, can be <code>null</code> or empty
	 * @return {@link AbstractMongoServiceConfig}
	 */
	public static AbstractMongoServiceConfig getInstanceForEnvironment(final String environment) {
		return getInstance(null, environment);
	}

	/**
	 * get configured instance of {@link AbstractMongoServiceConfig}
	 * 
	 * @param configuration
	 *            configuration name, can be <code>null</code> or empty
	 * @param environment
	 *            environment name, can be <code>null</code> or empty
	 * @return {@link AbstractMongoServiceConfig}
	 */
	public static AbstractMongoServiceConfig getInstance(final String configuration, final String environment) {
		if ((configuration == null || configuration.trim().isEmpty()) && (environment == null || environment.trim().isEmpty())) {
			if (instance != null)
				return instance;

			synchronized (LOCK) {
				if (instance == null)
					instance = new AbstractMongoServiceConfig(null, ConfigurationManager.INSTANCE.getDefaultEnvironment());
			}

			return instance;
		}

		Environment env = ConfigurationManager.INSTANCE.getDefaultEnvironment();
		if (environment != null && !environment.trim().isEmpty())
			env = DynamicEnvironment.parse(environment);

		return new AbstractMongoServiceConfig(configuration, env);
	}

	public long getInitMaxTime() {
		return initMaxTime;
	}

	public void setInitMaxTime(final long aInitMaxTime) {
		this.initMaxTime = aInitMaxTime;
	}

	public long getInitWaitInterval() {
		return initWaitInterval;
	}

	public void setInitWaitInterval(final long aInitWaitInterval) {
		this.initWaitInterval = aInitWaitInterval;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tAbstractMongoServiceConfig [\n\t initMaxTime=");
		builder.append(initMaxTime);
		builder.append(", initWaitInterval=");
		builder.append(initWaitInterval);
		builder.append("\n\t]\n");
		return builder.toString();
	}

}
