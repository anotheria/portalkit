package net.anotheria.portalkit.services.storage.util;

import java.io.Serializable;

/**
 * Example {@link Serializable} bean.
 * 
 * @author Alexandr Bolbat
 */
public class SerializableBean implements Serializable {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 817465805511655427L;

	/**
	 * Value.
	 */
	private String value;

	/**
	 * Default constructor.
	 * 
	 * @param aValue
	 *            value
	 */
	public SerializableBean(final String aValue) {
		this.value = aValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String aValue) {
		this.value = aValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SerializableBean [value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

}
