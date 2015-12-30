package net.anotheria.portalkit.services.storage.query.common;

import net.anotheria.portalkit.services.storage.query.Query;

/**
 * {@link Query} constants.
 * 
 * @author Alexandr Bolbat
 */
public final class QueryConstants {

	/**
	 * Field separator for nested objects.<br>
	 * Can be used for querying entities by nested objects fields.<br>
	 * Example: 'profile.position.id' - in this example we have entity 'profile' and nested object 'position' who have own identifier field.
	 */
	public static final String QUERY_NESTING_SEPARATOR = ".";

}
