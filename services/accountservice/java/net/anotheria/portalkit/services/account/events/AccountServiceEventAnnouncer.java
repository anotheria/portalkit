package net.anotheria.portalkit.services.account.events;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServicePushSupplier;
import net.anotheria.anoprise.eventservice.util.QueueFullException;
import net.anotheria.anoprise.eventservice.util.QueuedEventSender;
import net.anotheria.portalkit.services.account.Account;

import org.apache.log4j.Logger;

/**
 * 
 * @author dagafonov
 * 
 */
public class AccountServiceEventAnnouncer implements EventServicePushSupplier {

	/**
	 * Channel name.
	 */
	public static final String EVENT_CHANNEL_NAME = "AccountServiceCache";

	/**
	 * Originator.
	 */
	public static final String ORIGINATOR = "-AccountCache-";

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
	private static final Logger LOG = Logger.getLogger(AccountServiceEventAnnouncer.class);

	/**
	 * 
	 */
	public AccountServiceEventAnnouncer() {
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
	public void accountCreate(Account account) {
		deliver(AccountEvent.create(account));
	}

	/**
	 * Update event type.
	 * @param toSave 
	 * 
	 * @param owner
	 * @param target
	 * @param listName
	 */
	public void accountUpdate(Account before, Account after) {
		deliver(AccountUpdateEvent.save(before, after));
	}

	/**
	 * Delete event type.
	 * 
	 * @param owner
	 * @param target
	 * @param listName
	 */
	public void accountDelete(Account account) {
		deliver(AccountEvent.delete(account));
	}

	/**
	 * Internal method that delivers the events into the channel.
	 * 
	 * @param eventData
	 *            {@link AccountListEvent}
	 */
	private void deliver(AccountEvent eventData) {
		Event event = new Event(eventData);
		event.setOriginator(ORIGINATOR);
		try {
			eventSender.push(event);
		} catch (QueueFullException e) {
			LOG.error("Couldn't publish event due to queue overflow " + event);
		}
	}
}
