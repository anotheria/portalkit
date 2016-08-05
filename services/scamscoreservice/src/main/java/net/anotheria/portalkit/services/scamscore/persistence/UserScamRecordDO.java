package net.anotheria.portalkit.services.scamscore.persistence;

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
@Table(name = "user_scam_record")
@NamedQueries({
        @NamedQuery(
                name = UserScamRecordDO.JPQL_GET_ALL_RECORDS,
                query = "select u from UserScamRecordDO u"
        ),
        @NamedQuery(
                name = UserScamRecordDO.JPQL_GET_RECORD_BY_ID,
                query = "select u from UserScamRecordDO u where u.id = :id"
        ),
        @NamedQuery(
                name = UserScamRecordDO.JPQL_DELETE_RECORD_BY_ID,
                query = "delete from UserScamRecordDO u where u.id = :id"
        )
})
public class UserScamRecordDO {

    public static final String JPQL_GET_ALL_RECORDS = "UserScamRecordDO.getAllRecords";
    public static final String JPQL_GET_RECORD_BY_ID = "UserScamRecordDO.getRecordById";
    public static final String JPQL_DELETE_RECORD_BY_ID = "UserScamRecordDO.deleteRecordById";

    /**
     * Id.
     * */
    @Id
    @Column
    private String id;

    /**
     * Total score.
     * */
    @Column
    private int total_score;

    /**
     * Creation timestamp.
     * */
    @Column
    private long created = System.currentTimeMillis();

    /**
     * Time of updating.
     * */
    @Column
    private long updated;


    /**
     * Default constructor.
     * */
    public UserScamRecordDO() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }


    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof UserScamRecordDO)) {
            return false;
        }

        UserScamRecordDO scamRecord = (UserScamRecordDO) obj;

        if (!this.id.equals(scamRecord.getId())) {
            return false;
        }

        return true;
    }
}