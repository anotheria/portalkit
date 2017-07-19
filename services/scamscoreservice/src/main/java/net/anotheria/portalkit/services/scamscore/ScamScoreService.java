package net.anotheria.portalkit.services.scamscore;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;

/**
 * Defines all necessary methods for counting spam score of user.
 *
 * @author Vlad Lukjanenko
 */
@DistributeMe()
@FailBy(strategyClass=RetryCallOnce.class)
public interface ScamScoreService extends Service {

    /**
     * Creates user scam record.
     *
     * @param scamRecord    scam record.
     * */
    void createUserScamRecord(UserScamRecordBO scamRecord) throws ScamScoreServiceException;

    /**
     * Updates user scam record.
     *
     * @param scamRecord    scam record.
     * */
    void updateUserScamRecord(UserScamRecordBO scamRecord) throws ScamScoreServiceException;

    /**
     * Removes user scam record.
     *
     * @param recordId    record id.
     * */
    void deleteUserScamRecord(String recordId) throws ScamScoreServiceException;

    /**
     * Creates score record.
     *
     * @param score    score record.
     * */
    void createScoreRecord(ScoreBO score) throws ScamScoreServiceException;

    /**
     * Removes score record.
     *
     * @param id    score record id.
     * */
    void deleteScoreRecord(long id) throws ScamScoreServiceException;

    /**
     * Removes score record.
     *
     * @param userId    score user id.
     * */
    void deleteScoreRecords(String userId) throws ScamScoreServiceException;

    /**
     * Returns all scam records.
     *
     * @return list of {@link UserScamRecordBO}.
     * */
    List<UserScamRecordBO> getScamRecords() throws ScamScoreServiceException;

    /**
     * Returns only not checked scam records.
     *
     * @return list of {@link UserScamRecordBO}.
     * */
    List<UserScamRecordBO> getScamRecordsNotChecked() throws ScamScoreServiceException;

    /**
     * Returns scam record by id.
     *
     * @return {@link UserScamRecordBO}.
     * */
    UserScamRecordBO getScamRecord(String id) throws ScamScoreServiceException;

    /**
     * Returns all scores records.
     *
     * @return list of {@link ScoreBO}.
     * */
    List<ScoreBO> getScoresRecords() throws ScamScoreServiceException;

    /**
     * Returns score record by id.
     *
     * @return {@link ScoreBO}.
     * */
    ScoreBO getScoreRecord(long id) throws ScamScoreServiceException;

    /**
     * Returns score record by user id.
     *
     * @return {@link ScoreBO}.
     * */
    List<ScoreBO> getScoreRecordsByUserId(String userId) throws ScamScoreServiceException;

    /**
     * Delete user data.
     * */
    void deleteUserData(String userId) throws ScamScoreServiceException;
}
