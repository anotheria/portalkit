package net.anotheria.portalkit.services.storage.mongo;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.StorageService;

/**
 * Generic mongo service definition.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public interface GenericMongoService<T extends Serializable> extends StorageService<T> {

}
