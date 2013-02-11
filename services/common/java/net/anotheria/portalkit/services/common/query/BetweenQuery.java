package net.anotheria.portalkit.services.common.query;

import net.anotheria.portalkit.services.common.query.value.IntegerValue;
import net.anotheria.portalkit.services.common.query.value.LongValue;
import net.anotheria.portalkit.services.common.query.value.PairValues;
import net.anotheria.portalkit.services.common.query.value.QueryValue;

/**
 * {@link BetweenQuery}.
 * 
 * @author Alexandr Bolbat
 */
public class BetweenQuery extends AbstractQuery implements Query {

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
	 * Get {@link QueryValue} as {@link PairValues}.
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
	 * Create new instance of {@link BetweenQuery} with {@link Integer} values.
	 * 
	 * @param fieldName
	 *            field name
	 * @param firstValue
	 *            first value
	 * @param secondValue
	 *            second value
	 * @return {@link BetweenQuery}
	 */
	public static BetweenQuery create(final String fieldName, final Integer firstValue, final Integer secondValue) {
		return create(fieldName, firstValue, secondValue, BetweenModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link BetweenQuery} with {@link Integer} values.
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
	public static BetweenQuery create(final String fieldName, final Integer firstValue, final Integer secondValue, final BetweenModifier aModifier) {
		PairValues value = PairValues.create(IntegerValue.create(firstValue), IntegerValue.create(secondValue));
		return new BetweenQuery(fieldName, value, aModifier);
	}

	/**
	 * Create new instance of {@link BetweenQuery} with {@link Long} values.
	 * 
	 * @param fieldName
	 *            field name
	 * @param firstValue
	 *            first value
	 * @param secondValue
	 *            second value
	 * @return {@link BetweenQuery}
	 */
	public static BetweenQuery create(final String fieldName, final Long firstValue, final Long secondValue) {
		return create(fieldName, firstValue, secondValue, BetweenModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link BetweenQuery} with {@link Long} values.
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
	public static BetweenQuery create(final String fieldName, final Long firstValue, final Long secondValue, final BetweenModifier aModifier) {
		PairValues value = PairValues.create(LongValue.create(firstValue), LongValue.create(secondValue));
		return new BetweenQuery(fieldName, value, aModifier);
	}

	@Override
	public String toString() {
		return "between(" + modifier + ")[" + getFieldName() + "=" + getQueryValue() + "]";
	}

}
