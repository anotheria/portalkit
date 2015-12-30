package net.anotheria.portalkit.services.storage.query.value;

/**
 * {@link LongValue} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class LongValue extends NumberValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -7671714214567119935L;

	/**
	 * Default constructor.
	 * 
	 * @param aValue
	 *            value
	 */
	public LongValue(final Long aValue) {
		super(aValue);
	}

	@Override
	public Long getValue() {
		return Long.class.cast(super.getValue());
	}

	/**
	 * Create new instance of {@link LongValue}.
	 * 
	 * @param aValue
	 *            value
	 * @return {@link LongValue}
	 */
	public static LongValue create(final Long aValue) {
		return new LongValue(aValue);
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}

}
