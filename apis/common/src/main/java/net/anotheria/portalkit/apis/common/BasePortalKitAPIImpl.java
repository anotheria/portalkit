package net.anotheria.portalkit.apis.common;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.portalkit.services.common.AccountId;

public class BasePortalKitAPIImpl extends AbstractAPIImpl {
    /**
     * Returns current account id as AccountId object.
     * @return
     */
    protected AccountId getCurrentAccountId() {
        return new AccountId(getCurrentUserId());
    }

    /**
     * Returns currently logged in AccountId. If there is no logged in user an exception is thrown (this way the extending code doesn't need to check itself).
     * @return
     */
    protected AccountId getCurrentLoggedInAccountId() throws APIException {
        return new AccountId(getLoggedInUserId());
    }
}
