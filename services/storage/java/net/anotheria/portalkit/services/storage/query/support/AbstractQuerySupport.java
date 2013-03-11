package net.anotheria.portalkit.services.storage.query.support;

/**
 * Common {@link QuerySupport} functionality.
 * 
 * @author Alexandr Bolbat
 */
public abstract class AbstractQuerySupport implements QuerySupport {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
		builder.append(" [storageType=");
		builder.append(getStorageType());
		builder.append("]");
		return builder.toString();
	}

}
