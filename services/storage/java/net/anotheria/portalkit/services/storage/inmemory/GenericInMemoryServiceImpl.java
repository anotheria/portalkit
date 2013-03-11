package net.anotheria.portalkit.services.storage.inmemory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.portalkit.services.storage.exception.EntityAlreadyExistStorageException;
import net.anotheria.portalkit.services.storage.exception.EntityNotFoundStorageException;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.inmemory.query.InMemoryQueryProcessor;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.util.SerializationUtils;

import org.apache.log4j.Logger;

/**
 * {@link GenericInMemoryService} implementation.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 *            entity type
 */
public class GenericInMemoryServiceImpl<T extends Serializable> implements GenericInMemoryService<T> {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(GenericInMemoryServiceImpl.class);

	/**
	 * In-Memory storage.
	 */
	private final Map<String, T> storage = new HashMap<String, T>();

	/**
	 * Entity key field name.
	 */
	private final String entityKeyFieldName;

	/**
	 * Default constructor.
	 * 
	 * @param aEntityKeyFieldName
	 *            entity key field name
	 */
	public GenericInMemoryServiceImpl(final String aEntityKeyFieldName) {
		if (aEntityKeyFieldName == null || aEntityKeyFieldName.trim().isEmpty())
			throw new IllegalArgumentException("aEntityKeyFieldName argument is empty.");

		this.entityKeyFieldName = aEntityKeyFieldName;
	}

	@Override
	public T read(final String uid) throws StorageException {
		if (uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("uid argument is empty.");

		T entity = storage.get(uid);
		if (entity == null)
			throw new EntityNotFoundStorageException(uid);

		return SerializationUtils.clone(entity);
	}

	@Override
	public T save(final T toSave) throws StorageException {
		if (toSave == null)
			throw new IllegalArgumentException("toSave argument is null.");

		String uid = getEntityUID(toSave);

		T toSaveInt = SerializationUtils.clone(toSave);
		storage.put(uid, toSaveInt);

		return SerializationUtils.clone(toSaveInt);
	}

	@Override
	public T create(final T toCreate) throws StorageException {
		if (toCreate == null)
			throw new IllegalArgumentException("toCreate argument is null.");

		String uid = getEntityUID(toCreate);
		try {
			T entity = read(uid);
			if (entity != null)
				throw new EntityAlreadyExistStorageException(uid);
		} catch (EntityNotFoundStorageException e) {
			if (LOGGER.isTraceEnabled())
				LOGGER.trace("create(" + toCreate + ") expected exception. Message[" + e.getMessage() + "].");
		}

		T toCreateInt = SerializationUtils.clone(toCreate);
		storage.put(uid, toCreateInt);

		return SerializationUtils.clone(toCreateInt);
	}

	@Override
	public T update(final T toUpdate) throws StorageException {
		if (toUpdate == null)
			throw new IllegalArgumentException("toUpdate argument is null.");

		String uid = getEntityUID(toUpdate);

		// checking is entity exist
		read(uid); // there EntityNotFoundStorageException can be thrown

		T toUpdateInt = SerializationUtils.clone(toUpdate);
		storage.put(uid, toUpdateInt);

		return SerializationUtils.clone(toUpdateInt);
	}

	@Override
	public T delete(final String uid) throws StorageException {
		if (uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("uid argument is empty.");

		// checking is entity exist
		T entity = read(uid); // there EntityNotFoundStorageException can be thrown
		storage.remove(uid);

		return SerializationUtils.clone(entity);
	}

	@Override
	public List<T> read(final List<String> uidList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (uidList == null || uidList.isEmpty())
			return result;

		for (String uid : uidList)
			try {
				result.add(read(uid));
			} catch (StorageException e) {
				LOGGER.warn("read('uidList') skipping read(" + uid + ").", e);
			}

		return result;
	}

	@Override
	public List<T> save(final List<T> toSaveList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (toSaveList == null || toSaveList.isEmpty())
			return result;

		for (T entity : toSaveList)
			try {
				result.add(save(entity));
			} catch (StorageException e) {
				LOGGER.warn("save('toSaveList') skipping save(" + entity + ").", e);
			}

		return result;
	}

	@Override
	public List<T> create(final List<T> toCreateList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (toCreateList == null || toCreateList.isEmpty())
			return result;

		for (T entity : toCreateList)
			try {
				result.add(create(entity));
			} catch (StorageException e) {
				LOGGER.warn("create('toCreateList') skipping create(" + entity + ").", e);
			}

		return result;
	}

	@Override
	public List<T> update(final List<T> toUpdateList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (toUpdateList == null || toUpdateList.isEmpty())
			return result;

		for (T entity : toUpdateList)
			try {
				result.add(update(entity));
			} catch (StorageException e) {
				LOGGER.warn("update('toUpdateList') skipping update(" + entity + ").", e);
			}

		return result;
	}

	@Override
	public List<T> delete(final List<String> uidList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (uidList == null || uidList.isEmpty())
			return result;

		for (String uid : uidList)
			try {
				result.add(delete(uid));
			} catch (StorageException e) {
				LOGGER.warn("delete('uidList') skipping delete(" + uid + ").", e);
			}

		return result;
	}

	@Override
	public List<T> findAll() throws StorageException {
		return SerializationUtils.clone(storage.values());
	}

	@Override
	public List<T> find(final Query query) throws StorageException {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		List<T> internalResult = InMemoryQueryProcessor.execute(storage.values(), query);
		return SerializationUtils.clone(internalResult);
	}

	/**
	 * Get entity unique identifier.
	 * 
	 * @param entity
	 *            entity
	 * @return unique identifier
	 * @throws StorageException
	 */
	private String getEntityUID(final Serializable entity) throws StorageException {
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
