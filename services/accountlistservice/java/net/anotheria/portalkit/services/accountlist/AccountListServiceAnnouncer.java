package net.anotheria.portalkit.services.accountlist;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServicePushSupplier;
import net.anotheria.anoprise.eventservice.util.QueueFullException;
import net.anotheria.anoprise.eventservice.util.QueuedEventSender;
import net.anotheria.anoprise.sessiondistributor.cache.events.SDCacheEventAnnouncer;
import net.anotheria.portalkit.services.common.AccountId;

import org.apache.log4j.Logger;

/**
 * 
 * @author dagafonov
 * 
 */
public class AccountListServiceAnnouncer implements EventServicePushSupplier {

	/**
	 * Channel name.
	 */
	public static final String EVENT_CHANNEL_NAME = "AccountListServiceCache";

	/**
	 * Originator.
	 */
	public static final String ORIGINATOR = "-AccountListCache-";

	/**
	 * Property name which will avoid ES - start in local mode... for tests etc.
	 */
	private static final String JUNITTEST = "JUNITTEST";

	/**
	 * Sender for the events.
	 */
	private QueuedEventSender eventSender;

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(SDCacheEventAnnouncer.class);

	/**
	 * 
	 */
	public AccountListServiceAnnouncer() {
		EventChannel eventChannel = EventServiceFactory.createEventService().obtainEventChannel(EVENT_CHANNEL_NAME, this);
		boolean unitTesting = Boolean.valueOf(System.getProperty(JUNITTEST, String.valueOf(false)));
		eventSender = new QueuedEventSender(EVENT_CHANNEL_NAME + "-sender", eventChannel, LOG);
		if (unitTesting) {
			eventSender.setSynchedMode(true);
		} else {
			eventSender.start();
		}
	}

	/**
	 * Create event type.
	 * 
	 * @param owner
	 * @param target
	 * @param listName
	 */
	public void accountListCreate(AccountId owner, AccountId target, String listName) {
		deliver(AccountListEvent.create(owner, target, listName));
	}

	/**
	 * Update event type.
	 * 
	 * @param owner
	 * @param target
	 * @param listName
	 */
	public void accountListUpdate(AccountId owner, AccountId target, String listName) {
		deliver(AccountListEvent.update(owner, target, listName));
	}

	/**
	 * Delete event type.
	 * 
	 * @param owner
	 * @param target
	 * @param listName
	 */
	public void accountListDelete(AccountId owner, AccountId target, String listName) {
		deliver(AccountListEvent.delete(owner, target, listName));
	}

	/**
	 * Internal method that delivers the events into the channel.
	 * 
	 * @param eventData
	 *            {@link AccountListEvent}
	 */
	private void deliver(AccountListEvent eventData) {
		Event event = new Event(eventData);
		event.setOriginator(ORIGINATOR);
		try {
			eventSender.push(event);
		} catch (QueueFullException e) {
			LOG.error("Couldn't publish event due to queue overflow " + event);
		}
	}
}
