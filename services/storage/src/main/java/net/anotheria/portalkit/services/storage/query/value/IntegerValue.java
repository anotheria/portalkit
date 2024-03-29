package net.anotheria.portalkit.services.storage.query.value;

/**
 * {@link IntegerValue} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class IntegerValue extends NumberValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 4190221682782550536L;

	/**
	 * Default constructor.
	 * 
	 * @param aValue
	 *            value
	 */
	public IntegerValue(final Integer aValue) {
		super(aValue);
	}

	@Override
	public Integer getValue() {
		return Integer.class.cast(super.getValue());
	}

	/**
	 * Create new instance of {@link IntegerValue}.
	 * 
	 * @param aValue
	 *            value
	 * @return {@link IntegerValue}
	 */
	public static IntegerValue create(final Integer aValue) {
		return new IntegerValue(aValue);
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}

}
