package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.IntegerValue;
import net.anotheria.portalkit.services.storage.query.value.LongValue;
import net.anotheria.portalkit.services.storage.query.value.QueryValue;

/**
 * {@link LessThenQuery}.
 * 
 * @author Alexandr Bolbat
 */
public final class LessThenQuery extends AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 2909568816125822224L;

	/**
	 * {@link LessThenModifier} modifier.
	 */
	private final LessThenModifier modifier;

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
	private LessThenQuery(final String fieldName, final QueryValue queryValue, final LessThenModifier aModifier) {
		super(fieldName, queryValue);
		this.modifier = aModifier != null ? aModifier : LessThenModifier.DEFAULT;
	}

	public LessThenModifier getModifier() {
		return modifier;
	}

	/**
	 * Create new instance of {@link LessThenQuery} with {@link Integer} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link LessThenQuery}
	 */
	public static LessThenQuery create(final String fieldName, final Integer value) {
		return create(fieldName, value, LessThenModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link LessThenQuery} with {@link Integer} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @param aModifier
	 *            query modifier
	 * @return {@link LessThenQuery}
	 */
	public static LessThenQuery create(final String fieldName, final Integer value, final LessThenModifier aModifier) {
		return new LessThenQuery(fieldName, IntegerValue.create(value), aModifier);
	}

	/**
	 * Create new instance of {@link LessThenQuery} with {@link Long} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link LessThenQuery}
	 */
	public static LessThenQuery create(final String fieldName, final Long value) {
		return create(fieldName, value, LessThenModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link LessThenQuery} with {@link Long} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @param aModifier
	 *            query modifier
	 * @return {@link LessThenQuery}
	 */
	public static LessThenQuery create(final String fieldName, final Long value, final LessThenModifier aModifier) {
		return new LessThenQuery(fieldName, LongValue.create(value), aModifier);
	}

	@Override
	public String toString() {
		return "lessThen(" + modifier + ")[" + getFieldName() + "=" + getQueryValue() + "]";
	}

}
