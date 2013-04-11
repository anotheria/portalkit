package net.anotheria.portalkit.services.online.events;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Account logged out activity event.
 *
 * @author h3llka
 */
public class OnlineActivityLogoutEvent extends OnlineActivityEvent {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -2836440306504689765L;
    /**
     * OnlineActivityLoginEvent 'accountId'.
     */
    private AccountId accountId;

    /**
     * Constructor.
     *
     * @param account {@link AccountId}
     */
    public OnlineActivityLogoutEvent(final AccountId account) {
        super(OnlineActivityESOperation.ACCOUNT_LOGGED_OUT);
        this.accountId = account;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return "OnlineActivityLogoutEvent{" +
                "accountId=" + accountId +
                '}';
    }
}
