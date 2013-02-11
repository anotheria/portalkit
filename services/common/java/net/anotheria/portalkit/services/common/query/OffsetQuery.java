package net.anotheria.portalkit.services.common.query;

import net.anotheria.portalkit.services.common.query.value.IntegerValue;

/**
 * {@link OffsetQuery}, works only in root scope.
 * 
 * @author Alexandr Bolbat
 */
public class OffsetQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 845483407467511947L;

	/**
	 * Mock field name.
	 */
	public static final String FIELD_NAME = "none";

	/**
	 * Limit value.
	 */
	private final IntegerValue offsetValue;

	/**
	 * Default constructor.
	 * 
	 * @param aOffsetValue
	 *            offset value
	 */
	private OffsetQuery(final IntegerValue aOffsetValue) {
		this.offsetValue = aOffsetValue;
	}

	@Override
	public String getFieldName() {
		return FIELD_NAME;
	}

	@Override
	public IntegerValue getQueryValue() {
		return offsetValue;
	}

	/**
	 * Create new instance of {@link OffsetQuery}.
	 * 
	 * @param offset
	 *            limit value
	 * @return {@link OffsetQuery}
	 */
	public static OffsetQuery create(final int offset) {
		return new OffsetQuery(IntegerValue.create(offset >= 0 ? offset : 0));
	}

	@Override
	public String toString() {
		return "offset[" + getQueryValue() + "]";
	}

}
