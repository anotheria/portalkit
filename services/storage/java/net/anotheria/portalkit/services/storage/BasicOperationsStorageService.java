package net.anotheria.portalkit.services.storage;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.exception.StorageException;

/**
 * Service with basic operations on entity.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public interface BasicOperationsStorageService<T extends Serializable> {

	/**
	 * Read entity.
	 * 
	 * @param uid
	 *            entity unique id
	 * @return entity
	 * @throws StorageException
	 */
	T read(String uid) throws StorageException;

	/**
	 * Save (create or update) entity.
	 * 
	 * @param toSave
	 *            entity to save
	 * @return saved entity
	 * @throws StorageException
	 */
	T save(T toSave) throws StorageException;

	/**
	 * Create entity.
	 * 
	 * @param toCreate
	 *            entity to create
	 * @return created entity
	 * @throws StorageException
	 */
	T create(T toCreate) throws StorageException;

	/**
	 * Update entity.
	 * 
	 * @param toUpdate
	 *            entity to update
	 * @return updated entity
	 * @throws StorageException
	 */
	T update(T toUpdate) throws StorageException;

	/**
	 * Delete entity.
	 * 
	 * @param uid
	 *            entity unique id
	 * @return deleted entity
	 * @throws StorageException
	 */
	T delete(String uid) throws StorageException;

}
