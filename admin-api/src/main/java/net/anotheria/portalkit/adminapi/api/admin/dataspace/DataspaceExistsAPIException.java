package net.anotheria.portalkit.adminapi.api.admin.dataspace;

import net.anotheria.anoplass.api.APIException;

public class DataspaceExistsAPIException extends APIException {

    public DataspaceExistsAPIException() {
        super();
    }

    public DataspaceExistsAPIException(String message) {
        super(message);
    }

    public DataspaceExistsAPIException(String message, Exception cause) {
        super(message, cause);
    }
}
