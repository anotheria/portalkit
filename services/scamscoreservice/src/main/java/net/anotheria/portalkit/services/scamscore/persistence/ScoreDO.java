package net.anotheria.portalkit.services.scamscore.persistence;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Objects;
import javax.persistence.Table;

/**
 * @author Vlad Lukjanenko
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "detection_score")
@NamedQueries({
        @NamedQuery(
                name = ScoreDO.JPQL_GET_ALL_RECORDS,
                query = "select s from ScoreDO s"
        ),
        @NamedQuery(
                name = ScoreDO.JPQL_GET_RECORD_BY_ID,
                query = "select s from ScoreDO s where s.id = :id"
        ),
        @NamedQuery(
                name = ScoreDO.JPQL_DELETE_RECORD_BY_ID,
                query = "delete from ScoreDO s where s.id = :id"
        ),
        @NamedQuery(
                name = ScoreDO.JPQL_GET_RECORDS_BY_USER,
                query = "select s from ScoreDO s where s.userId = :userId order by s.created desc"
        ),
        @NamedQuery(
                name = ScoreDO.JPQL_DELETE_RECORD_BY_USER_UD,
                query = "delete from ScoreDO s where s.userId = :userId"
        )
})
public class ScoreDO {

    public static final String JPQL_GET_ALL_RECORDS = "ScoreDO.getAllRecords";
    public static final String JPQL_GET_RECORD_BY_ID = "ScoreDO.getRecordById";
    public static final String JPQL_GET_RECORDS_BY_USER = "ScoreDO.getRecordsByUser";
    public static final String JPQL_DELETE_RECORD_BY_ID = "ScoreDO.deleteRecordById";
    public static final String JPQL_DELETE_RECORD_BY_USER_UD = "ScoreDO.deleteRecordByUserId";

    /**
     * Id.
     * */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private long id;

    /**
     * Detector's name.
     * */
    @Column
    private String detector;

    /**
     * Score.
     * */
    @Column
    private int score;

    /**
     * User id.
     * */
    @Column
    private String userId;

    /**
     * Creation timestamp.
     * */
    @Column
    private long created = System.currentTimeMillis();


    /**
     * Default constructor.
     * */
    public ScoreDO() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDetector() {
        return detector;
    }

    public void setDetector(String detector) {
        this.detector = detector;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.detector, this.score, this.userId);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ScoreDO)) {
            return false;
        }

        ScoreDO score = (ScoreDO) obj;

        if (!this.detector.equals(score.getDetector())) {
            return false;
        }

        if (this.score != score.getScore()) {
            return false;
        }

        return true;
    }
}
