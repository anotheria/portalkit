package net.anotheria.portalkit.services.storage.query;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.query.value.QueryValue;

/**
 * General query interface.
 * 
 * @author Alexandr Bolbat
 */
public interface Query extends Serializable {

	/**
	 * Get field name.
	 * 
	 * @return {@link String}
	 */
	String getFieldName();

	/**
	 * Get query value.
	 * 
	 * @return {@link QueryValue}
	 */
	QueryValue getQueryValue();

}
