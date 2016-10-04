package net.anotheria.portalkit.services.storage.query;

import java.io.Serializable;
import java.util.List;

import net.anotheria.portalkit.services.storage.exception.StorageException;

/**
 * Service with query operations.
 * 
 * @author Alexandr Bolbat
 */
public interface BasicQueryStorageService<T extends Serializable> {

	/**
	 * Find all entities.
	 * 
	 * @return {@link List} of T
	 * @throws StorageException	if error.
	 */
	List<T> findAll() throws StorageException;

	/**
	 * Find entities by given query.
	 * 
	 * @param query
	 *            query
	 * @return {@link List} of T
	 * @throws StorageException	if error.
	 */
	List<T> find(Query query) throws StorageException;

}
