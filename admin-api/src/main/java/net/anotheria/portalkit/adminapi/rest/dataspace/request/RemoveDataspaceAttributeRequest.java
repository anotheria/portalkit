package net.anotheria.portalkit.adminapi.rest.dataspace.request;

public class RemoveDataspaceAttributeRequest {

    private String accountId;
    private int dataspaceId;
    private String attributeName;

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

    @Override
    public String toString() {
        return "RemoveDataspaceAttributeRequest{" +
                "accountId='" + accountId + '\'' +
                ", dataspaceId=" + dataspaceId +
                ", attributeName='" + attributeName + '\'' +
                '}';
    }
}
