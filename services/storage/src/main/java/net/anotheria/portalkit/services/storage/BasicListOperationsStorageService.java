package net.anotheria.portalkit.services.storage;

import java.io.Serializable;
import java.util.List;

import net.anotheria.portalkit.services.storage.exception.StorageException;

/**
 * Service with basic list operations on entities.
 * 
 * @author Alexandr Bolbat
 */
public interface BasicListOperationsStorageService<T extends Serializable> {

	/**
	 * Read entities.
	 *
	 * @param uidList
	 *            entities unique id's
	 * @return {@link List} of T
	 * @throws StorageException if error.
	 */
	List<T> read(List<String> uidList) throws StorageException;

	/**
	 * Save (create or update) entities.
	 *
	 * @param toSaveList
	 *            entities to save
	 * @return saved {@link List} of T
	 * @throws StorageException if error.
	 */
	List<T> save(List<T> toSaveList) throws StorageException;

	/**
	 * Create entities.
	 *
	 * @param toCreateList
	 *            entities to create
	 * @return created {@link List} of T
	 * @throws StorageException if error.
	 */
	List<T> create(List<T> toCreateList) throws StorageException;

	/**
	 * Update entities.
	 *
	 * @param toUpdateList
	 *            entities to update
	 * @return updated {@link List} of T
	 * @throws StorageException if error.
	 */
	List<T> update(List<T> toUpdateList) throws StorageException;

	/**
	 * Delete entities.
	 *
	 * @param uidList
	 *            entities unique id's
	 * @return deleted {@link List} of T
	 * @throws StorageException if error.
	 */
	List<T> delete(List<String> uidList) throws StorageException;

}
