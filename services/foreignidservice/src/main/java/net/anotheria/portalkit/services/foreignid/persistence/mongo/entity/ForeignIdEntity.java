package net.anotheria.portalkit.services.foreignid.persistence.mongo.entity;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Roman Stetsiuk
 */
@Entity("foreignId")
public class ForeignIdEntity extends BaseEntity {
    @Id
    private ObjectId id;

    private AccountId accountId;
    private int sourceId;
    private String foreignid;

    private long daoCreated;
    private long daoUpdated;

    public ForeignIdEntity() {
    }

    public ForeignIdEntity(ObjectId id) {
        this.id = id;
    }

    public ForeignId toForeignId() {
        ForeignId foreignId = new ForeignId();

        return foreignId;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getForeignid() {
        return foreignid;
    }

    public void setForeignid(String foreignid) {
        this.foreignid = foreignid;
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
        return "{\"_class\":\"ForeignIdEntity\", " +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"accountId\":" + (accountId == null ? "null" : "\"" + accountId + "\"") + ", " +
                "\"sourceId\":\"" + sourceId + "\"" + ", " +
                "\"foreignid\":" + (foreignid == null ? "null" : "\"" + foreignid + "\"") + ", " +
                "\"daoCreated\":\"" + daoCreated + "\"" + ", " +
                "\"daoUpdated\":\"" + daoUpdated + "\"" +
                "}";
    }
}

