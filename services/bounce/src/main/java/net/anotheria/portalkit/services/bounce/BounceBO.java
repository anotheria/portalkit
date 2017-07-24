package net.anotheria.portalkit.services.bounce;

import net.anotheria.portalkit.services.bounce.persistence.BounceDO;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author Vlad Lukjanenko
 */
public class BounceBO implements Serializable {

    public static final String JPQL_GET_ALL_BOUNCES = "BounceDO.getAllBounces";
    public static final String JPQL_GET_BOUNCE_BY_ID = "BounceDO.getBounceById";
    public static final String JPQL_DELETE_BOUNCE_BY_ID = "BounceDO.deleteBounceById";

    /**
     * Generated serial version UID.
     * */
    private static final long serialVersionUID = 8035181712036584617L;

    /**
     * User email.
     * */
    private String email;

    /**
     * Bounce error code.
     * */
    private int errorCode;

    /**
     * Bounce error message.
     * */
    private String errorMessage;

    /**
     * Creation time.
     * */
    private long created = System.currentTimeMillis();


    /**
     * Default constructor.
     * */
    public BounceBO() {

    }

    /**
     * Constructor.
     * */
    public BounceBO(BounceDO bounce) {
        this.email = bounce.getEmail();
        this.errorCode = bounce.getErrorCode();
        this.errorMessage = bounce.getErrorMessage();
        this.created = bounce.getCreated();
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


    public BounceDO toDO() {

        BounceDO bounce = new BounceDO();

        bounce.setEmail(this.email);
        bounce.setErrorCode(this.errorCode);
        bounce.setErrorMessage(this.errorMessage);
        bounce.setCreated(this.created);

        return bounce;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BounceBO bounceDO = (BounceBO) o;

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
        return "BounceBO{" +
                "email='" + email + '\'' +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", created=" + created +
                '}';
    }
}
