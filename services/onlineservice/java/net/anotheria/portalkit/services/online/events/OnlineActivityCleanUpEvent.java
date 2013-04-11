package net.anotheria.portalkit.services.online.events;

import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

/**
 * Activity clean up event.
 *
 * @author h3llka
 */
public class OnlineActivityCleanUpEvent extends OnlineActivityEvent {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -7420747306562340340L;
    /**
     * Accounts collection. Contains all cleaned up accounts.
     */
    private List<AccountId> accounts;

    /**
     * Constructor.
     *
     * @param accounts {@link AccountId} collection
     */
    public OnlineActivityCleanUpEvent(final List<AccountId> accounts) {
        super(OnlineActivityESOperation.AUTO_CLEANUP);
        this.accounts = accounts;
    }

    public List<AccountId> getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        return "OnlineActivityCleanUpEvent{" +
                "accounts=" + accounts +
                '}';
    }
}
