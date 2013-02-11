package net.anotheria.portalkit.services.common.query;

import net.anotheria.portalkit.services.common.query.value.IntegerValue;
import net.anotheria.portalkit.services.common.query.value.LongValue;
import net.anotheria.portalkit.services.common.query.value.QueryValue;
import net.anotheria.portalkit.services.common.query.value.StringValue;

/**
 * {@link EqualQuery}.
 * 
 * @author Alexandr Bolbat
 */
public class EqualQuery extends AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -8797067504240035729L;

	/**
	 * Default constructor.
	 * 
	 * @param fieldName
	 *            field name
	 * @param queryValue
	 *            query value
	 */
	private EqualQuery(final String fieldName, final QueryValue queryValue) {
		super(fieldName, queryValue);
	}

	/**
	 * Create new instance of {@link EqualQuery} with {@link String} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link EqualQuery}
	 */
	public static EqualQuery create(final String fieldName, final String value) {
		return new EqualQuery(fieldName, StringValue.create(value));
	}

	/**
	 * Create new instance of {@link EqualQuery} with {@link Integer} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link EqualQuery}
	 */
	public static EqualQuery create(final String fieldName, final Integer value) {
		return new EqualQuery(fieldName, IntegerValue.create(value));
	}

	/**
	 * Create new instance of {@link EqualQuery} with {@link Long} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            query value
	 * @return {@link EqualQuery}
	 */
	public static EqualQuery create(final String fieldName, final Long value) {
		return new EqualQuery(fieldName, LongValue.create(value));
	}

	@Override
	public String toString() {
		return "equal[" + getFieldName() + "=" + getQueryValue() + "]";
	}

}
