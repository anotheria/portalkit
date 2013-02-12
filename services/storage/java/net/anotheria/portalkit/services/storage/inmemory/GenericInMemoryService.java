package net.anotheria.portalkit.services.storage.inmemory;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.StorageService;

/**
 * Generic in-memory service definition.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public interface GenericInMemoryService<T extends Serializable> extends StorageService<T> {

}
