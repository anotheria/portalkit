package net.anotheria.portalkit.services.storage.query.value;

/**
 * Null {@link net.anotheria.portalkit.services.storage.query.value.QueryValue}.
 *
 * @author Ivan Batura
 */
public class NullValue implements QueryValue {
	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 4632580835411837696L;
	/**
	 * Value.
	 */
	private final Boolean value;

	/**
	 * Constructor.
	 *
	 * @param aValue
	 * 		{@link Boolean} value
	 */
	public NullValue(Boolean aValue) {
		this.value = aValue != null ? aValue : Boolean.FALSE;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "null";
	}

	/**
	 * Create new instance of {@link NullValue}.
	 *
	 * @return {@link NumberValue}
	 */
	public static NullValue create(Boolean value) {
		return new NullValue(value);
	}

}
