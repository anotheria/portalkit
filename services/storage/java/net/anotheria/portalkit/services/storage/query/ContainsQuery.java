package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.ListValue;

/**
 * {@link ContainsQuery}.
 * 
 * @author Alexandr Bolbat
 */
public final class ContainsQuery extends AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -2732419554491740370L;

	/**
	 * Default constructor.
	 * 
	 * @param fieldName
	 *            field name
	 * @param queryValue
	 *            query value
	 */
	private ContainsQuery(final String fieldName, final ListValue queryValue) {
		super(fieldName, queryValue);
	}

	@Override
	public ListValue getQueryValue() {
		return ListValue.class.cast(super.getQueryValue());
	}

	/**
	 * Create new instance of {@link ContainsQuery} with {@link String} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param values
	 *            query values
	 * @return {@link ContainsQuery}
	 */
	public static ContainsQuery create(final String fieldName, final String... values) {
		return new ContainsQuery(fieldName, ListValue.create(values));
	}

	/**
	 * Create new instance of {@link ContainsQuery} with {@link Number} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param values
	 *            query values
	 * @return {@link ContainsQuery}
	 */
	public static ContainsQuery create(final String fieldName, final Number... values) {
		return new ContainsQuery(fieldName, ListValue.create(values));
	}

	/**
	 * Create new instance of {@link ContainsQuery} with {@link Boolean} value.
	 * 
	 * @param fieldName
	 *            field name
	 * @param values
	 *            query values
	 * @return {@link ContainsQuery}
	 */
	public static ContainsQuery create(final String fieldName, final Boolean... values) {
		return new ContainsQuery(fieldName, ListValue.create(values));
	}

	@Override
	public String toString() {
		return "contains[" + getFieldName() + ":" + getQueryValue() + "]";
	}

}
