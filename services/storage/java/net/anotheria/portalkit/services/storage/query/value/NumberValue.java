package net.anotheria.portalkit.services.storage.query.value;

/**
 * {@link NumberValue} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class NumberValue implements QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 1463666205296472338L;

	/**
	 * Value.
	 */
	private final Number value;

	/**
	 * Default constructor.
	 * 
	 * @param aValue
	 *            value
	 */
	public NumberValue(final Number aValue) {
		this.value = aValue;
	}

	@Override
	public Number getValue() {
		return value;
	}

	/**
	 * Create new instance of {@link NumberValue}.
	 * 
	 * @param aValue
	 *            value
	 * @return {@link NumberValue}
	 */
	public static NumberValue create(final Number aValue) {
		return new NumberValue(aValue);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
