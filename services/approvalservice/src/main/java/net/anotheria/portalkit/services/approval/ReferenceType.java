package net.anotheria.portalkit.services.approval;

/**
 * Describes reference type of tickets.
 * 
 * @author Vlad Lukjanenko
 * 
 */
public enum ReferenceType implements IReferenceType {

	/**
	 * Text message.
	 */
	MESSAGE(1),

	/**
	 * Photo.
	 * */
	PICTURE(2),

	/**
	 * Hello message text.
	 */
	HELLO_MESSAGE(3);

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
	 * @param value value.
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

	@Override
	public long getId() {
		return value;
	}

	@Override
	public String getName() {
		return this.name();
	}
}
