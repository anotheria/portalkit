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


    public AccountBuilder name(String aName) {
        name = aName;
        return this;
    }

    public AccountBuilder email(String anEmail) {
        email = anEmail;
        return this;
    }

    public AccountBuilder type(int aType) {
        type = aType;
        return this;
    }

    public AccountBuilder status(long aStatus) {
        status = aStatus;
        return this;
    }

    public AccountBuilder registrationTimestamp(long aRegistrationTimestamp) {
        registrationTimestamp = aRegistrationTimestamp;
        return this;
    }

    public AccountBuilder tenant(String aTenant) {
        tenant = aTenant;
        return this;
    }

    public AccountBuilder randomUID(int aRandomUID) {
        randomUID = aRandomUID;
        return this;
    }

    public Account build() {

        Account acc = new Account(id);

        acc.setName(name);
        acc.setEmail(email);
        acc.setRegistrationTimestamp(registrationTimestamp);
        acc.setType(type);
        acc.setStatus(status);
        acc.setTenant(tenant);
        acc.setRandomUID(randomUID);

        return acc;
    }
}
