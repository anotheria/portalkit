package net.anotheria.portalkit.services.scamscore.persistence;

import net.anotheria.moskito.aop.annotation.Monitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Default {@link ScamScorePersistenceService} implementation.
 *
 * @author Vlad Lukjanenko
 */
@Service
@Transactional
@Monitor(subsystem = "scamscore", category = "portalkit-service")
public class ScamScorePersistenceServiceImpl implements ScamScorePersistenceService {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void createUserScamRecord(UserScamRecordDO scamRecord) throws ScamScorePersistenceServiceException {
        entityManager.persist(scamRecord);
    }

    @Override
    public void updateUserScamRecord(UserScamRecordDO scamRecord) throws ScamScorePersistenceServiceException {
        entityManager.merge(scamRecord);
    }

    @Override
    public void deleteUserScamRecord(String recordId) throws ScamScorePersistenceServiceException {
        Query query = entityManager.createNamedQuery(UserScamRecordDO.JPQL_DELETE_RECORD_BY_ID)
                .setParameter("id", recordId);

        query.executeUpdate();
    }

    @Override
    public void createScoreRecord(ScoreDO score) throws ScamScorePersistenceServiceException {
        entityManager.persist(score);
    }

    @Override
    public void deleteScoreRecord(long id) throws ScamScorePersistenceServiceException {
        Query query = entityManager.createNamedQuery(ScoreDO.JPQL_DELETE_RECORD_BY_ID)
                .setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public void deleteScoreRecords(String userId) throws ScamScorePersistenceServiceException {
        Query query = entityManager.createNamedQuery(ScoreDO.JPQL_DELETE_RECORD_BY_USER_UD)
                .setParameter("userId", userId);

        query.executeUpdate();
    }

    @Override
    public List<UserScamRecordDO> getScamRecords(boolean notCheckedOnly) throws ScamScorePersistenceServiceException {

        TypedQuery<UserScamRecordDO> query = entityManager.createNamedQuery(notCheckedOnly ? UserScamRecordDO.JPQL_GET_ALL_RECORDS_NOT_CHECKED : UserScamRecordDO.JPQL_GET_ALL_RECORDS, UserScamRecordDO.class);
        List<UserScamRecordDO> records = query.getResultList();

        if (records == null) {
            throw new ScamScorePersistenceServiceException("Error occurred while getting user scam records");
        }

        return records;
    }

    @Override
    public UserScamRecordDO getScamRecord(String id) throws ScamScorePersistenceServiceException {

        TypedQuery<UserScamRecordDO> query = entityManager.createNamedQuery(UserScamRecordDO.JPQL_GET_RECORD_BY_ID, UserScamRecordDO.class);
        query.setParameter("id", id);
        List<UserScamRecordDO> records = query.getResultList();

        if (records == null || records.isEmpty()) {
            throw new ScamScorePersistenceServiceException("Error occurred while getting user scam record with id=" + id);
        }

        return records.get(0);
    }

    @Override
    public List<ScoreDO> getScoresRecords() throws ScamScorePersistenceServiceException {

        TypedQuery<ScoreDO> query = entityManager.createNamedQuery(ScoreDO.JPQL_GET_ALL_RECORDS, ScoreDO.class);
        List<ScoreDO> records = query.getResultList();

        if (records == null) {
            throw new ScamScorePersistenceServiceException("Error occurred while getting score records");
        }

        return records;
    }

    @Override
    public ScoreDO getScoreRecord(long id) throws ScamScorePersistenceServiceException {

        TypedQuery<ScoreDO> query = entityManager.createNamedQuery(ScoreDO.JPQL_GET_RECORD_BY_ID, ScoreDO.class);
        query.setParameter("id", id);
        List<ScoreDO> records = query.getResultList();

        if (records == null || records.isEmpty()) {
            throw new ScamScorePersistenceServiceException("Error occurred while getting score record with id=" + id);
        }

        return records.get(0);
    }

    @Override
    public List<ScoreDO> getScoreRecordsByUserId(String userId) throws ScamScorePersistenceServiceException {

        TypedQuery<ScoreDO> query = entityManager.createNamedQuery(ScoreDO.JPQL_GET_RECORDS_BY_USER, ScoreDO.class);
        query.setParameter("userId", userId);
        List<ScoreDO> records = query.getResultList();

        if (records == null) {
            throw new ScamScorePersistenceServiceException("Error occurred while getting score record with user id=" + userId);
        }

        return records;
    }

    @Override
    public void deleteUserData(String userId) throws ScamScorePersistenceServiceException {
        deleteUserScamRecord(userId);
        deleteScoreRecords(userId);
    }
}
