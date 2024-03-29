package net.anotheria.portalkit.services.profileservice;

import com.mongodb.BasicDBObject;
import net.anotheria.anoprise.metafactory.Service;

import java.util.List;

/**
 * @author asamoilich.
 */
public interface ProfileService<T extends Profile> extends Service {

    /**
     * Read entity.
     *
     * @param uid entity unique _id
     * @return entity
     * @throws ProfileServiceException if error occurs
     */
    T read(String uid) throws ProfileServiceException;

    /**
     * Save (create or update) entity.
     *
     * @param toSave entity to save
     * @return saved entity
     * @throws ProfileServiceException if error occurs
     */
    T save(T toSave) throws ProfileServiceException;

    /**
     * Create entity.
     *
     * @param toCreate entity to create
     * @return created entity
     * @throws ProfileServiceException if error occurs
     */
    T create(T toCreate) throws ProfileServiceException;

    /**
     * Update entity.
     *
     * @param toUpdate entity to update
     * @return updated entity
     * @throws ProfileServiceException if error occurs
     */
    T update(T toUpdate) throws ProfileServiceException;

    /**
     * Delete entity.
     *
     * @param uid entity unique _id
     * @return deleted entity
     * @throws ProfileServiceException if error occurs
     */
    T delete(String uid) throws ProfileServiceException;

    /**
     * Find all entities.
     *
     * @return {@link List} of T
     * @throws ProfileServiceException if error occurs
     */
    List<T> findAll() throws ProfileServiceException;

    /**
     * Find entities by given query.
     *
     * @param query
     *            query
     * @return {@link List} of T
     * @throws ProfileServiceException if error occurs
     */
    List<T> find(BasicDBObject query) throws ProfileServiceException;
}
