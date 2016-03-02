package net.anotheria.portalkit.services.match.entity;

import org.slf4j.LoggerFactory;

/**
 * Match type for admin actions.
 *
 * @author ivanbatura
 */
public enum MatchType {
	/**
	 * Best match.
	 */
	BEST_MATCH(0),
	/**
	 * Next match.
	 */
	NEXT_MATCH(1),
	/**
	 * Match.
	 */
	MATCH(2);
	/**
	 * Value.
	 */
	private int value;

	/**
	 * Constructor.
	 *
	 * @param value
	 * 		value
	 */
	private MatchType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Returns {@link MatchType} by given value. If there is no such {@link MatchType} with specified value - return {@code null}.
	 *
	 * @param value
	 * 		{@code int} value
	 * @return corresponding {@link MatchType} or {@code null} if there is no such gender
	 */
	public static MatchType valueOf(final int value) {
		for (MatchType matchType : MatchType.values())
			if (matchType.getValue() == value)
				return matchType;
		LoggerFactory.getLogger(MatchType.class).warn("Method MatchType.valueOf() called with invalid value=[" + value + "]");
		return null;
	}
}
