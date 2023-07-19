package net.anotheria.portalkit.adminapi.api.auth;

public class AdminAccountAO {

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "AdminAccountAO{" +
                "login='" + login + '\'' +
                '}';
    }
}
