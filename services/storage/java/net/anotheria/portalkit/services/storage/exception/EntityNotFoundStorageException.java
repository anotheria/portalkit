package net.anotheria.portalkit.services.storage.exception;

/**
 * Storage exception.
 * 
 * @author Alexandr Bolbat
 */
public class EntityNotFoundStorageException extends StorageException {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -7343082391442105443L;

	/**
	 * Public constructor.
	 * 
	 * @param uid
	 *            entity id
	 */
	public EntityNotFoundStorageException(final String uid) {
		super("Entity with uid[" + uid + "] not found.");
	}

}
