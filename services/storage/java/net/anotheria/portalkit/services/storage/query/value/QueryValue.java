package net.anotheria.portalkit.services.storage.query.value;

import java.io.Serializable;

/**
 * General query value interface.
 * 
 * @author Alexandr Bolbat
 */
public interface QueryValue extends Serializable {

	/**
	 * Get value.
	 * 
	 * @return {@link Serializable} value
	 */
	Serializable getValue();

}
