package net.anotheria.portalkit.adminapi.api.auth;

public class AuthTokenAO {

    private String token;
    private AdminAccountAO account;
    private long expirationTime;
    private long timestamp;

    public AuthTokenAO() {
    }

    public AuthTokenAO(String token, AdminAccountAO account, long expirationTime, long timestamp) {
        this.token = token;
        this.account = account;
        this.expirationTime = expirationTime;
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AdminAccountAO getAccount() {
        return account;
    }

    public void setAccount(AdminAccountAO account) {
        this.account = account;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AuthTokenAO{" +
                "token='" + token + '\'' +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
