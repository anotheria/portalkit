package net.anotheria.portalkit.services.authentication.persistence.mongo.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Roman Stetsiuk
 */
@Entity("auth_passwd")
public class AuthPasswordEntity extends BaseEntity {
    @Id
    private ObjectId id;

    private String accid;

    private String password;

    private long daoCreated;

    private long daoUpdated;

    public AuthPasswordEntity() {
    }

    public AuthPasswordEntity(ObjectId id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public AuthPasswordBO toAuthPasswordBO() {
        AuthPasswordBO authPasswordBO = new AuthPasswordBO();
        authPasswordBO.setAccid(accid);
        authPasswordBO.setPassword(password);
        authPasswordBO.setDaoCreated(daoCreated);
        authPasswordBO.setDaoUpdated(daoUpdated);

        return authPasswordBO;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"AuthPasswordEntity\", " +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"accid\":" + (accid == null ? "null" : "\"" + accid + "\"") + ", " +
                "\"password\":" + (password == null ? "null" : "\"" + password + "\"") + ", " +
                "\"daoCreated\":\"" + daoCreated + "\"" + ", " +
                "\"daoUpdated\":\"" + daoUpdated + "\"" +
                "}";
    }
}

