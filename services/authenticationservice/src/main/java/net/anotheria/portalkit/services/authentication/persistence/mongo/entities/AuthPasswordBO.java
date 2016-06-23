package net.anotheria.portalkit.services.authentication.persistence.mongo.entities;

/**
 * Created by Roman Stetsiuk
 */
public class AuthPasswordBO{
    private String accid;

    private String password;

    private long daoCreated;

    private long daoUpdated;

    public AuthPasswordBO() {
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
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


    @Override
    public String toString() {
        return "{\"_class\":\"AuthPasswordBO\", " +
                "\"accid\":" + (accid == null ? "null" : "\"" + accid + "\"") + ", " +
                "\"password\":" + (password == null ? "null" : "\"" + password + "\"") + ", " +
                "\"daoCreated\":\"" + daoCreated + "\"" + ", " +
                "\"daoUpdated\":\"" + daoUpdated + "\"" +
                "}";
    }
}

