package net.anotheria.portalkit.services.bounce.persistence;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Vlad Lukjanenko
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "bounce")
@NamedQueries({
        @NamedQuery(
                name = BounceDO.JPQL_GET_ALL_BOUNCES,
                query = "select b from BounceDO b"
        ),
        @NamedQuery(
                name = BounceDO.JPQL_GET_BOUNCE_BY_ID,
                query = "select b from BounceDO b where b.email = :email"
        ),
        @NamedQuery(
                name = BounceDO.JPQL_DELETE_BOUNCE_BY_ID,
                query = "delete from BounceDO b where b.email = :email"
        )

})
public class BounceDO implements Serializable {

    public static final String JPQL_GET_ALL_BOUNCES = "BounceDO.getAllBounces";
    public static final String JPQL_GET_BOUNCE_BY_ID = "BounceDO.getBounceById";
    public static final String JPQL_DELETE_BOUNCE_BY_ID = "BounceDO.deleteBounceById";


    /**
     * User email.
     * */
    @Id
    @Column
    private String email;

    /**
     * Bounce error code.
     * */
    @Column
    private int errorCode;

    /**
     * Bounce error message.
     * */
    @Column
    private String errorMessage;

    /**
     * Creation time.
     * */
    @Column
    private long created;


    /**
     * Default constructor.
     * */
    public BounceDO() {

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BounceDO bounceDO = (BounceDO) o;

        if (errorCode != bounceDO.errorCode) return false;
        if (email != null ? !email.equals(bounceDO.email) : bounceDO.email != null) return false;
        return errorMessage != null ? errorMessage.equals(bounceDO.errorMessage) : bounceDO.errorMessage == null;

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + errorCode;
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BounceDO{" +
                "email='" + email + '\'' +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", created=" + created +
                '}';
    }
}
