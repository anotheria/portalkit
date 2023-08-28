package net.anotheria.portalkit.adminapi.rest.dataspace.request;

import net.anotheria.portalkit.adminapi.api.admin.dataspace.DataspaceAttributeAO;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.LinkedList;
import java.util.List;

public class CreateDataspaceRequest {

    private int type;
    private AccountId accountId;
    private List<DataspaceAttributeAO> attributes = new LinkedList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public List<DataspaceAttributeAO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<DataspaceAttributeAO> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "CreateDataspaceRequest{" +
                "type=" + type +
                ", accountId=" + accountId +
                ", attributes=" + attributes +
                '}';
    }
}
