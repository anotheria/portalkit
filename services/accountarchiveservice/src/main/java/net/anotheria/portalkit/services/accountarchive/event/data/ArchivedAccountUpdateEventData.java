package net.anotheria.portalkit.services.accountarchive.event.data;

import net.anotheria.portalkit.services.accountarchive.ArchivedAccount;
import net.anotheria.portalkit.services.accountarchive.event.AccountArchiveServiceOperationType;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;

/**
 * @author VKoulakov
 * @since 24.04.14 16:42
 */
public class ArchivedAccountUpdateEventData extends ServiceEventData {
    private static final long serialVersionUID = -5256234785452364111L;
    private ArchivedAccount beforeUpdate;
    private ArchivedAccount afterUpdate;

    public ArchivedAccountUpdateEventData(ArchivedAccount beforeUpdate, ArchivedAccount afterUpdate) {
        this.beforeUpdate = beforeUpdate;
        this.afterUpdate = afterUpdate;
    }

    public ArchivedAccount getAfterUpdate() {
        return afterUpdate;
    }

    public ArchivedAccount getBeforeUpdate() {
        return beforeUpdate;
    }

    @Override
    public String getOperationType() {
        return AccountArchiveServiceOperationType.ACCOUNT_UPDATE.name();
    }
}
