package net.anotheria.portalkit.services.common.persistence.mongo;

import org.apache.log4j.Logger;

/**
 * {@link GenericMongoService} implementation.
 * 
 * @author Alexandr Bolbat
 */
public class GenericMongoServiceImpl extends AbstractMongoService implements GenericMongoService {

	/**
	 * {@link Logger} instance.
	 */
	protected static final Logger LOGGER = Logger.getLogger(GenericMongoServiceImpl.class);

	/**
	 * {@link GenericMongoServiceConfig} instance.
	 */
	private final GenericMongoServiceConfig configuration;

	/**
	 * Default constructor.
	 */
	public GenericMongoServiceImpl() {
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
	public GenericMongoServiceImpl(final String conf, final String serviceConf, final String clientConf, final String env) {
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
