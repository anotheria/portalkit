import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.profileservice.ProfileService;
import net.anotheria.portalkit.services.profileservice.ProfileServiceFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author asamoilich.
 */
@Ignore
public class ProfileServiceTest {
    @BeforeClass
    public static void before() {
        Map<String, Serializable> factoryParameters = new HashMap<String, Serializable>();
        factoryParameters.put(ProfileServiceFactory.PARAMETER_ENTITY_CLASS, ProfileBO.class);
        factoryParameters.put(ProfileServiceFactory.PARAMETER_CONFIGURATION, "pk-profile-mongo-service-config");
        MetaFactory.addParameterizedFactoryClass(ProfileService.class, "profile", ProfileServiceFactory.class, factoryParameters);

    }

    @Test
    public void dd() {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB my_test_db = mongoClient.getDB("test_profile_db");
            DBCollection test_profiles = my_test_db.getCollection("test_profiles_collection");
            test_profiles.drop();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        try {
//            ProfileBO profile18 = createProfile(18);
//            ProfileBO profile19 = createProfile(19);
//            ProfileBO profile20 = createProfile(20);
//            @SuppressWarnings("unchecked")
//            ProfileService<ProfileBO> service = MetaFactory.get(ProfileService.class, "profile");
//            service.create(profile18);
//            service.create(profile19);
//            service.create(profile20);
//            Assert.assertEquals(3, service.findAll().size());
//            service.delete(profile19.get_id());
//            Assert.assertEquals(2, service.findAll().size());
//
//            try {
//                service.read(profile19.get_id());
//                Assert.fail();
//            } catch (ProfileServiceException e) {
//                Assert.assertTrue(e instanceof ProfileNotFoundException);
//            }
//        } catch (MetaFactoryException e) {
//            e.printStackTrace();
//        } catch (ProfileServiceException e) {
//            e.printStackTrace();
//        }
    }

    private static ProfileBO createProfile(int i) {
        ProfileBO profile = new ProfileBO("" + i);
        profile.setAge(i);
        profile.setHeight(i);
        profile.setWeight(i);
        profile.setName("profile" + i);
        return profile;
    }
}
