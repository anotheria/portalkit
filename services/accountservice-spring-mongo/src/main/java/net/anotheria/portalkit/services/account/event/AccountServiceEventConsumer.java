package net.anotheria.portalkit.services.account.event;

import net.anotheria.portalkit.services.account.event.data.AccountCreateEventData;
import net.anotheria.portalkit.services.account.event.data.AccountDeleteEventData;
import net.anotheria.portalkit.services.account.event.data.AccountUpdateEventData;
import net.anotheria.portalkit.services.common.eventing.AbstractServiceEventConsumer;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AbstractServiceEventConsumer} for Account Service.
 *
 * @author Alex Osadchy
 */
public abstract class AccountServiceEventConsumer extends AbstractServiceEventConsumer {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceEventConsumer.class);

	/**
	 * Default constructor.
	 */
	protected AccountServiceEventConsumer() {
		super("AccountServiceEventConsumer", AccountServiceEventSupplier.CHANNEL_NAME);
	}

	@Override
	protected final void serviceEvent(final ServiceEventData eventData) {
		if (eventData == null)
			throw new IllegalArgumentException("eventData is null");

		switch (AccountServiceOperationType.valueOf(eventData.getOperationType())) {
			case ACCOUNT_CREATE: {
				if (eventData  instanceof  AccountCreateEventData)
					accountCreated(AccountCreateEventData.class.cast(eventData));
				else
					LOGGER.error("unexpected type of event data");

				break;
			}

			case ACCOUNT_UPDATE: {
				if (eventData instanceof  AccountUpdateEventData)
					accountUpdated(AccountUpdateEventData.class.cast(eventData));
				else
					LOGGER.error("unexpected type of event data");

				break;
			}

			case ACCOUNT_DELETE: {
				if (eventData instanceof  AccountDeleteEventData)
					accountDeleted(AccountDeleteEventData.class.cast(eventData));
				else
					LOGGER.error("unexpected type of event data");

				break;
			}
		}
	}

	// concrete listeners should implement methods below

	/**
	 * Called on account create.
	 *
	 * @param eventData {@link AccountCreateEventData}
	 */
	public abstract void accountCreated(AccountCreateEventData eventData);

	/**
	 * Called on account update.
	 *
	 * @param eventData {@link AccountUpdateEventData}
	 */
	public abstract void accountUpdated(AccountUpdateEventData eventData);

	/**
	 * Called on account delete.
	 *
	 * @param eventData {@link AccountDeleteEventData}
	 */
	public abstract void accountDeleted(AccountDeleteEventData eventData);

}
