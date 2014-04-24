package net.anotheria.portalkit.services.accountarchive.event;

import net.anotheria.portalkit.services.accountarchive.event.data.ArchivedAccountEventData;
import net.anotheria.portalkit.services.accountarchive.event.data.ArchivedAccountUpdateEventData;
import net.anotheria.portalkit.services.common.eventing.AbstractServiceEventConsumer;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author VKoulakov
 * @since 24.04.14 16:46
 */
public abstract class AccountArchiveServiceEventConsumer extends AbstractServiceEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountArchiveServiceEventConsumer.class);

    public static final String EVENT_CONSUMER = "AccountArchiveServiceEventConsumer";

    /**
     * Constructor.
     *
     */
    protected AccountArchiveServiceEventConsumer() {
        super(EVENT_CONSUMER, AccountArchiveServiceEventSupplier.EVENT_CHANNEL);
    }

    @Override
    protected void serviceEvent(ServiceEventData eventData) {
        if (eventData == null){
            throw new IllegalArgumentException("eventData is null");
        }
        switch (AccountArchiveServiceOperationType.valueOf(eventData.getOperationType())){
            case ACCOUNT_CREATE:
                if (eventData instanceof ArchivedAccountEventData){
                    archivedAccountCreated(ArchivedAccountEventData.class.cast(eventData));
                } else {
                    LOGGER.error("unexpected type of eventData");
                }
                break;
            case ACCOUNT_DELETE:
                if (eventData instanceof ArchivedAccountEventData){
                    archivedAccountDeleted(ArchivedAccountEventData.class.cast(eventData));
                } else {
                    LOGGER.error("unexpected type of eventData");
                }
                break;
            case ACCOUNT_UPDATE:
                if (eventData instanceof ArchivedAccountUpdateEventData){
                    archivedAccountUpdated(ArchivedAccountUpdateEventData.class.cast(eventData));
                } else {
                    LOGGER.error("unexpected type of eventData");
                }
                break;
        }
    }

    protected abstract void archivedAccountCreated(ArchivedAccountEventData eventData);

    protected abstract void archivedAccountUpdated(ArchivedAccountUpdateEventData eventData);

    protected abstract void archivedAccountDeleted(ArchivedAccountEventData eventData);
}
