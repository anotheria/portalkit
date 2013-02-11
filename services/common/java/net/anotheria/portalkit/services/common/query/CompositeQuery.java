package net.anotheria.portalkit.services.common.query;

import java.util.List;

import net.anotheria.portalkit.services.common.query.value.QueryCompositeValue;
import net.anotheria.portalkit.services.common.query.value.QueryValue;

/**
 * {@link CompositeQuery}.
 * 
 * @author Alexandr Bolbat
 */
public class CompositeQuery implements Query, QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 88215187443197341L;

	/**
	 * Mock field name.
	 */
	public static final String FIELD_NAME = "none";

	/**
	 * Query value.
	 */
	private final QueryCompositeValue queryValue;

	/**
	 * {@link CompositeQuery} modifier.
	 */
	private final CompositeModifier modifier;

	/**
	 * Default constructor.
	 * 
	 * @param value
	 */
	private CompositeQuery(final CompositeModifier aModifier, final QueryCompositeValue value) {
		this.queryValue = value;
		this.modifier = aModifier != null ? aModifier : CompositeModifier.DEFAULT;
	}

	@Override
	public String getFieldName() {
		return FIELD_NAME;
	}

	@Override
	public QueryCompositeValue getQueryValue() {
		return queryValue;
	}

	@Override
	public QueryCompositeValue getValue() {
		return getQueryValue();
	}

	public CompositeModifier getModifier() {
		return modifier;
	}

	/**
	 * Add {@link Query}.
	 * 
	 * @param query
	 *            {@link Query}
	 */
	public void add(final Query query) {
		if (query == null)
			return;

		getQueryValue().getQueries().add(query);
	}

	/**
	 * Create instance of {@link CompositeQuery}.
	 * 
	 * @return {@link CompositeQuery}
	 */
	public static CompositeQuery create() {
		return create(CompositeModifier.DEFAULT, new Query[] {});
	}

	/**
	 * Create instance of {@link CompositeQuery}.
	 * 
	 * @param queries
	 *            {@link List} of {@link Query}
	 * @return {@link CompositeQuery}
	 */
	public static CompositeQuery create(final Query... queries) {
		return create(CompositeModifier.DEFAULT, queries);
	}

	/**
	 * Create instance of {@link CompositeQuery}.
	 * 
	 * @param aModifier
	 *            {@link CompositeModifier}
	 * @param queries
	 *            {@link List} of {@link Query}
	 * @return {@link CompositeQuery}
	 */
	public static CompositeQuery create(final CompositeModifier aModifier, final Query... queries) {
		return new CompositeQuery(aModifier, QueryCompositeValue.create(queries));
	}

	@Override
	public String toString() {
		return "composite(" + modifier + ")" + getQueryValue();
	}

}
