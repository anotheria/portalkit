package net.anotheria.portalkit.services.authentication.persistence.mongo.entities;

import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

/**
 * Created by Roman Stetsiuk
 */
@Entity("auth_token")
public class AuthTokenEntity extends BaseEntity {
    @Id
    private ObjectId id;

    @Indexed
    private String accid;

    private String token;

    private long daoCreated;

    private long daoUpdated;

    private long expiryTimestamp;

    private boolean multiUse;

    private boolean exclusive;

    private boolean exclusiveInType;

    private int type;

    public AuthTokenEntity() {
    }

    public AuthTokenEntity(ObjectId id) {
        this.id = id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    public boolean isMultiUse() {
        return multiUse;
    }

    public void setMultiUse(boolean multiUse) {
        this.multiUse = multiUse;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isExclusiveInType() {
        return exclusiveInType;
    }

    public void setExclusiveInType(boolean exclusiveInType) {
        this.exclusiveInType = exclusiveInType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AuthTokenEntity{" +
                "id=" + id +
                ", accid='" + accid + '\'' +
                ", token='" + token + '\'' +
                ", daoCreated=" + daoCreated +
                ", daoUpdated=" + daoUpdated +
                ", expiryTimestamp=" + expiryTimestamp +
                ", multiUse=" + multiUse +
                ", exclusive=" + exclusive +
                ", exclusiveInType=" + exclusiveInType +
                ", type=" + type +
                '}';
    }
}
