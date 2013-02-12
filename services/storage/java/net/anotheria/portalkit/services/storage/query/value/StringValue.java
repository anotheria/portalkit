package net.anotheria.portalkit.services.storage.query.value;

/**
 * {@link String} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class StringValue implements QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -6484398818614743471L;

	/**
	 * Value.
	 */
	private final String value;

	/**
	 * Default constructor.
	 * 
	 * @param aValue
	 *            value
	 */
	public StringValue(final String aValue) {
		this.value = aValue;
	}

	@Override
	public String getValue() {
		return value;
	}

	/**
	 * Create new instance of {@link StringValue}.
	 * 
	 * @param aValue
	 *            value
	 * @return {@link StringValue}
	 */
	public static StringValue create(final String aValue) {
		return new StringValue(aValue);
	}

	@Override
	public String toString() {
		return value;
	}

}
