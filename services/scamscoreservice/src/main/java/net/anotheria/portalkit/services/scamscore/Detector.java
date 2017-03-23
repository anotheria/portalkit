package net.anotheria.portalkit.services.scamscore;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parent class for all detectors.
 *
 * @author Vlad Lukjanenko
 */
public abstract class Detector {

    /**
     * LOGGER.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(Detector.class);

    /**
     * {@link ScamScoreService} instance.
     * */
    private ScamScoreService scamScoreService;

    /**
     * Contains score value according to threshold key.
     * */
    private Map<Integer, Integer> thresholdScoreMap;

    /**
     * Default score value if no threshold available.
     * */
    protected int defaultScore;

    /**
     * Detector name.
     * */
    protected String name;

    /**
     * Default constructor.
     * */
    public Detector() {
        thresholdScoreMap = new HashMap<>();

        try {
            scamScoreService = MetaFactory.get(ScamScoreService.class);
        } catch (MetaFactoryException e) {
            throw new RuntimeException("Unable to get ScamScoreService instance", e);
        }
    }


    /**
     * Adds score value for appropriate threshold.
     *
     * @param threshold threshold key.
     * @param score     score value.
     * */
    public void addScoreValue(int threshold, int score) {
        thresholdScoreMap.put(threshold, score);
    }

    /**
     * Appends score to current user score.
     * */
    public void storeScore(String userId, int score) {

        UserScamRecordBO userScamRecord = null;
        List<ScoreBO> scores = null;

        try {
            userScamRecord = scamScoreService.getScamRecord(userId);
        } catch (ScamScoreServiceException e) {
            userScamRecord = new UserScamRecordBO();
            userScamRecord.setId(userId);
            userScamRecord.setUpdated(System.currentTimeMillis());
            userScamRecord.setUpdated(System.currentTimeMillis());
            userScamRecord.setTotal_score(score);
            userScamRecord.setChecked(false);

            try {
                scamScoreService.createUserScamRecord(userScamRecord);
            } catch (ScamScoreServiceException e1) {
                LOGGER.error("Unable to create UserScamRecordBO for user with id=" + userId, e1);
                return;
            }

            addScore(userId, score);
            return;
        }

        int totalScore = userScamRecord.getTotal_score();
        userScamRecord.setTotal_score(totalScore + score);
        userScamRecord.setChecked(false);
        addScore(userId, score);

        try {
            scamScoreService.updateUserScamRecord(userScamRecord);
        } catch (ScamScoreServiceException e) {
            LOGGER.error("Unable to update UserScamRecordBO for user with id=" + userId, e);
            return;
        }
    }

    private void addScore(String userId, int score) {

        ScoreBO scoreBO = new ScoreBO();
        scoreBO.setUserId(userId);
        scoreBO.setScore(score);
        scoreBO.setDetector(this.getClass().getSimpleName());

        try {
            scamScoreService.createScoreRecord(scoreBO);
        } catch (ScamScoreServiceException e) {
            LOGGER.error("Unable to create ScoreBO for user with id=" + userId, e);
        }
    }


    public Map<Integer, Integer> getThresholdScoreMap() {
        return thresholdScoreMap;
    }

    public void setThresholdScoreMap(Map<Integer, Integer> thresholdScoreMap) {
        this.thresholdScoreMap = thresholdScoreMap;
    }

    public int getDefaultScore() {
        return defaultScore;
    }

    public void setDefaultScore(int defaultScore) {
        this.defaultScore = defaultScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScamScoreService getScamScoreService() {
        return scamScoreService;
    }
}
