package net.anotheria.portalkit.services.pushtoken.persistence;

import net.anotheria.portalkit.services.common.AccountId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "push_token")
public class PushTokenEntity {

    @EmbeddedId
    private PushTokenEntityPK id;

    @CreationTimestamp
    @Column(name = "dao_created")
    private Date daoCreated;

    @UpdateTimestamp
    @Column(name = "dao_modified")
    private Date daoModified;

    public PushTokenEntity() {
    }

    public PushTokenEntity(String accountId, String token) {
        this.id = new PushTokenEntityPK(accountId, token);
    }

    public PushTokenEntityPK getId() {
        return id;
    }

    public void setId(PushTokenEntityPK id) {
        this.id = id;
    }

    public AccountId getAccountId() {
        return new AccountId(id.accountId);
    }

    public String getToken() {
        return id.token;
    }

    public Date getDaoCreated() {
        return daoCreated;
    }

    public void setDaoCreated(Date daoCreated) {
        this.daoCreated = daoCreated;
    }

    public Date getDaoModified() {
        return daoModified;
    }

    public void setDaoModified(Date daoModified) {
        this.daoModified = daoModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PushTokenEntity that = (PushTokenEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PushTokenEntity{" +
                "id=" + id +
                '}';
    }

    @Embeddable
    public static class PushTokenEntityPK implements Serializable {

        @Column(name = "accid")
        private String accountId;

        @Column(name = "token")
        private String token;

        public PushTokenEntityPK() {
        }

        public PushTokenEntityPK(String accountId, String token) {
            this.accountId = accountId;
            this.token = token;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PushTokenEntityPK that = (PushTokenEntityPK) o;
            return Objects.equals(accountId, that.accountId) && Objects.equals(token, that.token);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountId, token);
        }

        @Override
        public String toString() {
            return "PushTokenEntityPK{" +
                    "accountId='" + accountId + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

}
