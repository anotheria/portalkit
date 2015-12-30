package net.anotheria.portalkit.services.storage.inmemory.query.support;

import net.anotheria.portalkit.services.storage.query.support.AbstractQuerySupport;
import net.anotheria.portalkit.services.storage.query.support.QuerySupport;
import net.anotheria.portalkit.services.storage.type.StorageType;

/**
 * {@link QuerySupport} abstraction for in-memory implementation.
 * 
 * @author Alexandr Bolbat
 */
public abstract class InMemoryQuerySupport extends AbstractQuerySupport {

	@Override
	public StorageType getStorageType() {
		return StorageType.IN_MEMORY_GENERIC;
	}

}
