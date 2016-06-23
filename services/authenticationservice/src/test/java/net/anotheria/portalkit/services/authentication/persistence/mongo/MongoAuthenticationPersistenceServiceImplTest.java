package net.anotheria.portalkit.services.authentication.persistence.mongo;

import com.mongodb.*;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptors;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.authentication.persistence.jdbc.JDBCAuthenticationPersistenceServiceImpl;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.IdCodeGenerator;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Roman Stetsiuk on 6/21/16.
 */
public class MongoAuthenticationPersistenceServiceImplTest {
//    public static final String ENV = "embedded";
//
//    private static final String DATABASE_NAME = "embedded";
//
//    private static final String MONGO_HOST = "localhost";
//    private static final int MONGO_PORT = 27777;
//    private static final String IN_MEM_CONNECTION_URL = MONGO_HOST + ":" + MONGO_PORT;
//
//    private MongodExecutable mongodExecutable;
//    private MongodProcess mongod;
//    private Mongo mongo;
//    private DB db;
//    private DBCollection col;
//
//    private MongoAuthenticationPersistenceServiceImpl getService() throws Exception{
//        ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", ENV));
//        MongoAuthenticationPersistenceServiceImpl service = new MongoAuthenticationPersistenceServiceImpl();
////        service.cleanupFromUnitTests();
//        return service;
//    }



    /**
     * Start in-memory Mongo DB process
     */
    @Before
    public void setup() throws Exception {
//        MongodStarter starter = MongodStarter.getDefaultInstance();
//
//        IMongodConfig mongodConfig = new MongodConfigBuilder()
//                .version(Version.Main.PRODUCTION)
//                .net(new Net(MONGO_PORT, Network.localhostIsIPv6()))
//                .build();
//
//        mongodExecutable = starter.prepare(mongodConfig);
//        mongod = mongodExecutable.start();
//        mongo = new MongoClient(MONGO_HOST, MONGO_PORT);
//
//        db = mongo.getDB(DATABASE_NAME);
//        col = db.createCollection("testCol", new BasicDBObject());
//        col.save(new BasicDBObject("testDoc", new Date()));

    }

    /**
     * Shutdown in-memory Mongo DB process
     */
    @After
    public void teardown() throws Exception {
//        if (mongodExecutable != null) {
//            mongod.stop();
//            mongodExecutable.stop();
//        }
    }


    @Test public void testCreateEditDeletePassword() throws Exception{
        AuthenticationPersistenceService service = new MongoAuthenticationPersistenceServiceImpl();
        AccountId id = AccountId.generateNew();

        String newPassword = IdCodeGenerator.generateCode(200);
        String password = service.getEncryptedPassword(id);
        assertNull("NO Password should be set yet", password);

        service.saveEncryptedPassword(id, newPassword);
        assertEquals(newPassword, service.getEncryptedPassword(id));

        service.deleteEncryptedPassword(id);
        assertEquals(null, service.getEncryptedPassword(id));
    }

    @Test public void testCreateEditUpdateDeletePassword() throws Exception{
        AuthenticationPersistenceService service = new MongoAuthenticationPersistenceServiceImpl();
        AccountId id = AccountId.generateNew();

        String newPassword = IdCodeGenerator.generateCode(200);
        String password = service.getEncryptedPassword(id);
        assertNull("NO Password should be set yet", password);

        service.saveEncryptedPassword(id, newPassword);
        assertEquals(newPassword, service.getEncryptedPassword(id));

        String newnewPassword = IdCodeGenerator.generateCode(200);
        service.saveEncryptedPassword(id, newnewPassword);
        assertEquals(newnewPassword, service.getEncryptedPassword(id));

        service.deleteEncryptedPassword(id);
        assertEquals(null, service.getEncryptedPassword(id));
    }

    @Test public void testDelete() throws Exception{
        AuthenticationPersistenceService service = new MongoAuthenticationPersistenceServiceImpl();
        AccountId id1 = AccountId.generateNew();
        AccountId id2 = AccountId.generateNew();

        String newPassword1 = IdCodeGenerator.generateCode(200);
        String newPassword2 = IdCodeGenerator.generateCode(200);

        service.saveEncryptedPassword(id1, newPassword1);
        service.saveEncryptedPassword(id2, newPassword2);

        assertEquals(newPassword1, service.getEncryptedPassword(id1));
        assertEquals(newPassword2, service.getEncryptedPassword(id2));

        service.deleteEncryptedPassword(id1);
        assertEquals(null, service.getEncryptedPassword(id1));
        //ensure the second password is not deleted
        assertEquals(newPassword2, service.getEncryptedPassword(id2));

    }

    @Test
    public void testCreateGetDeleteToken() throws AuthenticationPersistenceServiceException {
        AuthenticationPersistenceService service = new MongoAuthenticationPersistenceServiceImpl();

        AccountId id = AccountId.generateNew();
        AuthToken token1 = createDummyAuthToken(id);
        AuthToken token2 = createDummyAuthToken(id);
        token2.setType(43);

        String t1 = AuthTokenEncryptors.encrypt(token1);
        String t2 = AuthTokenEncryptors.encrypt(token2);

        assertFalse(service.authTokenExists(t1));
        assertFalse(service.authTokenExists(t2));

        //store token 1
        service.saveAuthToken(id, t1);
        assertTrue(service.authTokenExists(t1));
        assertFalse(service.authTokenExists(t2));

        //this should
        service.saveAuthToken(id, t2);
        assertTrue(service.authTokenExists(t1));
        assertTrue(service.authTokenExists(t2));

        service.deleteAuthToken(id, t2);
        assertTrue(service.authTokenExists(t1));
        assertFalse(service.authTokenExists(t2));

        service.deleteAuthTokens(id);
        assertFalse(service.authTokenExists(t1));
        assertFalse(service.authTokenExists(t2));
    }

    private AuthToken createDummyAuthToken(AccountId accId){
        AuthToken token = new AuthToken();

        token.setMultiUse(true);
        token.setExpiryTimestamp(System.currentTimeMillis()+1000000L);
        token.setExclusive(true);
        token.setExclusiveInType(true);
        token.setAccountId(accId);
        token.setType(42);


        return token;
    }


}