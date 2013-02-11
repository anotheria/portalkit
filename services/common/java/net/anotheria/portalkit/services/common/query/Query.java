package net.anotheria.portalkit.services.common.query;

import java.io.Serializable;

import net.anotheria.portalkit.services.common.query.value.QueryValue;

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
