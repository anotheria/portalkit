package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.NumberValue;
import net.anotheria.portalkit.services.storage.query.value.QueryValue;

/**
 * {@link MoreThanQuery}.
 * 
 * @author Alexandr Bolbat
 */
public final class MoreThanQuery extends AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -1200796017382346437L;

	/**
	 * {@link MoreThanModifier} modifier.
	 */
	private final MoreThanModifier modifier;

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
	private MoreThanQuery(final String fieldName, final QueryValue queryValue, final MoreThanModifier aModifier) {
		super(fieldName, queryValue);
		this.modifier = aModifier != null ? aModifier : MoreThanModifier.DEFAULT;
	}

	public MoreThanModifier getModifier() {
		return modifier;
	}

	/**
	 * Create new instance of {@link MoreThanQuery} with {@link Number} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link MoreThanQuery}
	 */
	public static MoreThanQuery create(final String fieldName, final Number value) {
		return create(fieldName, value, MoreThanModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link MoreThanQuery} with {@link Number} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @param aModifier
	 *            query modifier
	 * @return {@link MoreThanQuery}
	 */
	public static MoreThanQuery create(final String fieldName, final Number value, final MoreThanModifier aModifier) {
		return new MoreThanQuery(fieldName, NumberValue.create(value), aModifier);
	}

	@Override
	public String toString() {
		return "moreThan(" + modifier + ")[" + getFieldName() + "=" + getQueryValue() + "]";
	}

}
