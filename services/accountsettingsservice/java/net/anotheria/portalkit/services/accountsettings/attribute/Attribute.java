package net.anotheria.portalkit.services.accountsettings.attribute;

import java.io.Serializable;

import net.anotheria.util.BasicComparable;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Abstract attribute used in dataspace.
 * 
 * @author lrosenberg
 * @author abolbat
 * @author dagafonov
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	@Type(value = StringAttribute.class, name = "STRING"), 
	@Type(value = IntAttribute.class, name = "INT"),
	@Type(value = LongAttribute.class, name = "LONG"), 
	@Type(value = BooleanAttribute.class, name = "BOOLEAN") 
})
public abstract class Attribute implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -7944645330790901903L;

	/**
	 * Attribute name.
	 */
	private String name;

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - attribute name
	 */
	public Attribute(String aName) {
		name = aName;
	}

	public String getName() {
		return name;
	}

	/**
	 * Get attribute value as string.
	 * 
	 * @return {@link String}
	 */
	public abstract String getValueAsString();

	/**
	 * Get attribute type.
	 * 
	 * @return {@link net.anotheria.anoprise.dataspace.attribute.AttributeType}
	 */
	public abstract AttributeType getType();

	/**
	 * Create new attribute by type id with given name and string value.
	 * 
	 * @param aTypeId
	 *            - type id
	 * @param aName
	 *            - attribute name
	 * @param aStringValue
	 *            - attribute value
	 * @return {@link net.anotheria.anoprise.dataspace.attribute.Attribute}
	 */
	public static Attribute createAttribute(int aTypeId, String aName, String aStringValue) {
		return createAttribute(AttributeType.getTypeById(aTypeId), aName, aStringValue);
	}

	/**
	 * Create new attribute by type with given name and string value.
	 * 
	 * @param aType
	 *            - type
	 * @param aName
	 *            - attribute name
	 * @param aStringValue
	 *            - attribute value
	 * @return {@link net.anotheria.anoprise.dataspace.attribute.Attribute}
	 */
	public static Attribute createAttribute(AttributeType aType, String aName, String aStringValue) {
		switch (aType) {
			case LONG:
				return new LongAttribute(aName, aStringValue);
			case INT:
				return new IntAttribute(aName, aStringValue);
			case STRING:
				return new StringAttribute(aName, aStringValue);
			case BOOLEAN:
				return new BooleanAttribute(aName, aStringValue);
			default:
				throw new AssertionError("Unsupported AttributeType: " + aType);
		}
	}

	@Override
	public String toString() {
		return getName() + " = " + getValueAsString();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object o) {
		return o instanceof Attribute ? BasicComparable.compareString(getName(), ((Attribute) o).getName()) == 0
				&& getType() == ((Attribute) o).getType()
				&& BasicComparable.compareString(getValueAsString(), ((Attribute) o).getValueAsString()) == 0 : false;
	}

}
