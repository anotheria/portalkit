package net.anotheria.portalkit.services.scamscore.persistence;

import net.anotheria.moskito.aop.annotation.Monitor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Defines all necessary methods for counting spam score of user.
 *
 * @author Vlad Lukjanenko
 */
@Monitor
@Service
public interface ScamScorePersistenceService {

    /**
     * Creates user scam record.
     *
     * @param scamRecord    scam record.
     * */
    void createUserScamRecord(UserScamRecordDO scamRecord) throws ScamScorePersistenceServiceException;

    /**
     * Updates user scam record.
     *
     * @param scamRecord    scam record.
     * */
    void updateUserScamRecord(UserScamRecordDO scamRecord) throws ScamScorePersistenceServiceException;

    /**
     * Removes user scam record.
     *
     * @param recordId    record id.
     * */
    void deleteUserScamRecord(String recordId) throws ScamScorePersistenceServiceException;

    /**
     * Creates score record.
     *
     * @param score    score record.
     * */
    void createScoreRecord(ScoreDO score) throws ScamScorePersistenceServiceException;

    /**
     * Removes score record.
     *
     * @param id    score record id.
     * */
    void deleteScoreRecord(long id) throws ScamScorePersistenceServiceException;

    /**
     * Removes score record.
     *
     * @param userId    score user id.
     * */
    void deleteScoreRecords(String userId) throws ScamScorePersistenceServiceException;

    /**
     * Returns all scam records.
     *
     * @return list of {@link UserScamRecordDO}.
     * */
    List<UserScamRecordDO> getScamRecords(boolean notCheckedOnly) throws ScamScorePersistenceServiceException;

    /**
     * Returns scam record by id.
     *
     * @return {@link UserScamRecordDO}.
     * */
    UserScamRecordDO getScamRecord(String id) throws ScamScorePersistenceServiceException;

    /**
     * Returns all scores records.
     *
     * @return list of {@link ScoreDO}.
     * */
    List<ScoreDO> getScoresRecords() throws ScamScorePersistenceServiceException;

    /**
     * Returns score record by id.
     *
     * @return {@link ScoreDO}.
     * */
    ScoreDO getScoreRecord(long id) throws ScamScorePersistenceServiceException;

    /**
     * Returns score record by user id.
     *
     * @return {@link ScoreDO}.
     * */
    List<ScoreDO> getScoreRecordsByUserId(String userId) throws ScamScorePersistenceServiceException;

    /**
     * Delete user data.
     * */
    void deleteUserData(String userId) throws ScamScorePersistenceServiceException;
}
