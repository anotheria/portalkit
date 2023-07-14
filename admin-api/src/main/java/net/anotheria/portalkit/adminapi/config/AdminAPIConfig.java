package net.anotheria.portalkit.adminapi.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@ConfigureMe(name = "pk-admin-api-config")
public class AdminAPIConfig {

    private static final Logger log = LoggerFactory.getLogger(AdminAPIConfig.class);

    @Configure
    @SerializedName("@statuses")
    private AccountStatusConfig[] statuses;

    @Configure
    @SerializedName("@types")
    private AccountTypeConfig[] types;

    @Configure
    @SerializedName("@tokens")
    private AuthTokenConfig[] tokens;

    public AccountStatusConfig[] getStatuses() {
        return statuses;
    }

    public void setStatuses(AccountStatusConfig[] statuses) {
        this.statuses = statuses;
    }

    public AccountTypeConfig[] getTypes() {
        return types;
    }

    public void setTypes(AccountTypeConfig[] types) {
        this.types = types;
    }

    public AuthTokenConfig[] getTokens() {
        return tokens;
    }

    public void setTokens(AuthTokenConfig[] tokens) {
        this.tokens = tokens;
    }

    public static AdminAPIConfig getInstance() {
        return AdminAPIConfig.HolderClass.INSTANCE;
    }

    /**
     * Holder class idiom.
     */
    private static class HolderClass {
        /**
         * Singleton instance.
         */
        private static final AdminAPIConfig INSTANCE;

        static {
            INSTANCE = new AdminAPIConfig();
            try {
                ConfigurationManager.INSTANCE.configure(INSTANCE);
            } catch (Exception e) {
                log.error("GooglePubSubConfig configuration load failed: {}", e.getMessage(), e);
                throw e;
            }
        }
    }

    @Override
    public String toString() {
        return "AdminAPIConfig{" +
                "statuses=" + Arrays.toString(statuses) +
                ", types=" + Arrays.toString(types) +
                ", tokens=" + Arrays.toString(tokens) +
                '}';
    }

    @ConfigureMe
    public static class AccountStatusConfig {

        @Configure
        private int value;

        @Configure
        private String name;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "AccountStatusConfig{" +
                    "statusValue=" + value +
                    ", statusName='" + name + '\'' +
                    '}';
        }
    }

    @ConfigureMe
    public static class AccountTypeConfig {

        @Configure
        private int value;

        @Configure
        private String name;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "AccountTypeConfig{" +
                    "typeValue=" + value +
                    ", typeName='" + name + '\'' +
                    '}';
        }
    }

    @ConfigureMe
    public static class AuthTokenConfig {

        @Configure
        private int value;

        @Configure
        private String name;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "AuthTokenConfig{" +
                    "tokenValue=" + value +
                    ", tokenName='" + name + '\'' +
                    '}';
        }
    }


}
