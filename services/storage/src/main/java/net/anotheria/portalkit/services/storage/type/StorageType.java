package net.anotheria.portalkit.services.storage.type;

/**
 * Storage types.
 * 
 * @author Alexandr Bolbat
 */
public enum StorageType {

	/**
	 * Generic in-memory implementation.
	 */
	IN_MEMORY_GENERIC,

	/**
	 * Generic file system implementation.
	 */
	FS_GENERIC,

	/**
	 * Generic mongo implementation.
	 */
	NO_SQL_MONGO_GENERIC,

	/**
	 * Generic SQL implementation.
	 */
	SQL_GENERIC;

}
