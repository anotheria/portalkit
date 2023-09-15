package net.anotheria.portalkit.adminapi.rest.dataspace.request;

import net.anotheria.portalkit.services.accountsettings.attribute.AttributeType;

public class AddDataspaceAttributeRequest {

    private String accountId;
    private int dataspaceId;
    private String attributeName;
    private String attributeValue;
    private AttributeType type;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getDataspaceId() {
        return dataspaceId;
    }

    public void setDataspaceId(int dataspaceId) {
        this.dataspaceId = dataspaceId;
    }

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
        return "AddDataspaceAttributeRequest{" +
                "accountId='" + accountId + '\'' +
                ", dataspaceId=" + dataspaceId +
                ", attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                ", type=" + type +
                '}';
    }
}
