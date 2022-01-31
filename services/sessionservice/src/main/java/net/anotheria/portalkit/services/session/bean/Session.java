package net.anotheria.portalkit.services.session.bean;

import net.anotheria.anoprise.dataspace.attribute.Attribute;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Session implements Serializable {

    /**
     * Generated default serialVersionUID.
     */
    private static final long serialVersionUID = 518948669364600729L;

    private String key;

    /**
     * Session extra attributes.
     */
    private HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();

    public Session(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
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
