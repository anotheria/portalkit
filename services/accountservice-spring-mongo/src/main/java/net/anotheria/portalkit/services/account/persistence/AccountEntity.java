package net.anotheria.portalkit.services.account.persistence;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pk-accounts")
public class AccountEntity {
    /**
     * The account id. The format is UUID as string.
     */
    @Id private String id;

    //all fields below correspond with Account fields

    private String name;
    private String email;
    private int type;
    private long status;
    private long registrationTimestamp;
    private String tenant;
    private int randomUID;
    private String brand;

    public AccountEntity() {
    }

    public AccountEntity(Account account){
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

    public Account toAccount(){
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
