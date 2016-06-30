package net.anotheria.portalkit.services.authentication.persistence.mongo.entities;

import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Roman Stetsiuk
 */
@Entity("auth_token")
public class AuthTokenEntity extends BaseEntity {
    @Id
    private ObjectId id;

    private String accid;

    private String token;

    private long daoCreated;

    private long daoUpdated;

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

    @Override
    public String toString() {
        return "{\"_class\":\"AuthPasswordEntity\", " +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"accid\":" + (accid == null ? "null" : "\"" + accid + "\"") + ", " +
                "\"token\":" + (token == null ? "null" : "\"" + token + "\"") + ", " +
                "\"daoCreated\":\"" + daoCreated + "\"" + ", " +
                "\"daoUpdated\":\"" + daoUpdated + "\"" +
                "}";
    }
}
