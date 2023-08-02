package net.anotheria.portalkit.adminapi.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

@ConfigureMe(name = "pk-admin-api-authentication-config")
public class AuthenticationConfig {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationConfig.class);

    @Configure
    @SerializedName("@accounts")
    private AccountConfig[] accounts;

    public AuthenticationConfig() {
        try {
            ConfigurationManager.INSTANCE.configure(this);
        } catch (final IllegalArgumentException e) {
            log.error("Configuration fail[" + e.getMessage() + "]. Check for pk-admin-api-authentication-config.json.");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Holder class idiom.
     */
    private static class HolderClass {
        /**
         * Singleton instance.
         */
        private static final AuthenticationConfig INSTANCE;

        static {
            INSTANCE = new AuthenticationConfig();
        }
    }

    public static AuthenticationConfig getInstance() {
        return AuthenticationConfig.HolderClass.INSTANCE;
    }

    public AccountConfig getAccountByLogin(String login) {
        for (AccountConfig account : accounts) {
            if (account.getLogin().equals(login)) {
                return account;
            }
        }
        return null;
    }

    public AccountConfig[] getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountConfig[] accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "AuthenticationConfig{" +
                "accounts=" + Arrays.toString(accounts) +
                '}';
    }

    @ConfigureMe
    public static class AccountConfig {

        @Configure
        private String login;

        @Configure
        private String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccountConfig that = (AccountConfig) o;
            return Objects.equals(login, that.login) && Objects.equals(password, that.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(login, password);
        }

        @Override
        public String toString() {
            return "AccountConfig{" +
                    "login='" + login + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
