package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.portalkit.services.accountsettings.attribute.Attribute;
import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Dataspace bean class.
 * 
 * @author lrosenberg
 * @since 11.12.12 17:20
 */
public class Dataspace implements Serializable {

	/**
	 * Generated default serialVersionUID.
	 */
	private static final long serialVersionUID = 355356911003047256L;

	/**
	 * 
	 */
	private AccountSettingsKey key;

	/**
	 * Dataspace attributes.
	 */
	private HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();

	/**
	 * Default constructor.
	 */
	public Dataspace() {
		this.key = new AccountSettingsKey();
	}

	/**
	 * Default constructor.
	 * 
	 * @param accountId
	 *            - account id
	 * @param type
	 *            - dataspace type
	 */
	public Dataspace(String accountId, DataspaceType type) {
		this();
		this.key.setAccountId(accountId);
		this.key.setDataspaceId(type.getId());
	}

	public Dataspace(AccountId accountId, DataspaceType type){
		this(accountId.getInternalId(), type);
	}



	public AccountSettingsKey getKey() {
		return key;
	}

	public void setKey(AccountSettingsKey key) {
		this.key = key;
	}

	/**
	 * Get attribute by given name from dataspace.
	 * 
	 * @param attributeName
	 *            - attribute name
	 * @return - attribute or null if no attribute with given name
	 */
	public Attribute getAttribute(String attributeName) {
		return attributes.get(attributeName);
	}

	/**
	 * Add new attribute to dataspace.
	 * 
	 * @param attributeName
	 *            - new attribute name
	 * @param attribute
	 *            - new attribute
	 */
	public void addAttribute(String attributeName, Attribute attribute) {
		attributes.put(attributeName, attribute);
	}

	/**
	 * Remove attribute by given name from dataspace.
	 * 
	 * @param attributeName
	 *            - given attribute name
	 */
	public void removeAttribute(String attributeName) {
		attributes.remove(attributeName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	public HashMap<String, Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dataspace other = (Dataspace) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Dataspace [key=" + key + ", attributes=" + attributes + "]";
	}

}
