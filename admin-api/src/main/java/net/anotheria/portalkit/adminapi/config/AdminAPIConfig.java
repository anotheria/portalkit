package net.anotheria.portalkit.adminapi.config;

import com.google.gson.annotations.SerializedName;
import net.anotheria.portalkit.adminapi.api.auth.provider.AuthProviderType;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

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

    @Configure
    @SerializedName("@dataspaces")
    private DataspaceConfig[] dataspaces;

    @Configure
    private AuthProviderType authProvider;

    public AdminAPIConfig() {
        try {
            ConfigurationManager.INSTANCE.configure(this);
        } catch (final IllegalArgumentException e) {
            log.warn("Configuration fail[" + e.getMessage() + "]. Relaying on defaults.");
        }
    }

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

    public AuthProviderType getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProviderType authProviderType) {
        this.authProvider = authProviderType;
    }

    public DataspaceConfig[] getDataspaces() {
        return dataspaces;
    }

    public void setDataspaces(DataspaceConfig[] dataspaces) {
        this.dataspaces = dataspaces;
    }

    public AccountStatusConfig getStatus(String statusName) {
        for (AccountStatusConfig status : getStatuses()) {
            if (status.getName().equals(statusName)) {
                return status;
            }
        }
        return null;
    }

    public AccountTypeConfig getType(String typeName) {
        for (AccountTypeConfig type : getTypes()) {
            if (type.getName().equals(typeName)) {
                return type;
            }
        }
        return null;
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
        }
    }

    @Override
    public String toString() {
        return "AdminAPIConfig{" +
                "statuses=" + Arrays.toString(statuses) +
                ", types=" + Arrays.toString(types) +
                ", tokens=" + Arrays.toString(tokens) +
                ", authProvider=" + authProvider +
                '}';
    }

    @ConfigureMe
    public static class AccountStatusConfig {

        @Configure
        private int value;

        @Configure
        private String name;

        public AccountStatusConfig() {
        }

        public AccountStatusConfig(int value, String name) {
            this.value = value;
            this.name = name;
        }

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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccountStatusConfig that = (AccountStatusConfig) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
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

        public AccountTypeConfig() {
        }

        public AccountTypeConfig(int value, String name) {
            this.value = value;
            this.name = name;
        }

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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccountTypeConfig that = (AccountTypeConfig) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
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

        /**
         * Token is used for "signAs" feature
         */
        @Configure
        private boolean signAs;

        public AuthTokenConfig() {
        }

        public AuthTokenConfig(int value, String name, boolean signAs) {
            this.value = value;
            this.name = name;
            this.signAs = signAs;
        }

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

        public boolean isSignAs() {
            return signAs;
        }

        public void setSignAs(boolean signAs) {
            this.signAs = signAs;
        }

        @Override
        public String toString() {
            return "AuthTokenConfig{" +
                    "value=" + value +
                    ", name='" + name + '\'' +
                    ", signAs=" + signAs +
                    '}';
        }
    }

    @ConfigureMe
    public static class DataspaceConfig {

        @Configure
        private int value;

        @Configure
        private String name;

        public DataspaceConfig() {
        }

        public DataspaceConfig(int value, String name) {
            this.value = value;
            this.name = name;
        }

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
            return "DataspaceConfig{" +
                    "type=" + value +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


}
