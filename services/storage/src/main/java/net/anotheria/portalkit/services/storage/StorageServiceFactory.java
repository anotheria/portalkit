package net.anotheria.portalkit.services.storage;

import net.anotheria.anoprise.metafactory.AbstractParameterizedServiceFactory;
import net.anotheria.portalkit.services.storage.exception.StorageRuntimeException;
import net.anotheria.portalkit.services.storage.inmemory.GenericInMemoryServiceFactory;
import net.anotheria.portalkit.services.storage.mongo.GenericMongoServiceFactory;
import net.anotheria.portalkit.services.storage.type.StorageType;

import java.io.Serializable;

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
public class StorageServiceFactory<T extends Serializable> extends AbstractParameterizedServiceFactory<StorageService<T>> {

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
		case NO_SQL_MONGO_GENERIC:
			GenericMongoServiceFactory<T> mongoFactory = createMongoServiceFactory();
			mongoFactory.setParameters(getParameters());
			return mongoFactory.create();
		default:
			throw new StorageRuntimeException("Storage type[" + type + "] not supported.");
		}
	}

	protected GenericMongoServiceFactory<T> createMongoServiceFactory(){
		return new GenericMongoServiceFactory<T>();
	}

}
