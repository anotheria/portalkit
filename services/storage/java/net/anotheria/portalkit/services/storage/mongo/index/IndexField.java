package net.anotheria.portalkit.services.storage.mongo.index;

import java.io.Serializable;

import org.configureme.annotations.Configure;
import org.configureme.annotations.DontConfigure;

/**
 * Index field configuration.
 * 
 * @author Alexandr Bolbat
 */
public class IndexField implements Serializable {

	/**
	 * Generated SerialVersionUID.
	 */
	@DontConfigure
	private static final long serialVersionUID = 7245618183926014804L;

	/**
	 * Field name.
	 */
	@Configure
	private String name;

	/**
	 * For each field in the index specify either 1 for an ascending order or -1 for a descending order, which represents the order of the keys in the index.<br>
	 * Ignored for hashed fields.
	 */
	@Configure
	private int order = 1;

	/**
	 * MongoDB can use the hashed index to support equality queries, but hashed indexes do not support range queries.<br>
	 * Hashed indexes compute a hash of the value of a field in a collection and index the hashed value.<br>
	 * These indexes permit equality queries and may be suitable shard keys for some collections.<br>
	 * MongoDB supports hashed indexes of any single field.<br>
	 * The hashing function collapses sub-documents and computes the hash for the entire value, but does not support multi-key (i.e. arrays) indexes.
	 */
	@Configure
	private boolean hashed = false;

	public String getName() {
		return name;
	}

	public void setName(final String aName) {
		this.name = aName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(final int aOrder) {
		this.order = aOrder;
	}

	public boolean isHashed() {
		return hashed;
	}

	public void setHashed(final boolean aHashed) {
		this.hashed = aHashed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexField other = (IndexField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
		builder.append(" [name=").append(name);
		builder.append(", order=").append(order);
		builder.append(", hashed=").append(hashed);
		builder.append("]");
		return builder.toString();
	}

}
