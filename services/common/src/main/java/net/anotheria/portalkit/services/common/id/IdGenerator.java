package net.anotheria.portalkit.services.common.id;

import java.util.UUID;

/**
 * Unique id generator.
 * 
 * @author Leon Rosenberg
 * @since 13.12.12 16:05
 */
public final class IdGenerator {

	/**
	 * Private constructor.
	 */
	private IdGenerator() {
		throw new IllegalAccessError();
	}

	/**
	 * Generate new unique identifier.
	 * 
	 * @return generated {@link String}
	 */
	public static String generateUniqueRandomId() {
		return UUID.randomUUID().toString();
	}

}
