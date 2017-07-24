package net.anotheria.portalkit.services.scamscore;

import net.anotheria.portalkit.services.scamscore.persistence.ScoreDO;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Vlad Lukjanenko
 */
public class ScoreBO implements Serializable {

    /**
     * Generated serial version UID.
     * */
    private static final long serialVersionUID = -8958293312297340881L;

    /**
     * Id.
     * */
    private long id;

    /**
     * Detector's name.
     * */
    private String detector;

    /**
     * Score.
     * */
    private int score;

    /**
     * User id.
     * */
    private String userId;

    /**
     * Creation timestamp.
     * */
    private long created = System.currentTimeMillis();


    /**
     * Default constructor.
     * */
    public ScoreBO() {

    }

    /**
     * Constructor.
     *
     * @param entry     persistence entry.
     * */
    public ScoreBO(ScoreDO entry) {

        this.id = entry.getId();
        this.detector = entry.getDetector();
        this.score = entry.getScore();
        this.userId = entry.getUserId();
        this.created = entry.getCreated();
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

    public ScoreDO toDO() {

        ScoreDO scoreEntry = new ScoreDO();

        scoreEntry.setId(this.id);
        scoreEntry.setDetector(this.detector);
        scoreEntry.setScore(this.score);
        scoreEntry.setUserId(this.userId);
        scoreEntry.setCreated(this.created);

        return scoreEntry;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.detector, this.score, this.userId);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ScoreBO)) {
            return false;
        }

        ScoreBO score = (ScoreBO) obj;

        if (!this.detector.equals(score.getDetector())) {
            return false;
        }

        if (this.score != score.getScore()) {
            return false;
        }

        return true;
    }
}
