package net.anotheria.portalkit.services.common.eventing;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract {@link ServiceEventConsumer}.
 *
 * @author Alex Osadchy
 */
public abstract class AbstractServiceEventConsumer implements ServiceEventConsumer {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceEventConsumer.class);

	/**
	 * Constructor.
	 *
	 * @param consumerId id of the consumer
	 * @param channelId id of the channel
	 */
	protected AbstractServiceEventConsumer(final String consumerId, final String channelId) {
		if (consumerId == null || consumerId.isEmpty())
			throw new IllegalArgumentException("consumerId is empty");
		if (channelId == null || channelId.isEmpty())
			throw new IllegalArgumentException("channelId is empty");

		//ServiceEventingConfig config = ServiceEventingConfig.getInstance();

/*		if (config.isAsynchronousMode()) {
			QueuedEventReceiver eventReceiver = new QueuedEventReceiver(consumerId, channelId, this, config.getQueueSize(), config.getSleepTime());
			EventServiceFactory.createEventService().obtainEventChannel(channelId, eventReceiver).addConsumer(eventReceiver);
			eventReceiver.start();
		} else {*/
			EventServiceFactory.createEventService().obtainEventChannel(channelId, this).addConsumer(this);
		//}
	}

	/**
	 * This method should be overridden in subclasses to do dispatching to appropriate methods depending on operation type.
	 *
	 * @param eventData {@link ServiceEventData}
	 */
	protected abstract void serviceEvent(ServiceEventData eventData);

	@Override
	public final void push(final Event e) {
		if (e != null && e.getData() instanceof ServiceEventData)
			serviceEvent(ServiceEventData.class.cast(e.getData()));
		else
			LOGGER.error("AbstractServiceEventConsumer: incoming event is null or not contains ServiceEventData");
	}
}
