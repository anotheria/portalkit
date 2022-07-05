package net.anotheria.portalkit.apis.common.authentication;

import net.anotheria.anoplass.api.APIException;

public class AccountNotFoundException extends APIException {
    public AccountNotFoundException(String username){
        super("AccountNotFound: "+username);
    }
}
