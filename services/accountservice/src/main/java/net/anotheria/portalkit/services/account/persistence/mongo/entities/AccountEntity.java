package net.anotheria.portalkit.services.account.persistence.mongo.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import org.bson.types.ObjectId;

/**
 * Created by Roman Stetsiuk
 */
@Entity("account")
public class AccountEntity extends BaseEntity {
    @Id
    private ObjectId id;

    private String accid;

    @Indexed
    private String name;

    @Indexed
    private String email;

    @Indexed
    private int type;

    private String tenant;

    private long regts;

    private long status;

    private String brand;

    private long daoCreated;

    private long daoUpdated;

    public AccountEntity() {
    }

    public AccountEntity(ObjectId id) {
        this.id = id;
    }

    public Account toAccout() {
        Account account = new Account();
        account.setEmail(this.email);
        account.setId(new AccountId(this.accid));
        account.setName(this.name);
        account.setRegistrationTimestamp(this.regts);
        account.setStatus(this.status);
        account.setTenant(this.tenant);
        account.setType(this.type);
        account.setBrand(this.brand);
        return account;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getAccid() {
        return accid;
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

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public long getRegts() {
        return regts;
    }

    public void setRegts(long regts) {
        this.regts = regts;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getDaoCreated() {
        return daoCreated;
    }

    public void setDaoCreated(long daoCreated) {
        this.daoCreated = daoCreated;
    }

    public long getDaoUpdated() {
        return daoUpdated;
    }

    public void setDaoUpdated(long daoUpdated) {
        this.daoUpdated = daoUpdated;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"AccountEntity\", " +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"accid\":" + (accid == null ? "null" : "\"" + accid + "\"") + ", " +
                "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
                "\"email\":" + (email == null ? "null" : "\"" + email + "\"") + ", " +
                "\"type\":\"" + type + "\"" + ", " +
                "\"tenant\":" + (tenant == null ? "null" : "\"" + tenant + "\"") + ", " +
                "\"regts\":\"" + regts + "\"" + ", " +
                "\"status\":\"" + status + "\"" + ", " +
                "\"brand\":" + (brand == null ? "null" : "\"" + brand + "\"") + ", " +
                "\"daoCreated\":\"" + daoCreated + "\"" + ", " +
                "\"daoUpdated\":\"" + daoUpdated + "\"" +
                "}";
    }
}

