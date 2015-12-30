package net.anotheria.portalkit.services.storage.query.value;

/**
 * {@link BooleanValue} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class BooleanValue implements QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 2552377509431936955L;

	/**
	 * Value.
	 */
	private final Boolean value;

	/**
	 * Default constructor.
	 * 
	 * @param aValue
	 *            value
	 */
	public BooleanValue(final Boolean aValue) {
		this.value = aValue != null ? aValue : Boolean.FALSE;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

	/**
	 * Create new instance of {@link BooleanValue}.
	 * 
	 * @param aValue
	 *            value
	 * @return {@link BooleanValue}
	 */
	public static BooleanValue create(final Boolean aValue) {
		return new BooleanValue(aValue);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
