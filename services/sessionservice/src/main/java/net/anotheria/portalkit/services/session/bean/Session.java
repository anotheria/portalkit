package net.anotheria.portalkit.services.session.bean;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.session.bean.attribute.Attribute;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Session implements Serializable {

    /**
     * Generated default serialVersionUID.
     */
    private static final long serialVersionUID = 518948669364600729L;

    private SessionKey key;

    private long creationTimestamp;

    private long modifiedTimestamp;

    public Session() {
    }

    public Session(AccountId accountId, String authToken) {
        this.key = new SessionKey(accountId.getInternalId(), authToken);
    }

    /**
     * Session extra attributes.
     */
    private HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();

    public SessionKey getKey() {
        return key;
    }

    public void setKey(SessionKey key) {
        this.key = key;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(long modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    /**
     * Get attribute by given name from dataspace.
     *
     * @param attributeName - attribute name
     * @return - attribute or null if no attribute with given name
     */
    public Attribute getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * Add new attribute to dataspace.
     *
     * @param attributeName - new attribute name
     * @param attribute     - new attribute
     */
    public void addAttribute(String attributeName, Attribute attribute) {
        attributes.put(attributeName, attribute);
    }

    /**
     * Remove attribute by given name from dataspace.
     *
     * @param attributeName - given attribute name
     */
    public void removeAttribute(String attributeName) {
        attributes.remove(attributeName);
    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        return Objects.equals(key, session.key);
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Session{" +
                "authToken='" + key + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
