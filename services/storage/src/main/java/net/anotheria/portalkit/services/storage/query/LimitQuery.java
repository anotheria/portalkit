package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.IntegerValue;

/**
 * {@link LimitQuery}, works only in root scope.
 * 
 * @author Alexandr Bolbat
 */
public final class LimitQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 4884289906646767787L;

	/**
	 * Mock field name.
	 */
	public static final String FIELD_NAME = "none";

	/**
	 * Limit value.
	 */
	private final IntegerValue limitValue;

	/**
	 * Default constructor.
	 * 
	 * @param aLimitValue
	 *            limit value
	 */
	private LimitQuery(final IntegerValue aLimitValue) {
		this.limitValue = aLimitValue;
	}

	@Override
	public String getFieldName() {
		return FIELD_NAME;
	}

	@Override
	public IntegerValue getQueryValue() {
		return limitValue;
	}

	/**
	 * Create new instance of {@link LimitQuery}.
	 * 
	 * @param limit
	 *            limit value
	 * @return {@link LimitQuery}
	 */
	public static LimitQuery create(final int limit) {
		return new LimitQuery(IntegerValue.create(limit >= 0 ? limit : 0));
	}

	@Override
	public String toString() {
		return "limit[" + getQueryValue() + "]";
	}

}
