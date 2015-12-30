package net.anotheria.portalkit.services.common.eventing;

import java.io.Serializable;

/**
 * The purpose of this class is to hold the data that is associated with particular service operation
 * (i.e. ProfileService: createProfile operation would produce ServiceOperationData that holds created profile in itself).
 *
 * @author Alex Osadchy
 */
public abstract class ServiceEventData implements Serializable {

	/**
	 * Default Version Number.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Returns type of service operation. We didn't make it {@link Enum} for adding flexibility
	 * (different services can have their own operation types except traditional CRUD).
	 *
	 * @return {@link String} type identifier of the service operation type
	 */
	public abstract String getOperationType();

}
