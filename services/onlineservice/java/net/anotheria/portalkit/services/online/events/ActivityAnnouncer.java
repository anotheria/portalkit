package net.anotheria.portalkit.services.online.events;

import java.util.List;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServicePushSupplier;
import net.anotheria.anoprise.eventservice.util.QueueFullException;
import net.anotheria.anoprise.eventservice.util.QueuedEventSender;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.OnlineServiceConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link OnlineActivityEvent} announcer.
 *
 * @author h3llka
 */
public class ActivityAnnouncer implements EventServicePushSupplier {

    /**
     * Channel name.
     */
    public static final String EVENT_CHANNEL_NAME = "OnlineActivityEventChannel";
    /**
     * Originator.
     */
    public static final String ORIGINATOR = "-OnlineService-";
    /**
     * Property name which will avoid ES - start in local mode... for  tests etc.
     */
    private static final String JUNITTEST = "JUNITTEST";

    /**
     * Logger instance.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ActivityAnnouncer.class);

    /**
     * Sender for the events.
     */
    private QueuedEventSender eventSender;


    /**
     * Constructor.
     */
    public ActivityAnnouncer() {
        OnlineServiceConfiguration config = OnlineServiceConfiguration.getInstance();
        EventChannel eventChannel = EventServiceFactory.createEventService().obtainEventChannel(EVENT_CHANNEL_NAME, this);
        boolean unitTesting = Boolean.valueOf(System.getProperty(JUNITTEST, String.valueOf(false)));
        eventSender = new QueuedEventSender(EVENT_CHANNEL_NAME + "-sender", eventChannel, config.getEventChannelQueueSize(), config.getEventChannelQueueSleepTime(), LOG);
        if (unitTesting) {
            eventSender.setSynchedMode(true);
        } else {
            eventSender.start();
        }
    }

    /**
     * Send {@link OnlineActivityLoginEvent}.
     *
     * @param account       {@link AccountId}
     * @param lastLoginTime last login time stamp
     */
    public void accountLoggedIn(final AccountId account, final long lastLoginTime) {
        deliver(OnlineActivityEvent.login(account, lastLoginTime));
    }

    /**
     * Send {@link OnlineActivityUpdateEvent}.
     *
     * @param account          {@link AccountId}
     * @param lastActivityTime last activity time stamp
     */
    public void accountActivityChange(final AccountId account, final long lastActivityTime) {
        deliver(OnlineActivityEvent.activityUpdate(account, lastActivityTime));
    }

    /**
     * Send {@link OnlineActivityLogoutEvent}.
     *
     * @param account {@link AccountId}
     */
    public void accountLoggedOut(final AccountId account) {
        deliver(OnlineActivityEvent.logOut(account));
    }

    /**
     * Send {@link OnlineActivityCleanUpEvent}.
     *
     * @param accounts {@link AccountId} collection
     */
    public void cleanUp(final List<AccountId> accounts) {
        deliver(OnlineActivityEvent.cleanUp(accounts));
    }


    /**
     * Internal method that delivers the events into the channel.
     *
     * @param eventData {@link OnlineActivityEvent}
     */
    private void deliver(OnlineActivityEvent eventData) {
        Event event = new Event(eventData);
        event.setOriginator(ORIGINATOR);
        try {
            eventSender.push(event);
        } catch (QueueFullException e) {
            LOG.error("Couldn't publish event due to queue overflow " + event);
        }
    }
}
