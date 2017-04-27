package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * @author Vlad Lukjanenko
 */
public final class AccountBuilder {

    /**
     * The account id.
     */
    private AccountId id;
    /**
     * Name that is used by the user. Can be email.
     */
    private String name;

    /**
     * Email of the user.
     */
    private String email;

    /**
     * Type of the user.
     */
    private int type;

    /**
     * This field is used to set different statuses of the account. Feel free to use whatever you like, but
     * we would advice to use this field as bitmap and combine multiple statuses.
     */
    private long status;

    /**
     * The creation timestamp for this account.
     */
    private long registrationTimestamp;

    /**
     * Tenant. This can be an agency, a locale or whatever, you use to separate the accounts. In case of ASG based
     * sites, tenant should be the cms-language.
     */
    private String tenant;

    /**
     * Random UID.
     */
    private int randomUID;


    public AccountBuilder id(AccountId accountId) {
        this.id = accountId;
        return this;
    }


    public AccountBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AccountBuilder email(String email) {
        this.email = email;
        return this;
    }

    public AccountBuilder type(int type) {
        this.type = type;
        return this;
    }

    public AccountBuilder status(long status) {
        this.status = status;
        return this;
    }

    public AccountBuilder registrationTimestamp(long registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
        return this;
    }

    public AccountBuilder tenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    public AccountBuilder randomUID(int randomUID) {
        this.randomUID = randomUID;
        return this;
    }

    public Account build() {

        Account acc = new Account(id);

        acc.setName(this.name);
        acc.setEmail(this.email);
        acc.setRegistrationTimestamp(this.registrationTimestamp);
        acc.setType(this.type);
        acc.setStatus(this.status);
        acc.setTenant(this.tenant);
        acc.setRandomUID(randomUID);

        return acc;
    }
}
