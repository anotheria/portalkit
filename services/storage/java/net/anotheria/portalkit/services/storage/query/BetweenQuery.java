package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.NumberValue;
import net.anotheria.portalkit.services.storage.query.value.PairValues;
import net.anotheria.portalkit.services.storage.util.NumberUtils;

/**
 * {@link BetweenQuery}.
 * 
 * @author Alexandr Bolbat
 */
public final class BetweenQuery extends AbstractQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 2909568816125822224L;

	/**
	 * {@link BetweenModifier} modifier.
	 */
	private final BetweenModifier modifier;

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
	private BetweenQuery(final String fieldName, final PairValues queryValue, final BetweenModifier aModifier) {
		super(fieldName, queryValue);
		this.modifier = aModifier != null ? aModifier : BetweenModifier.DEFAULT;
	}

	/**
	 * Get query value as {@link PairValues}.
	 * 
	 * @return {@link PairValues}
	 */
	public PairValues getPairValue() {
		return PairValues.class.cast(getQueryValue());
	}

	public BetweenModifier getModifier() {
		return modifier;
	}

	/**
	 * Create new instance of {@link BetweenQuery} with {@link Number} values.
	 * 
	 * @param fieldName
	 *            field name
	 * @param firstValue
	 *            first value
	 * @param secondValue
	 *            second value
	 * @return {@link BetweenQuery}
	 */
	public static BetweenQuery create(final String fieldName, final Number firstValue, final Integer secondValue) {
		return create(fieldName, firstValue, secondValue, BetweenModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link BetweenQuery} with {@link Number} values.
	 * 
	 * @param fieldName
	 *            field name
	 * @param firstValue
	 *            first value
	 * @param secondValue
	 *            second value
	 * @param aModifier
	 *            query modifier
	 * @return {@link BetweenQuery}
	 */
	public static BetweenQuery create(final String fieldName, final Number firstValue, final Number secondValue, final BetweenModifier aModifier) {
		if (NumberUtils.compare(firstValue, secondValue) == 1)
			throw new IllegalArgumentException("firstValue[" + firstValue + "] can't be more than secondValue[" + secondValue + "].");

		PairValues value = PairValues.create(NumberValue.create(firstValue), NumberValue.create(secondValue));
		return new BetweenQuery(fieldName, value, aModifier);
	}

	@Override
	public String toString() {
		return "between(" + modifier + ")[" + getFieldName() + "=" + getQueryValue() + "]";
	}

}
