package net.anotheria.portalkit.services.accountarchive.event.data;

import net.anotheria.portalkit.services.accountarchive.ArchivedAccount;
import net.anotheria.portalkit.services.accountarchive.event.AccountArchiveServiceOperationType;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;

/**
 * @author VKoulakov
 * @since 24.04.14 16:36
 */
public class ArchivedAccountEventData extends ServiceEventData {

    private static final long serialVersionUID = 2495259079641732846L;
    private AccountArchiveServiceOperationType operationType;
    private ArchivedAccount account;

    public ArchivedAccountEventData(AccountArchiveServiceOperationType operationType, ArchivedAccount account) {
        if (operationType == null){
            throw new IllegalArgumentException("operationType is null");
        }
        if (account == null){
            throw new IllegalArgumentException("account is null");
        }
        this.operationType = operationType;
        this.account = account;
    }

    @Override
    public String getOperationType() {
        return operationType.name();
    }

    public ArchivedAccount getAccount() {
        return account;
    }
}
