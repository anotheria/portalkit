package net.anotheria.portalkit.adminapi.rest.dataspace.request;

import net.anotheria.portalkit.services.common.AccountId;

public class DeleteDataspaceRequest {

    private AccountId accountId;
    private int type;

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DeleteDataspaceRequest{" +
                "accountId=" + accountId +
                ", type=" + type +
                '}';
    }
}
