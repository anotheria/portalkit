package net.anotheria.portalkit.services.common.persistence.mongo.exception;

/**
 * Storage exception.
 * 
 * @author Alexandr Bolbat
 */
public class EntityAlreadyExistStorageException extends StorageException {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 7304132846779239438L;

	/**
	 * Public constructor.
	 * 
	 * @param uid
	 *            entity id
	 */
	public EntityAlreadyExistStorageException(final String uid) {
		super("Entity with uid[" + uid + "] already exist.");
	}

}
