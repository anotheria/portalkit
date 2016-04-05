package net.anotheria.portalkit.services.storage.mongo;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.query.Query;

/**
 * Generic mongo service definition.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public interface GenericMongoService<T extends Serializable> extends StorageService<T> {

    void delete(final Query query) throws StorageException;
}
