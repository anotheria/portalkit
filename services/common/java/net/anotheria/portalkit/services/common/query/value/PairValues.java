package net.anotheria.portalkit.services.common.query.value;

/**
 * {@link PairValues} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class PairValues implements QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -6484398818614743471L;

	/**
	 * First {@link QueryValue}.
	 */
	private final QueryValue firstValue;

	/**
	 * Second {@link QueryValue}.
	 */
	private final QueryValue secondValue;

	/**
	 * Default constructor.
	 * 
	 * @param aFirstValue
	 *            first value
	 * @param aSecondValue
	 *            second value
	 */
	public PairValues(final QueryValue aFirstValue, final QueryValue aSecondValue) {
		if (aFirstValue == null)
			throw new IllegalArgumentException("aFirstValue argument is null");
		if (aSecondValue == null)
			throw new IllegalArgumentException("aSecondValue argument is null");

		this.firstValue = aFirstValue;
		this.secondValue = aSecondValue;
	}

	@Override
	public PairValues getValue() {
		return this;
	}

	public QueryValue getFirstValue() {
		return firstValue;
	}

	public QueryValue getSecondValue() {
		return secondValue;
	}

	/**
	 * Create new instance of {@link PairValues}.
	 * 
	 * @param aFirstValue
	 *            first value
	 * @param aSecondValue
	 *            second value
	 * @return {@link PairValues}
	 */
	public static PairValues create(final QueryValue aFirstValue, final QueryValue aSecondValue) {
		return new PairValues(aFirstValue, aSecondValue);
	}

	@Override
	public String toString() {
		return firstValue + "|" + secondValue;
	}

}
