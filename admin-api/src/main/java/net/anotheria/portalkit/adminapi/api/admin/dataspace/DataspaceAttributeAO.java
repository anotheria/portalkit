package net.anotheria.portalkit.adminapi.api.admin.dataspace;

import net.anotheria.portalkit.services.accountsettings.attribute.AttributeType;

public class DataspaceAttributeAO {

    private String attributeName;
    private String attributeValue;
    private AttributeType type;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DataspaceAttributeAO{" +
                "attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                ", type=" + type +
                '}';
    }
}
