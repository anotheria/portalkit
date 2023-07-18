package net.anotheria.portalkit.adminapi.rest.filter.auth;

public class AuthError {

    private final int statusCode;
    private final String msg;

    public AuthError(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }
}
