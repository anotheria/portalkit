package net.anotheria.portalkit.services.online.events;

import net.anotheria.portalkit.services.common.eventing.AbstractServiceEventConsumer;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityCleanUpEvent;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityEvent;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityLoginEvent;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityLogoutEvent;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default consumer implementation for {@link net.anotheria.portalkit.services.online.OnlineService}.
 *
 * @author h3llka
 */
public abstract class OnlineActivityServiceEventConsumer extends AbstractServiceEventConsumer {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineActivityServiceEventConsumer.class);

	/**
	 * Constructor.
	 */
	protected OnlineActivityServiceEventConsumer() {
		super("OnlineActivityServiceEventConsumer", OnlineActivityServiceEventSupplier.EVENT_CHANNEL_NAME);
	}

	@Override
	protected void serviceEvent(ServiceEventData eventData) {

		if (eventData == null)
			throw new IllegalArgumentException("eventData bean is null");


		if (!(eventData instanceof OnlineActivityEvent)) {
			LOGGER.warn("Unsupported eventData[" + eventData + "] received!");
			return;
		}


		OnlineActivityEvent event = OnlineActivityEvent.class.cast(eventData);
		switch (event.getOperation()) {
			case AUTO_CLEANUP:
				if (!(event instanceof OnlineActivityCleanUpEvent)) {
					LOGGER.warn("Unexpected event[" + event + "] received! But OnlineActivityCleanUpEvent expected. Skipping!");
					return;
				}
				accountActivityCleanUp(OnlineActivityCleanUpEvent.class.cast(event));
				break;
			case ACCOUNT_LOGGED_IN:
				if (!(event instanceof OnlineActivityLoginEvent)) {
					LOGGER.warn("Unexpected event[" + event + "] received! But OnlineActivityLoginEvent expected. Skipping!");
					return;
				}
				accountLoggedIn(OnlineActivityLoginEvent.class.cast(event));
				break;
			case ACCOUNT_LOGGED_OUT:
				if (!(event instanceof OnlineActivityLogoutEvent)) {
					LOGGER.warn("Unexpected event[" + event + "] received! But OnlineActivityLogoutEvent expected. Skipping!");
					return;
				}
				accountLoggedOut(OnlineActivityLogoutEvent.class.cast(event));
				break;
			case ACCOUNT_ACTIVITY_UPDATE:
				if (!(event instanceof OnlineActivityUpdateEvent)) {
					LOGGER.warn("Unexpected event[" + event + "] received! But OnlineActivityUpdateEvent expected. Skipping!");
					return;
				}
				accountActivityUpdate(OnlineActivityUpdateEvent.class.cast(event));
				break;
			default:

				LOGGER.warn("Unsupported event[" + event + "] received! Skipping! Operation[" + event.getOperation() + "]");
				break;
		}
	}

	/**
	 * Marker for logIn events. Called in case if  {@link OnlineActivityLoginEvent} triggered.
	 *
	 * @param event {@link OnlineActivityLoginEvent}
	 */
	public abstract void accountLoggedIn(final OnlineActivityLoginEvent event);

	/**
	 * Marker for logOut events. Called in case if  {@link OnlineActivityLogoutEvent} triggered.
	 *
	 * @param event {@link OnlineActivityLogoutEvent}
	 */
	public abstract void accountLoggedOut(final OnlineActivityLogoutEvent event);

	/**
	 * Marker for activityUpdate events. Called in case if  {@link OnlineActivityUpdateEvent} triggered.
	 *
	 * @param event {@link OnlineActivityUpdateEvent}
	 */
	public abstract void accountActivityUpdate(final OnlineActivityUpdateEvent event);

	/**
	 * Marker for autoCleanUp events. Called in case if  {@link OnlineActivityCleanUpEvent} triggered.
	 *
	 * @param event {@link OnlineActivityCleanUpEvent}
	 */
	public abstract void accountActivityCleanUp(final OnlineActivityCleanUpEvent event);
}
