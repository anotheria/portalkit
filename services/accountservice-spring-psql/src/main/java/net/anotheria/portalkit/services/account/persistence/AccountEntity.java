package net.anotheria.portalkit.services.account.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * JPA entity for the {@code account} table. The physical schema is created and evolved by the Flyway
 * migrations under {@code persistence.jdbc.migrations.common} (copied verbatim from the legacy
 * accountservice), therefore the column names must match the lowercase identifiers Postgres produced.
 */
@Entity
@Table(name = "account")
public class AccountEntity {

    /**
     * The account id. The format is UUID as string.
     */
    @Id
    @Column(name = "id", length = 128)
    private String id;

    //all fields below correspond with Account fields

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "type")
    private int type;

    @Column(name = "status")
    private long status;

    @Column(name = "regts")
    private long registrationTimestamp;

    @Column(name = "tenant", length = 10)
    private String tenant;

    @Column(name = "randomuid")
    private int randomUID;

    @Column(name = "brand", length = 32)
    private String brand;

    /**
     * Bookkeeping columns, maintained exactly like the legacy {@code AccountDAO} (epoch millis).
     */
    @Column(name = "dao_created")
    private Long daoCreated;

    @Column(name = "dao_updated")
    private Long daoUpdated;

    public AccountEntity() {
    }

    public AccountEntity(Account account) {
        this.id = account.getId().getInternalId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.type = account.getType();
        this.status = account.getStatus();
        this.registrationTimestamp = account.getRegistrationTimestamp();
        this.tenant = account.getTenant();
        this.randomUID = account.getRandomUID();
        this.brand = account.getBrand();
    }

    @PrePersist
    void onCreate() {
        long now = System.currentTimeMillis();
        if (daoCreated == null) {
            daoCreated = now;
        }
        daoUpdated = 0L;
    }

    @PreUpdate
    void onUpdate() {
        daoUpdated = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public void setRegistrationTimestamp(long registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public int getRandomUID() {
        return randomUID;
    }

    public void setRandomUID(int randomUID) {
        this.randomUID = randomUID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getDaoCreated() {
        return daoCreated;
    }

    public Long getDaoUpdated() {
        return daoUpdated;
    }

    public Account toAccount() {
        Account ret = new Account();
        ret.setId(new AccountId(id));
        ret.setName(name);
        ret.setEmail(email);
        ret.setType(type);
        ret.setStatus(status);
        ret.setRegistrationTimestamp(registrationTimestamp);
        ret.setTenant(tenant);
        ret.setRandomUID(randomUID);
        ret.setBrand(brand);
        return ret;
    }
}
