package net.anotheria.portalkit.services.common.eventing;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.util.QueueFullException;
import net.anotheria.anoprise.eventservice.util.QueuedEventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract service event supplier.
 *
 * @author Alex Osadchy
 */
public abstract class AbstractServiceEventSupplier {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceEventSupplier.class);

	/**
	 * {@link QueuedEventSender} instance.
	 */
	private final QueuedEventSender eventSender;

	/**
	 * Constructor.
	 *
	 * @param supplierId id of the supplier
	 * @param channelId if of the channel
	 */
	protected AbstractServiceEventSupplier(final String supplierId, final String channelId) {
		if (supplierId == null || supplierId.isEmpty())
			throw new IllegalArgumentException("supplierId is empty");
		if (channelId == null || channelId.isEmpty())
			throw new IllegalArgumentException("channelId is empty");

		ServiceEventingConfig config = ServiceEventingConfig.getInstance();
		LOGGER.info("Eventing config for "+this.getClass().getSimpleName()+config);

		eventSender = new QueuedEventSender(supplierId, channelId, config.getQueueSize(), config.getSleepTime(), LOGGER);

		if (config.isAsynchronousMode())
			eventSender.start();
		else
			eventSender.setSynchedMode(true);
	}

	/**
	 * Sends event with given {@link ServiceEventData}.
	 *
	 * @param eventData {@link ServiceEventData}
	 */
	protected final void send(final ServiceEventData eventData) {
		if (eventData == null)
			throw new IllegalArgumentException("eventData is null");

		try {
			eventSender.push(new Event(eventData));
		} catch (QueueFullException e) {
			String failMsg = "AbstractServiceEventSupplier: QueuedEventSender queue overflow";
			LOGGER.error(failMsg, e);
		}
	}
}
