package net.anotheria.portalkit.services.common.id;

import java.util.UUID;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.12.12 16:05
 */
public class IdGenerator {

	public static String generateUniqueRandomId(){
		return UUID.randomUUID().toString();
	}
}
