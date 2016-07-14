package net.anotheria.portalkit.services.approval;

/**
 * Describes reference type of tickets.
 * 
 * @author Vlad Lukjanenko
 * 
 */
public enum ReferenceType {

	/**
	 * Text message.
	 */
	MESSAGE(1);

	/**
	 * Reference type integer representation.
	 * */
	long value;

	/**
	 * Constructor.
	 *
	 * @param value 	integer value.
	 * */
	ReferenceType(long value) {
		this.value = value;
	}

	/**
	 * Returns integer value of {@link ReferenceType}.
	 *
	 * @return integer representation.
	 * */
	public long getValue() {
		return value;
	}

	/**
	 * Gets {@link ReferenceType} acording to value.
	 *
	 * @return {@link ReferenceType}
	 * */
	public static ReferenceType valueOf(long value) {

		for (ReferenceType type : ReferenceType.values()) {

			if (type.value == value) {
				return type;
			}
		}

		return null;
	}
}
