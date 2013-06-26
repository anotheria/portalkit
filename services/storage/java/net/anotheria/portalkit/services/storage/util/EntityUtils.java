package net.anotheria.portalkit.services.storage.util;

import java.lang.reflect.Field;
import java.util.Set;

import net.anotheria.portalkit.services.storage.exception.StorageException;

import org.reflections.ReflectionUtils;

/**
 * Reflections utility.
 * 
 * @author Alexandr Bolbat
 */
public final class EntityUtils {

	/**
	 * Default constructor with preventing instantiations of this class.
	 */
	private EntityUtils() {
		throw new IllegalAccessError("Shouldn't be instantiated.");
	}

	/**
	 * Is entity field exist.
	 * 
	 * @param entity
	 *            entity type
	 * @param fieldName
	 *            field name
	 * @return <code>true</code> if field exist or <code>false</code>
	 */
	public static boolean isFieldExist(final Class<?> entity, final String fieldName) {
		Set<Field> fields = ReflectionUtils.getAllFields(entity, ReflectionUtils.withName(fieldName));
		if (fields == null || fields.isEmpty())
			return false;

		for (Field field : fields)
			if (field != null)
				return true;

		return false;
	}

	/**
	 * Get entity field value.
	 * 
	 * @param entity
	 *            entity
	 * @param fieldName
	 *            field name
	 * @return field value as {@link String}
	 * @throws StorageException
	 */
	public static String getFieldValue(final Object entity, final String fieldName) throws StorageException {
		try {
			Set<Field> fields = ReflectionUtils.getAllFields(entity.getClass(), ReflectionUtils.withName(fieldName));
			if (fields == null || fields.isEmpty())
				throw new StorageException("Wrong field[" + fieldName + "] configured.");

			for (Field field : fields)
				if (field != null) {
					field.setAccessible(true);
					return String.valueOf(field.get(entity));
				}

			throw new StorageException("Wrong field[" + fieldName + "] configured.");
		} catch (SecurityException e) {
			throw new StorageException("Wrong field[" + fieldName + "] configured.", e);
		} catch (IllegalArgumentException e) {
			throw new StorageException("Wrong field[" + fieldName + "] configured.", e);
		} catch (IllegalAccessException e) {
			throw new StorageException("Wrong field[" + fieldName + "] configured.", e);
		}
	}

}
