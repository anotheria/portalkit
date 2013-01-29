package net.anotheria.portalkit.services.common.persistence.mongo;

import org.apache.log4j.Logger;

/**
 * Generic mongo service implementation.
 * 
 * @author Alexandr Bolbat
 */
public class GenericMongoService extends AbstractMongoService {

	/**
	 * {@link Logger} instance.
	 */
	protected static final Logger LOGGER = Logger.getLogger(GenericMongoService.class);

	/**
	 * {@link GenericMongoServiceConfig} instance.
	 */
	private final GenericMongoServiceConfig configuration;

	/**
	 * Default constructor.
	 */
	public GenericMongoService() {
		this(null, null, null, null);
	}

	/**
	 * Public constructor.
	 * 
	 * @param conf
	 *            service configuration name, can be <code>null</code>
	 * @param serviceConf
	 *            abstract service configuration name, can be <code>null</code>
	 * @param clientConf
	 *            mongo client configuration name, can be <code>null</code>
	 * @param env
	 *            configuration environment, can be <code>null</code>
	 */
	public GenericMongoService(final String conf, final String serviceConf, final String clientConf, final String env) {
		super(serviceConf, clientConf, env);

		this.configuration = GenericMongoServiceConfig.getInstance(conf, env);
		// initialization done there (not in super constructor)
		// because initialization use getDBName() method (custom for each implementation)
		// current implementation takes database name from configuration
		// and it should be initialized before general initialization, but after calling super constructor
		initialize();
	}

	@Override
	protected String getDBName() {
		return configuration.getDatabaseName();
	}

}
