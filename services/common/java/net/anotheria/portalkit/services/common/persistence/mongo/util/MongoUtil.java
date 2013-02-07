package net.anotheria.portalkit.services.common.persistence.mongo.util;

import java.io.Serializable;
import java.lang.reflect.Field;

import net.anotheria.portalkit.services.common.persistence.mongo.GenericMongoServiceConfig;
import net.anotheria.portalkit.services.common.persistence.mongo.exception.StorageException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Mongo utility.
 * 
 * @author Alexandr Bolbat
 */
public final class MongoUtil {

	/**
	 * Private constructor.
	 */
	private MongoUtil() {
		throw new IllegalAccessError();
	}

	/**
	 * Create {@link DBObject} query for getting object by its unique identifier.
	 * 
	 * @param uid
	 *            unique identifier
	 * @return {@link DBObject} query
	 */
	public static DBObject queryGetEntity(String uid) {
		return new BasicDBObject(MongoConstants.FIELD_ID_NAME, String.valueOf(uid));
	}

	/**
	 * Get entity unique identifier.
	 * 
	 * @param entity
	 *            entity
	 * @param configuration
	 *            configuration
	 * @return unique identifier
	 * @throws StorageException
	 */
	public static final String getEntityUID(Serializable entity, GenericMongoServiceConfig configuration) throws StorageException {
		String entityKeyFieldName = configuration.getEntityKeyFieldName();
		if (entityKeyFieldName == null || entityKeyFieldName.trim().isEmpty())
			throw new StorageException("Wrong key field[" + entityKeyFieldName + "] configured.");

		try {
			Field field = entity.getClass().getDeclaredField(entityKeyFieldName);
			field.setAccessible(true);
			return String.valueOf(field.get(entity));
		} catch (SecurityException e) {
			throw new StorageException("Wrong key field[" + entityKeyFieldName + "] configured.", e);
		} catch (NoSuchFieldException e) {
			throw new StorageException("Wrong key field[" + entityKeyFieldName + "] configured.", e);
		} catch (IllegalArgumentException e) {
			throw new StorageException("Wrong key field[" + entityKeyFieldName + "] configured.", e);
		} catch (IllegalAccessException e) {
			throw new StorageException("Wrong key field[" + entityKeyFieldName + "] configured.", e);
		}
	}

}
