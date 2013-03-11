package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.NumberValue;
import net.anotheria.portalkit.services.storage.query.value.QueryValue;

/**
 * {@link LessThanQuery}.
 * 
 * @author Alexandr Bolbat
 */
public final class LessThanQuery extends AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 2909568816125822224L;

	/**
	 * {@link LessThanModifier} modifier.
	 */
	private final LessThanModifier modifier;

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
	private LessThanQuery(final String fieldName, final QueryValue queryValue, final LessThanModifier aModifier) {
		super(fieldName, queryValue);
		this.modifier = aModifier != null ? aModifier : LessThanModifier.DEFAULT;
	}

	public LessThanModifier getModifier() {
		return modifier;
	}

	/**
	 * Create new instance of {@link LessThanQuery} with {@link Number} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link LessThanQuery}
	 */
	public static LessThanQuery create(final String fieldName, final Number value) {
		return create(fieldName, value, LessThanModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link LessThanQuery} with {@link Number} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @param aModifier
	 *            query modifier
	 * @return {@link LessThanQuery}
	 */
	public static LessThanQuery create(final String fieldName, final Number value, final LessThanModifier aModifier) {
		return new LessThanQuery(fieldName, NumberValue.create(value), aModifier);
	}

	@Override
	public String toString() {
		return "lessThan(" + modifier + ")[" + getFieldName() + "=" + getQueryValue() + "]";
	}

}
