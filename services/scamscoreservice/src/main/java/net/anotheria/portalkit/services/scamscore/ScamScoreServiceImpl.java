package net.anotheria.portalkit.services.scamscore;

import net.anotheria.portalkit.services.scamscore.persistence.ScamScorePersistenceService;
import net.anotheria.portalkit.services.scamscore.persistence.ScamScorePersistenceServiceException;
import net.anotheria.portalkit.services.scamscore.persistence.ScoreDO;
import net.anotheria.portalkit.services.scamscore.persistence.UserScamRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link ScamScoreService} implementation.
 *
 * @author Vlad Lukjanenko
 */
@Service
public class ScamScoreServiceImpl implements ScamScoreService {

    @Autowired
    private ScamScorePersistenceService scamScorePersistenceService;

    @Override
    public void createUserScamRecord(UserScamRecordBO scamRecord) throws ScamScoreServiceException {
        try {
            scamScorePersistenceService.createUserScamRecord(scamRecord.toDO());
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while creating scam record", e);
        }
    }

    @Override
    public void updateUserScamRecord(UserScamRecordBO scamRecord) throws ScamScoreServiceException {
        try {
            scamScorePersistenceService.updateUserScamRecord(scamRecord.toDO());
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while updating scam record", e);
        }
    }

    @Override
    public void deleteUserScamRecord(String recordId) throws ScamScoreServiceException {
        try {
            scamScorePersistenceService.deleteUserScamRecord(recordId);
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while removing scam record by id=" + recordId, e);
        }
    }

    @Override
    public void createScoreRecord(ScoreBO score) throws ScamScoreServiceException {
        try {
            scamScorePersistenceService.createScoreRecord(score.toDO());
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while creating score record", e);
        }
    }

    @Override
    public void deleteScoreRecord(long id) throws ScamScoreServiceException {
        try {
            scamScorePersistenceService.deleteScoreRecord(id);
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while removing score record by id=" + id, e);
        }
    }

    @Override
    public void deleteScoreRecords(String userId) throws ScamScoreServiceException {
        try {
            scamScorePersistenceService.deleteScoreRecords(userId);
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while removing score records by user id=" + userId, e);
        }
    }

    @Override
    public List<UserScamRecordBO> getScamRecords() throws ScamScoreServiceException {
        return getScamRecords(false);
    }

    @Override
    public List<UserScamRecordBO> getScamRecordsNotChecked() throws ScamScoreServiceException {
        return getScamRecords(true);
    }

    private List<UserScamRecordBO> getScamRecords(boolean notCheckedOnly) throws ScamScoreServiceException {

        List<UserScamRecordBO> result = new ArrayList<>();
        List<UserScamRecordDO> records = null;

        try {
            records = scamScorePersistenceService.getScamRecords(notCheckedOnly);
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while getting scam records", e);
        }

        if (records == null) {
            throw new ScamScoreServiceException("Error occurred while getting scam records");
        }

        for (UserScamRecordDO record : records) {
            result.add(new UserScamRecordBO(record));
        }

        return result;
    }

    @Override
    public UserScamRecordBO getScamRecord(String id) throws ScamScoreServiceException {
        try {
            return new UserScamRecordBO(scamScorePersistenceService.getScamRecord(id));
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while getting scam record by id" + id, e);
        }
    }

    @Override
    public List<ScoreBO> getScoresRecords() throws ScamScoreServiceException {

        List<ScoreBO> result = new ArrayList<>();
        List<ScoreDO> records = null;

        try {
            records = scamScorePersistenceService.getScoresRecords();
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while getting score records", e);
        }

        if (records == null) {
            throw new ScamScoreServiceException("Error occurred while getting score records");
        }

        for (ScoreDO record : records) {
            result.add(new ScoreBO(record));
        }

        return result;
    }

    @Override
    public ScoreBO getScoreRecord(long id) throws ScamScoreServiceException {
        try {
            return new ScoreBO(scamScorePersistenceService.getScoreRecord(id));
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while getting score record by id" + id, e);
        }
    }

    @Override
    public List<ScoreBO> getScoreRecordsByUserId(String userId) throws ScamScoreServiceException {

        List<ScoreBO> result = new ArrayList<>();
        List<ScoreDO> records = null;

        try {
            records = scamScorePersistenceService.getScoreRecordsByUserId(userId);
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while getting score record by user id" + userId, e);
        }

        if (records == null) {
            throw new ScamScoreServiceException("Error occurred while getting score records");
        }

        for (ScoreDO record : records) {
            result.add(new ScoreBO(record));
        }

        return result;
    }

    @Override
    public void deleteUserData(String userId) throws ScamScoreServiceException {
        try {
            scamScorePersistenceService.deleteUserData(userId);
        } catch (ScamScorePersistenceServiceException e) {
            throw new ScamScoreServiceException("Error occurred while removing user data", e);
        }
    }
}
