package net.anotheria.portalkit.services.common.query.value;

/**
 * {@link LongValue} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class LongValue implements QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -7671714214567119935L;

	/**
	 * Value.
	 */
	private final Long value;

	/**
	 * Default constructor.
	 * 
	 * @param aValue
	 *            value
	 */
	public LongValue(final Long aValue) {
		this.value = aValue;
	}

	@Override
	public Long getValue() {
		return value;
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
		return String.valueOf(value);
	}

}
