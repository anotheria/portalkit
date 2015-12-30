package net.anotheria.portalkit.services.online.events;

import net.anotheria.util.StringUtils;

/**
 * Possible operation types for OnlineActivity stuff.
 *
 * @author h3llka
 */
public enum OnlineActivityESOperation {
	/**
	 * Logged in operation.
	 */
	ACCOUNT_LOGGED_IN("logged_In"),
	/**
	 * Activity time change operation.
	 */
	ACCOUNT_ACTIVITY_UPDATE("activity_Update"),
	/**
	 * Account goes offline.
	 */
	ACCOUNT_LOGGED_OUT("logged_Out"),
	/**
	 * Auto clean up operation.
	 */
	AUTO_CLEANUP("auto_clean_Up");
	/**
	 * String representation.
	 */
	private String value;

	/**
	 * Constructor.
	 *
	 * @param aValue string representation
	 */
	OnlineActivityESOperation(final String aValue) {
		this.value = aValue;
	}

	public String getValue() {
		return value;
	}

	/**
	 * Allow to resolve {@link OnlineActivityESOperation} via it string representation.
	 * In case if such dies not exists {@link EnumConstantNotPresentException} will occur.
	 *
	 * @param value string representation
	 * @return {@link OnlineActivityESOperation}
	 */
	public static OnlineActivityESOperation getByValue(final String value) {
		if (StringUtils.isEmpty(value))
			throw new IllegalArgumentException("Illegal value passed");

		for (OnlineActivityESOperation operation : values())
			if (operation.value.equals(value))
				return operation;


		throw new EnumConstantNotPresentException(OnlineActivityESOperation.class, value);
	}
}
