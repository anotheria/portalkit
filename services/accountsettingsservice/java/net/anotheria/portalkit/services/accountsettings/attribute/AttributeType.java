package net.anotheria.portalkit.services.accountsettings.attribute;

/**
 * AtributeType used in DataspaceService.
 * 
 * @author abolbat
 */
public enum AttributeType {

	/**
	 * Long type attribute.
	 */
	LONG(1),

	/**
	 * Integer type attribute.
	 */
	INT(2),

	/**
	 * String type attribute.
	 */
	STRING(3),

	/**
	 * Boolean type attribute.
	 */
	BOOLEAN(4);

	/**
	 * Attribute type id.
	 */
	private int typeId;

	/**
	 * Default private constructor.
	 * 
	 * @param aTypeId
	 *            - a attribute type id
	 */
	private AttributeType(int aTypeId) {
		this.typeId = aTypeId;
	}

	public int getTypeId() {
		return typeId;
	}

	/**
	 * Get {@link net.anotheria.anoprise.dataspace.attribute.AttributeType} type by given id.
	 *
	 * @param aTypeId
	 *            - id
	 * @return {@link net.anotheria.anoprise.dataspace.attribute.AttributeType} type or {@link RuntimeException} if type id is unknown
	 */
	public static AttributeType getTypeById(int aTypeId) {
		for (AttributeType obj : values()) {
			if (obj.getTypeId() == aTypeId)
				return obj;
		}

		throw new AssertionError("Unsupported AttributeType id: " + aTypeId);
	}

}
