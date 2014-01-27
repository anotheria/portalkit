package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.BooleanValue;
import net.anotheria.portalkit.services.storage.query.value.NullValue;
import net.anotheria.portalkit.services.storage.query.value.NumberValue;
import net.anotheria.portalkit.services.storage.query.value.QueryValue;
import net.anotheria.portalkit.services.storage.query.value.StringValue;

/**
 * {@link EqualQuery}.
 *
 * @author Alexandr Bolbat
 */
public final class EqualQuery extends AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -8797067504240035729L;

	/**
	 * Default constructor.
	 *
	 * @param fieldName
	 * 		field name
	 * @param queryValue
	 * 		query value
	 */
	private EqualQuery(final String fieldName, final QueryValue queryValue) {
		super(fieldName, queryValue);
	}

	/**
	 * Create new instance of {@link EqualQuery} with {@link String} value.
	 *
	 * @param fieldName
	 * 		field name
	 * @param value
	 * 		query value
	 * @return {@link EqualQuery}
	 */
	public static EqualQuery create(final String fieldName, final String value) {
		return new EqualQuery(fieldName, StringValue.create(value));
	}

	/**
	 * Create new instance of {@link EqualQuery} with {@link Number} value.
	 *
	 * @param fieldName
	 * 		field name
	 * @param value
	 * 		query value
	 * @return {@link EqualQuery}
	 */
	public static EqualQuery create(final String fieldName, final Number value) {
		return new EqualQuery(fieldName, NumberValue.create(value));
	}

	/**
	 * Create new instance of {@link EqualQuery} with {@link Boolean} value.
	 *
	 * @param fieldName
	 * 		field name
	 * @param value
	 * 		query value
	 * @return {@link EqualQuery}
	 */
	public static EqualQuery create(final String fieldName, final Boolean value) {
		return new EqualQuery(fieldName, BooleanValue.create(value));
	}


	/**
	 * Create new instance of {@link EqualQuery} with null value.
	 *
	 * @param fieldName
	 * 		field name
	 * @return {@link EqualQuery}
	 */
	public static EqualQuery create(final String fieldName, final NullModifier value) {
		return new EqualQuery(fieldName, value.getValue());
	}

	@Override
	public String toString() {
		return "equal[" + getFieldName() + "=" + getQueryValue() + "]";
	}

	/**
	 * @author ivanbatura
	 */
	public enum NullModifier {
		/**
		 * Null value.
		 */
		NULL(new NullValue(Boolean.TRUE)),
		/**
		 * Not Null value.
		 */
		NOT_NULL(new NullValue(Boolean.FALSE));
		/**
		 * Value.
		 */
		private NullValue value;

		/**
		 * Constructor.
		 *
		 * @param value
		 * 		value
		 */
		NullModifier(NullValue value) {
			this.value = value;
		}

		public NullValue getValue() {
			return value;
		}
	}

}
