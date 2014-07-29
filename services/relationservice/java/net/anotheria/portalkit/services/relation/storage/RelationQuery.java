package net.anotheria.portalkit.services.relation.storage;

import net.anotheria.anoprise.dualcrud.Query;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * Created by anotheria on 7/28/14.
 */
public class RelationQuery implements Query {
    private String methodName;
    private AccountId accountId;

    public RelationQuery(AccountId accountId, String methodName) {
        this.accountId = accountId;
        this.methodName = methodName;
    }

    @Override
    public String getName() {
        return methodName;
    }

    public void setName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String getValue() {
        return null;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }
}
