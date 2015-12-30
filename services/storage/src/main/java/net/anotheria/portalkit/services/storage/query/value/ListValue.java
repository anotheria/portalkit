package net.anotheria.portalkit.services.storage.query.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link ListValue} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class ListValue implements QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 8062983331885507538L;

	/**
	 * Values.
	 */
	private final List<Serializable> valuesList;

	/**
	 * Default constructor.
	 * 
	 * @param values
	 *            values
	 */
	public ListValue(final Serializable... values) {
		this.valuesList = new ArrayList<Serializable>();

		if (values != null && values.length > 0)
			valuesList.addAll(Arrays.asList(values));
	}

	@Override
	public ListValue getValue() {
		return this;
	}

	public List<Serializable> getValues() {
		return valuesList;
	}

	/**
	 * Create new instance of {@link ListValue}.
	 * 
	 * @param values
	 *            values
	 * @return {@link ListValue}
	 */
	public static <T extends Serializable> ListValue create(final T... values) {
		return new ListValue(values);
	}

	@Override
	public String toString() {
		return String.valueOf(getValues());
	}

}
