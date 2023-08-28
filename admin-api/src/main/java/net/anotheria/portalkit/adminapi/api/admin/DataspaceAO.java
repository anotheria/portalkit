package net.anotheria.portalkit.adminapi.api.admin;

import net.anotheria.portalkit.services.accountsettings.attribute.Attribute;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DataspaceAO {

    private int type;
    private String name;
    private AccountId accountId;
    private List<Attribute> attributes = new LinkedList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataspaceAO that = (DataspaceAO) o;
        return type == that.type && Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, accountId);
    }

    @Override
    public String toString() {
        return "DataspaceAO{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", accountId=" + accountId +
                ", attributes=" + attributes +
                '}';
    }
}
