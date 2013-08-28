package net.anotheria.portalkit.services.common.eventing;

import net.anotheria.anoprise.eventservice.EventServicePushConsumer;

/**
 * Marker interface for all objects that are interested in events about some service operations.
 *
 * @author Alex Osadchy
 */
public interface ServiceEventConsumer extends EventServicePushConsumer {
}
