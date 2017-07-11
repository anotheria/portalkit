package net.anotheria.portalkit.services.subscription.persistence.subscription;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Vlad Lukjanenko
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "cancellation")
@NamedQueries({
        @NamedQuery(
                name = CancellationDO.JPQL_GET_BY_ACCOUNT_ID,
                query = "select c from CancellationDO c where c.userId = :userId"
        ),
        @NamedQuery(
                name = CancellationDO.JPQL_GET_ALL,
                query = "select c from CancellationDO c"
        ),
        @NamedQuery(
                name = CancellationDO.JPQL_DELETE_CANCELLATION,
                query = "delete from CancellationDO c where c.userId = :userId"
        )
})
public class CancellationDO {

    public static final String JPQL_GET_BY_ACCOUNT_ID = "CancellationDO.getById";
    public static final String JPQL_GET_ALL = "CancellationDO.getAll";
    public static final String JPQL_DELETE_CANCELLATION = "CancellationDO.deleteById";

    /**
     * User id.
     * */
    @Column @Id
    private String userId;

    /**
     * Registration timestamp.
     * */
    @Column
    private long cancellationOriginalDate;

    /**
     * Registration timestamp.
     * */
    @Column
    private long cancellationExecutionDate;

    /**
     * Registration timestamp.
     * */
    @Column
    private String cancellationReason;

    /**
     * Registration timestamp.
     * */
    @Column
    private long created;


    /**
     * Default constructor.
     * */
    public CancellationDO() {

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


    /**
     *
     * */
    public static CancellationDO getInstance() {

        CancellationDO cancellation = new CancellationDO();

        return cancellation;
    }
}
