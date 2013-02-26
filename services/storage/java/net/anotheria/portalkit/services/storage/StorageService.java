package net.anotheria.portalkit.services.storage;

import java.io.Serializable;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.storage.query.BasicQueryStorageService;

/**
 * Interface for generic storage services.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public interface StorageService<T extends Serializable> extends BasicOperationsStorageService<T>, BasicListOperationsStorageService<T>,
		BasicQueryStorageService<T>, Service {

}
