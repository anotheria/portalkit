package net.anotheria.portalkit.apis.common.authentication;

import net.anotheria.anoplass.api.APIException;

public class IllegalCredentialsForLoginException extends APIException {
    public IllegalCredentialsForLoginException(String reason){
        super("Illegal credentials: "+reason);
    }
}
