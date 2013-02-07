package net.anotheria.portalkit.services.common.persistence.mongo.shared;

import java.io.Serializable;

/**
 * Value object for testing purposes.
 * 
 * @author Alexandr Bolbat
 */
public class TestVO implements Serializable, Cloneable {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 1836585731182672495L;

	/**
	 * {@link TestVO} unique identifier.
	 */
	private String id;

	/**
	 * Some integer value.
	 */
	private int intValue;

	/**
	 * Some boolean value.
	 */
	private boolean booleanValue;

	/**
	 * Some {@link TestVO} sub-object.
	 */
	private TestVO subObject;

	/**
	 * Default constructor.
	 */
	public TestVO() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param aId
	 *            {@link TestVO} unique identifier
	 */
	public TestVO(final String aId) {
		this.id = aId;
	}

	public String getId() {
		return id;
	}

	public void setId(final String aId) {
		this.id = aId;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(final int aIntValue) {
		this.intValue = aIntValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(final boolean aBooleanValue) {
		this.booleanValue = aBooleanValue;
	}

	public TestVO getSubObject() {
		return subObject;
	}

	public void setSubObject(final TestVO aSubObject) {
		this.subObject = aSubObject;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestVO [id=");
		builder.append(id);
		builder.append(", intValue=");
		builder.append(intValue);
		builder.append(", booleanValue=");
		builder.append(booleanValue);
		builder.append(", subObject=");
		builder.append(subObject);
		builder.append("]");
		return builder.toString();
	}

	@Override
	protected TestVO clone() {
		TestVO result = new TestVO(id);
		result.setIntValue(intValue);
		result.setBooleanValue(booleanValue);
		result.setSubObject(getSubObject());
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestVO other = (TestVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
