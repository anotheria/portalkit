package net.anotheria.portalkit.services.accountarchive.event;

import net.anotheria.portalkit.services.accountarchive.ArchivedAccount;
import net.anotheria.portalkit.services.accountarchive.event.data.ArchivedAccountEventData;
import net.anotheria.portalkit.services.accountarchive.event.data.ArchivedAccountUpdateEventData;
import net.anotheria.portalkit.services.common.eventing.AbstractServiceEventSupplier;

/**
 * @author VKoulakov
 * @since 24.04.14 16:32
 */
public class AccountArchiveServiceEventSupplier extends AbstractServiceEventSupplier {

    public static final String EVENT_CHANNEL = "AccountArchiveServiceEventChannel";
    public static final String EVENT_SUPPLIER = "AccountArchiveServiceEventSupplier";

    /**
     * Constructor.
     */
    public AccountArchiveServiceEventSupplier() {
        super(EVENT_SUPPLIER, EVENT_CHANNEL);
    }

    public void accountCreated(final ArchivedAccount archivedAccount){
        if (archivedAccount == null){
            throw new IllegalArgumentException("archivedAccount is null");
        }
        send(new ArchivedAccountEventData(AccountArchiveServiceOperationType.ACCOUNT_CREATE, archivedAccount));
    }

    public void accountUpdated(final ArchivedAccount beforeUpdate, final ArchivedAccount afterUpdate){
        if (beforeUpdate == null){
            throw new IllegalArgumentException("beforeUpdate is null");
        }
        if (afterUpdate == null){
            throw new IllegalArgumentException("afterUpdate is null");
        }
        send(new ArchivedAccountUpdateEventData(beforeUpdate, afterUpdate));
    }

    public void accountDeleted(final ArchivedAccount archivedAccount){
        if (archivedAccount == null){
            throw new IllegalArgumentException("archivedAccount is null");
        }
        send(new ArchivedAccountEventData(AccountArchiveServiceOperationType.ACCOUNT_DELETE, archivedAccount));
    }

}
