package net.anotheria.portalkit.services.scamscore;

import net.anotheria.portalkit.services.scamscore.persistence.ScamScorePersistenceService;
import net.anotheria.portalkit.services.scamscore.persistence.UserScamRecordDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;

/**
 * @author Vlad Lukjanenko
 */
@RunWith(MockitoJUnitRunner.class)
public class ScamScoreServiceTest {

    @InjectMocks
    private ScamScoreServiceImpl scamScoreService;

    @Mock
    private ScamScorePersistenceService scamScorePersistenceService;


    @Test
    public void testCreateScamScoreRecord() throws Exception {

        UserScamRecordBO userScamRecord = getUserScamRecord();

        scamScoreService.createUserScamRecord(userScamRecord);

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).createUserScamRecord(userScamRecord.toDO());
    }

    @Test
    public void testUpdateScamScoreRecord() throws Exception {

        UserScamRecordBO userScamRecord = getUserScamRecord();

        scamScoreService.createUserScamRecord(userScamRecord);

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).createUserScamRecord(userScamRecord.toDO());

        userScamRecord.setTotal_score(20);

        scamScoreService.updateUserScamRecord(userScamRecord);

        ArgumentCaptor<UserScamRecordDO> captor = ArgumentCaptor.forClass(UserScamRecordDO.class);
        Mockito.verify(scamScorePersistenceService).updateUserScamRecord(captor.capture());
        Mockito.verify(scamScorePersistenceService, atLeastOnce()).updateUserScamRecord(userScamRecord.toDO());

        assertEquals(captor.getValue().getId(), "1");
        assertEquals(captor.getValue().getTotal_score(), 20);
    }

    @Test
    public void testDeleteScamScoreRecord() throws Exception {

        UserScamRecordBO userScamRecord = getUserScamRecord();

        scamScoreService.createUserScamRecord(userScamRecord);

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).createUserScamRecord(userScamRecord.toDO());

        scamScoreService.deleteUserScamRecord("1");

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).deleteUserScamRecord("1");
    }

    @Test
    public void testCreateScoreRecord() throws Exception {

        ScoreBO score = getScore();

        scamScoreService.createScoreRecord(score);

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).createScoreRecord(score.toDO());
    }

    @Test
    public void testDeleteScoreRecord() throws Exception {

        ScoreBO score = getScore();

        scamScoreService.createScoreRecord(score);

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).createScoreRecord(score.toDO());

        scamScoreService.deleteScoreRecord(1);

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).deleteScoreRecord(1);
    }

    @Test
    public void testDeleteScoreRecords() throws Exception {

        ScoreBO score = getScore();

        scamScoreService.createScoreRecord(score);

        score.setId(2);

        scamScoreService.createScoreRecord(score);
        scamScoreService.deleteScoreRecords("1");

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).deleteScoreRecords("1");
    }

    @Test
    public void testDeleteUserData() throws Exception {

        UserScamRecordBO userScamRecord = getUserScamRecord();
        ScoreBO score = getScore();

        scamScoreService.createUserScamRecord(userScamRecord);
        scamScoreService.createScoreRecord(score);
        scamScoreService.deleteUserData("1");

        Mockito.verify(scamScorePersistenceService, atLeastOnce()).deleteUserData("1");
    }


    private UserScamRecordBO getUserScamRecord() {
        UserScamRecordBO userScamRecord = new UserScamRecordBO();

        userScamRecord.setId("1");
        userScamRecord.setTotal_score(10);
        userScamRecord.setCreated(System.currentTimeMillis());
        userScamRecord.setUpdated(System.currentTimeMillis());
        return userScamRecord;
    }

    private ScoreBO getScore() {
        ScoreBO score = new ScoreBO();

        score.setId(1);
        score.setDetector("TestDetector");
        score.setScore(5);
        score.setUserId("1");
        score.setCreated(System.currentTimeMillis());
        return score;
    }
}
