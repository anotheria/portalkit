package net.anotheria.portalkit.adminapi.api.admin;

import net.anotheria.portalkit.services.common.AccountId;

import java.util.LinkedList;
import java.util.List;

/**
 * Admin-API representation of {@link net.anotheria.portalkit.services.account.Account} object
 */
public class AdminAccountAO {

    /**
     * Account Id
     */
    private AccountId accountId;

    /**
     * Account email
     */
    private String email;

    /**
     * Account name
     */
    private String name;

    /**
     * Account tenant
     */
    private String tenant;

    /**
     * Registration timestamp
     */
    private long registrationTimestamp;

    /**
     * Random UID
     */
    private long randomUID;

    /**
     * Human-friendly account type in string.=
     */
    private String type;

    /**
     * Human-friendly account statuses in string
     */
    private List<String> statuses = new LinkedList<>();

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public long getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public void setRegistrationTimestamp(long registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
    }

    public long getRandomUID() {
        return randomUID;
    }

    public void setRandomUID(long randomUID) {
        this.randomUID = randomUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return "AdminAccountAO{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", tenant='" + tenant + '\'' +
                ", registrationTimestamp=" + registrationTimestamp +
                ", randomUID=" + randomUID +
                ", type='" + type + '\'' +
                ", statuses=" + statuses +
                '}';
    }
}
