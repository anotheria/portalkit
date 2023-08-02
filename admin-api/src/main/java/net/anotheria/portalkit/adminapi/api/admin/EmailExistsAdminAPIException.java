package net.anotheria.portalkit.adminapi.api.admin;

import net.anotheria.anoplass.api.APIException;

public class EmailExistsAdminAPIException extends APIException {

    public EmailExistsAdminAPIException() {
    }

    public EmailExistsAdminAPIException(String message) {
        super(message);
    }

    public EmailExistsAdminAPIException(String message, Exception cause) {
        super(message, cause);
    }
}
