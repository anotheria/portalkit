package net.anotheria.portalkit.services.storage;

import java.io.Serializable;

import net.anotheria.anoprise.metafactory.AbstractParameterizedServiceFactory;
import net.anotheria.portalkit.services.storage.exception.StorageRuntimeException;
import net.anotheria.portalkit.services.storage.inmemory.GenericInMemoryServiceFactory;
import net.anotheria.portalkit.services.storage.mongo.GenericMongoServiceFactory;

/**
 * Parameterizable {@link StorageService} factory.<br>
 * Parameters:<br>
 * - StorageType - check constant 'PARAMETER_STORAGE_TYPE'.<br>
 * <br>
 * Supported types:<br>
 * - In-memory generic - check {@link GenericInMemoryServiceFactory} for supported parameters; <br>
 * - Mongo generic - check {@link GenericMongoServiceFactory} for supported parameters.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public final class StorageServiceFactory<T extends Serializable> extends AbstractParameterizedServiceFactory<StorageService<T>> {

	/**
	 * Storage type parameter name. Value should be instance of {@link StorageType}.
	 */
	public static final String PARAMETER_STORAGE_TYPE = "STORAGE_TYPE";

	@Override
	public StorageService<T> create() {
		Serializable rawStorageType = getParameterValue(PARAMETER_STORAGE_TYPE);
		if (rawStorageType == null || !(rawStorageType instanceof StorageType))
			throw new StorageRuntimeException("Wrong storage type[" + rawStorageType + "] configuration.");

		StorageType type = StorageType.class.cast(rawStorageType);
		switch (type) {
		case IN_MEMORY_GENERIC:
			GenericInMemoryServiceFactory<T> inMemoryFactory = new GenericInMemoryServiceFactory<T>();
			inMemoryFactory.setParameters(getParameters());
			return inMemoryFactory.create();
		case NOSQL_MONGO_GENERIC:
			GenericMongoServiceFactory<T> mongoFactory = new GenericMongoServiceFactory<T>();
			mongoFactory.setParameters(getParameters());
			return mongoFactory.create();
		default:
			throw new StorageRuntimeException("Storage type[" + type + "] not supported.");
		}
	}

}
