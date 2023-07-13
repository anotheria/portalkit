package net.anotheria.portalkit.adminapi.api;

import net.anotheria.anoplass.api.APIException;

public class AccountIdEmptyAdminAPIException extends APIException {

    public AccountIdEmptyAdminAPIException() {
    }

    public AccountIdEmptyAdminAPIException(String message) {
        super(message);
    }

    public AccountIdEmptyAdminAPIException(String message, Exception cause) {
        super(message, cause);
    }
}
