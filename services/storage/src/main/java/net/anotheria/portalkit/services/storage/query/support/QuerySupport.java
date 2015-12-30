package net.anotheria.portalkit.services.storage.query.support;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.type.StorageType;

/**
 * Query support interface.
 * 
 * @author Alexandr Bolbat
 */
public interface QuerySupport {

	/**
	 * Get {@link StorageType}.
	 * 
	 * @return {@link StorageType}
	 */
	StorageType getStorageType();

	/**
	 * Return <code>true</code> if bean match query or <code>false</code>.
	 * 
	 * @param query
	 *            query
	 * @param bean
	 *            bean
	 * @return {@link Boolean}
	 */
	boolean canPass(Query query, Serializable bean);

}
