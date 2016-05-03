package net.anotheria.portalkit.services.storage.mongo;

import net.anotheria.anoprise.metafactory.AbstractParameterizedServiceFactory;
import net.anotheria.portalkit.services.storage.exception.StorageRuntimeException;
import org.configureme.Environment;

import java.io.Serializable;

/**
 * {@link GenericMongoService} factory.<br>
 * Check parameters constants.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public class GenericMongoServiceFactory<T extends Serializable> extends AbstractParameterizedServiceFactory<GenericMongoService<T>> {

	/**
	 * Parameter name for entity class.<br>
	 * Required.
	 */
	public static final String PARAMETER_ENTITY_CLASS = "entityClass";

	/**
	 * Parameter name for {@link GenericMongoServiceConfig} configuration name.<br>
	 * Not required. If <code>null</code> default configuration name will be used.
	 */
	public static final String PARAMETER_CONFIGURATION = "conf";

	/**
	 * Parameter name for {@link AbstractMongoServiceConfig} configuration name.<br>
	 * Not required. If <code>null</code> default configuration name will be used.
	 */
	public static final String PARAMETER_SERVICE_CONFIGURATION = "serviceConf";

	/**
	 * Parameter name for {@link MongoClientConfig} configuration name.<br>
	 * Not required. If <code>null</code> default configuration name will be used.
	 */
	public static final String PARAMETER_MONGO_CLIENT = "clientConf";

	/**
	 * Parameter name for ConfigureMe {@link Environment} configuration.<br>
	 * Not required. If <code>null</code> current system environment will be used.
	 */
	public static final String PARAMETER_ENVIRONMENT = "env";

	@Override
	public GenericMongoService<T> create() {
		Serializable rawEntityClass = getParameterValue(PARAMETER_ENTITY_CLASS);
		if (rawEntityClass == null || !(rawEntityClass instanceof Class))
			throw new StorageRuntimeException("Wrong entity class[" + rawEntityClass + "] configuration.");

		@SuppressWarnings("unchecked")
		Class<T> entityClass = (Class<T>) rawEntityClass;
		String conf = getParameterValueAsString(PARAMETER_CONFIGURATION);
		String serviceConf = getParameterValueAsString(PARAMETER_SERVICE_CONFIGURATION);
		String clientConf = getParameterValueAsString(PARAMETER_MONGO_CLIENT);
		String env = getParameterValueAsString(PARAMETER_ENVIRONMENT);
		return new GenericMongoServiceImpl<T>(entityClass, conf, serviceConf, clientConf, env);
	}

}
