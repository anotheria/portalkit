package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.IntegerValue;
import net.anotheria.portalkit.services.storage.query.value.LongValue;
import net.anotheria.portalkit.services.storage.query.value.QueryValue;

/**
 * {@link MoreThenQuery}.
 * 
 * @author Alexandr Bolbat
 */
public final class MoreThenQuery extends AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -1200796017382346437L;

	/**
	 * {@link MoreThenModifier} modifier.
	 */
	private final MoreThenModifier modifier;

	/**
	 * Default constructor.
	 * 
	 * @param fieldName
	 *            field name
	 * @param queryValue
	 *            query value
	 * @param aModifier
	 *            query modifier
	 */
	private MoreThenQuery(final String fieldName, final QueryValue queryValue, final MoreThenModifier aModifier) {
		super(fieldName, queryValue);
		this.modifier = aModifier != null ? aModifier : MoreThenModifier.DEFAULT;
	}

	public MoreThenModifier getModifier() {
		return modifier;
	}

	/**
	 * Create new instance of {@link MoreThenQuery} with {@link Integer} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link MoreThenQuery}
	 */
	public static MoreThenQuery create(final String fieldName, final Integer value) {
		return create(fieldName, value, MoreThenModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link MoreThenQuery} with {@link Integer} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @param aModifier
	 *            query modifier
	 * @return {@link MoreThenQuery}
	 */
	public static MoreThenQuery create(final String fieldName, final Integer value, final MoreThenModifier aModifier) {
		return new MoreThenQuery(fieldName, IntegerValue.create(value), aModifier);
	}

	/**
	 * Create new instance of {@link MoreThenQuery} with {@link Long} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link MoreThenQuery}
	 */
	public static MoreThenQuery create(final String fieldName, final Long value) {
		return create(fieldName, value, MoreThenModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link MoreThenQuery} with {@link Long} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @param aModifier
	 *            query modifier
	 * @return {@link MoreThenQuery}
	 */
	public static MoreThenQuery create(final String fieldName, final Long value, final MoreThenModifier aModifier) {
		return new MoreThenQuery(fieldName, LongValue.create(value), aModifier);
	}

	@Override
	public String toString() {
		return "moreThen(" + modifier + ")[" + getFieldName() + "=" + getQueryValue() + "]";
	}

}
