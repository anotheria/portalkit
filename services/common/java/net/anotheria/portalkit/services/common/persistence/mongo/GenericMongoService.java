package net.anotheria.portalkit.services.common.persistence.mongo;

import java.io.Serializable;
import java.util.List;

import net.anotheria.portalkit.services.common.persistence.mongo.exception.StorageException;
import net.anotheria.portalkit.services.common.persistence.mongo.query.Query;

/**
 * Generic mongo service definition.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public interface GenericMongoService<T extends Serializable> {

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

	/**
	 * Read entities.
	 * 
	 * @param uidList
	 *            entities unique id's
	 * @return {@link List} of <T>
	 * @throws StorageException
	 */
	List<T> read(List<String> uidList) throws StorageException;

	/**
	 * Save (create or update) entities.
	 * 
	 * @param toSaveList
	 *            entities to save
	 * @return saved {@link List} of <T>
	 * @throws StorageException
	 */
	List<T> save(List<T> toSaveList) throws StorageException;

	/**
	 * Create entities.
	 * 
	 * @param toCreateList
	 *            entities to create
	 * @return created {@link List} of <T>
	 * @throws StorageException
	 */
	List<T> create(List<T> toCreateList) throws StorageException;

	/**
	 * Update entities.
	 * 
	 * @param toUpdateList
	 *            entities to update
	 * @return updated {@link List} of <T>
	 * @throws StorageException
	 */
	List<T> update(List<T> toUpdateList) throws StorageException;

	/**
	 * Delete entities.
	 * 
	 * @param uidList
	 *            entities unique id's
	 * @return deleted {@link List} of <T>
	 * @throws StorageException
	 */
	List<T> delete(List<String> uidList) throws StorageException;

	/**
	 * Find all entities.
	 * 
	 * @return {@link List} of <T>
	 * @throws StorageException
	 */
	List<T> findAll() throws StorageException;

	/**
	 * Find entities by given query.
	 * 
	 * @param query
	 *            query
	 * @return {@link List} of <T>
	 * @throws StorageException
	 */
	List<T> find(Query<T> query) throws StorageException;

}
