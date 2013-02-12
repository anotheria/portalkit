package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.QueryValue;

/**
 * {@link AbstractQuery}.
 * 
 * @author Alexandr Bolbat
 */
public abstract class AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 4691644248728043323L;

	/**
	 * Field name.
	 */
	private final String fieldName;

	/**
	 * Query value.
	 */
	private final QueryValue queryValue;

	/**
	 * Default constructor.
	 * 
	 * @param aFieldName
	 *            field name
	 * @param aQueryValue
	 *            field value
	 */
	protected AbstractQuery(final String aFieldName, final QueryValue aQueryValue) {
		if (aFieldName == null || aFieldName.trim().isEmpty())
			throw new IllegalArgumentException("aFieldName argument is empty");
		if (aQueryValue == null)
			throw new IllegalArgumentException("aQueryValue argument is empty");

		this.fieldName = aFieldName;
		this.queryValue = aQueryValue;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public QueryValue getQueryValue() {
		return queryValue;
	}

}
