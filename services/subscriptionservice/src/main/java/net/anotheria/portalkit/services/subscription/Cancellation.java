package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.subscription.persistence.subscription.CancellationDO;

import java.io.Serializable;

/**
 * @author Vlad Lukjanenko
 */
public class Cancellation implements Serializable {

    /**
     * Generated serial version UID.
     * */
    private static final long serialVersionUID = -5047295234114893900L;

    /**
     * User id.
     * */
    private String userId;

    /**
     * Registration timestamp.
     * */
    private long cancellationOriginalDate;

    /**
     * Registration timestamp.
     * */
    private long cancellationExecutionDate;

    /**
     * Registration timestamp.
     * */
    private String cancellationReason;

    /**
     * Registration timestamp.
     * */
    private long created;


    /**
     * Default constructor.
     * */
    public Cancellation() {

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCancellationOriginalDate() {
        return cancellationOriginalDate;
    }

    public void setCancellationOriginalDate(long cancellationOriginalDate) {
        this.cancellationOriginalDate = cancellationOriginalDate;
    }

    public long getCancellationExecutionDate() {
        return cancellationExecutionDate;
    }

    public void setCancellationExecutionDate(long cancellationExecutionDate) {
        this.cancellationExecutionDate = cancellationExecutionDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }


    public static Cancellation getInstance(CancellationDO cancellation) {

        Cancellation instance = new Cancellation();

        instance.setUserId(cancellation.getUserId());
        instance.setCancellationExecutionDate(cancellation.getCancellationExecutionDate());
        instance.setCancellationOriginalDate(cancellation.getCancellationOriginalDate());
        instance.setCancellationReason(cancellation.getCancellationReason());
        instance.setCreated(cancellation.getCreated());

        return instance;
    }

    /**
     *
     * */
    public CancellationDO toPersist() {

        CancellationDO cancellation = new CancellationDO();

        cancellation.setUserId(this.userId);
        cancellation.setCancellationExecutionDate(this.cancellationExecutionDate);
        cancellation.setCancellationOriginalDate(this.cancellationOriginalDate);
        cancellation.setCancellationReason(this.cancellationReason);
        cancellation.setCreated(this.created);

        return cancellation;
    }
}
