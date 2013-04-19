package net.anotheria.portalkit.services.account.events;

import net.anotheria.portalkit.services.account.Account;

/**
 * Event class for {@link AccountEventOperation#UPDATE} operation.
 *
 * @author dagafonov
 */
public class AccountUpdateEvent extends AccountEvent {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -1543698926830293981L;

    /**
     *
     */
    private Account updated;

    public Account getUpdated() {
        return updated;
    }

    public void setUpdated(Account updated) {
        this.updated = updated;
    }

    /**
     * Update account instance creation.
     *
     * @param account
     * @return {@link AccountUpdateEvent}
     */
    public static AccountEvent save(Account beforeUpdate, Account afterUpdated) {
        AccountUpdateEvent event = new AccountUpdateEvent();
        event.setAccount(beforeUpdate);
        event.setUpdated(afterUpdated);
        event.setEventType(AccountEventOperation.UPDATE);
        return event;
    }

}
