import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.JDBCPickerConflictResolver;
import net.anotheria.portalkit.services.relation.RelationService;
import net.anotheria.portalkit.services.relation.RelationServiceException;
import net.anotheria.portalkit.services.relation.RelationServiceImplFactory;
import net.anotheria.portalkit.services.relation.persistence.RelationPersistenceService;
import net.anotheria.portalkit.services.relation.persistence.RelationPersistenceServiceException;
import net.anotheria.portalkit.services.relation.persistence.jdbc.JDBCRelationPersistenceServiceImplFactory;
import net.anotheria.portalkit.services.relation.storage.Relation;
import net.anotheria.portalkit.services.relation.storage.UserRelationData;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author asamoilich
 */
public class RelationServiceTest {

    @BeforeClass
    public static void before() {
        ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
        System.setProperty("JUNITTEST", "true");
        MetaFactory.reset();
        MetaFactory.addOnTheFlyConflictResolver(new JDBCPickerConflictResolver());
        MetaFactory.addAlias(RelationService.class, Extension.LOCAL);
        MetaFactory.addFactoryClass(RelationService.class, Extension.LOCAL, RelationServiceImplFactory.class);
        MetaFactory.addAlias(RelationPersistenceService.class, Extension.LOCAL);
        MetaFactory.addFactoryClass(RelationPersistenceService.class, Extension.LOCAL, JDBCRelationPersistenceServiceImplFactory.class);
    }

    @Test
    public void test() {
        try {
            //get services
            RelationService service = MetaFactory.get(RelationService.class);
            RelationPersistenceService persistenceService = MetaFactory.get(RelationPersistenceService.class);

            //prepare test data
            AccountId ownerId = AccountId.generateNew();
            AccountId partnerId = AccountId.generateNew();
            Relation relation = new Relation("Match");

            //add to database without caching
            Relation persistRelation = persistenceService.addRelation(ownerId, partnerId, relation);
            assertNotNull("Cannot be NULL", persistRelation);

            //then try to get using service
            UserRelationData relationFromDatabase = service.getRelationData(ownerId, partnerId);
            assertNotNull("Cannot be NULL", relationFromDatabase);
            assertEquals("Should be equals", ownerId, relationFromDatabase.getOwnerId());
            assertEquals("Should be equals", partnerId, relationFromDatabase.getPartnerId());
            assertNotNull("Cannot be NULL", relationFromDatabase.getRelationMap());
            assertFalse("Cannot be empty", relationFromDatabase.getRelationMap().isEmpty());
            assertEquals("Should be equals", 1, relationFromDatabase.getRelationMap().size());
            assertNotNull("Cannot be NULL", relationFromDatabase.getRelationMap().get(relation.getName()));

            //and now get the same relation from cache
            UserRelationData relationFromCache = service.getRelationData(ownerId, partnerId);
            assertNotNull("Cannot be NULL", relationFromCache);
            assertEquals("Should be equals", ownerId, relationFromCache.getOwnerId());
            assertEquals("Should be equals", partnerId, relationFromCache.getPartnerId());
            assertNotNull("Cannot be NULL", relationFromCache.getRelationMap());
            assertFalse("Cannot be empty", relationFromCache.getRelationMap().isEmpty());
            assertEquals("Should be equals", 1, relationFromCache.getRelationMap().size());
            assertNotNull("Cannot be NULL", relationFromCache.getRelationMap().get(relation.getName()));

            //try to add the same relation into database
            UserRelationData userRelationData = service.addRelation(ownerId, partnerId, relation);
            assertNotNull("Cannot be NULL", userRelationData);
            assertEquals("Should be equals", ownerId, userRelationData.getOwnerId());
            assertEquals("Should be equals", partnerId, userRelationData.getPartnerId());
            assertNotNull("Cannot be NULL", userRelationData.getRelationMap());
            assertFalse("Cannot be empty", userRelationData.getRelationMap().isEmpty());
            assertEquals("Should be equals", 1, userRelationData.getRelationMap().size());
            Relation updatedRelation = userRelationData.getRelationMap().get(relation.getName());
            assertNotNull("Cannot be NULL", updatedRelation);
            assertEquals("Should be equals", persistRelation.getCreatedTime(), updatedRelation.getCreatedTime());
            assertTrue("Should be TRUE", updatedRelation.getUpdatedTime() > updatedRelation.getCreatedTime());
            assertEquals("Should be equals", 1, service.getRelationCount(ownerId, "Match"));
        } catch (MetaFactoryException e) {
            fail(e.getMessage());
        } catch (RelationServiceException e) {
            fail(e.getMessage());
        } catch (RelationPersistenceServiceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRemove() {

        try {
            //get services
            RelationService service = MetaFactory.get(RelationService.class);
            //prepare test data
            AccountId ownerId = AccountId.generateNew();
            AccountId partnerId = AccountId.generateNew();
            service.addRelation(ownerId, partnerId, new Relation("Match"));
            service.addRelation(ownerId, partnerId, new Relation("Match_1"));

            UserRelationData userRelationData = service.getRelationData(ownerId, partnerId);
            assertNotNull("Cannot be NULL", userRelationData);
            assertEquals("Should be equals", ownerId, userRelationData.getOwnerId());
            assertEquals("Should be equals", partnerId, userRelationData.getPartnerId());
            assertNotNull("Cannot be NULL", userRelationData.getRelationMap());
            assertFalse("Cannot be empty", userRelationData.getRelationMap().isEmpty());
            assertEquals("Should be equals", 2, userRelationData.getRelationMap().size());
            service.removeRelation(ownerId, partnerId, "Match");

            userRelationData = service.getRelationData(ownerId, partnerId);
            assertNotNull("Cannot be NULL", userRelationData);
            assertEquals("Should be equals", ownerId, userRelationData.getOwnerId());
            assertEquals("Should be equals", partnerId, userRelationData.getPartnerId());
            assertNotNull("Cannot be NULL", userRelationData.getRelationMap());
            assertFalse("Cannot be empty", userRelationData.getRelationMap().isEmpty());
            assertEquals("Should be equals", 1, userRelationData.getRelationMap().size());
            assertEquals("Should be equals", 0, service.getRelationCount(ownerId, "Match"));
            assertEquals("Should be equals", 1, service.getRelationCount(ownerId, "Match_1"));
        } catch (MetaFactoryException e) {
            fail(e.getMessage());
        } catch (RelationServiceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOutAndInRelations() {
        try {
            //get services
            RelationService service = MetaFactory.get(RelationService.class);

            //prepare test data
            AccountId ownerId = AccountId.generateNew();
            AccountId partnerId = AccountId.generateNew();

            //add out relations
            service.addRelation(ownerId, partnerId, new Relation("Match"));
            service.addRelation(ownerId, AccountId.generateNew(), new Relation("Match"));
            service.addRelation(ownerId, AccountId.generateNew(), new Relation("Match"));
            service.addRelation(ownerId, AccountId.generateNew(), new Relation("Match"));
            service.addRelation(ownerId, AccountId.generateNew(), new Relation("Match"));

            //add in relations
            service.addRelation(partnerId, ownerId, new Relation("Match"));
            service.addRelation(AccountId.generateNew(), ownerId, new Relation("Match"));
            service.addRelation(AccountId.generateNew(), ownerId, new Relation("Match"));
            service.addRelation(AccountId.generateNew(), ownerId, new Relation("Match"));

            //add random relations
            service.addRelation(AccountId.generateNew(), AccountId.generateNew(), new Relation("Match"));
            service.addRelation(AccountId.generateNew(), AccountId.generateNew(), new Relation("Match"));
            service.addRelation(AccountId.generateNew(), AccountId.generateNew(), new Relation("Match"));
            service.addRelation(AccountId.generateNew(), AccountId.generateNew(), new Relation("Match"));

            assertEquals("Should be equals", 5, service.getRelationCount(ownerId, "Match"));
            assertEquals("Should be equals", 1, service.getRelationCount(partnerId, "Match"));
            List<UserRelationData> outRelationsData = service.getOutRelationsData(ownerId);
            assertNotNull("Cannot be NULL", outRelationsData);
            assertFalse("Should be FALSE", outRelationsData.isEmpty());
            assertEquals("Should be TRUE", 5, outRelationsData.size());

            List<UserRelationData> inRelationsData = service.getInRelationsData(ownerId);
            assertNotNull("Cannot be NULL", inRelationsData);
            assertFalse("Should be FALSE", inRelationsData.isEmpty());
            assertEquals("Should be TRUE", 4, inRelationsData.size());

            //try remove relation and check again out and in relations
            service.removeRelation(ownerId, partnerId, "Match");
            outRelationsData = service.getOutRelationsData(ownerId);
            assertNotNull("Cannot be NULL", outRelationsData);
            assertFalse("Should be FALSE", outRelationsData.isEmpty());
            assertEquals("Should be TRUE", 4, outRelationsData.size());

            outRelationsData = service.getOutRelationsData(partnerId);
            assertNotNull("Cannot be NULL", outRelationsData);
            assertFalse("Should be FALSE", outRelationsData.isEmpty());
            assertEquals("Should be TRUE", 1, outRelationsData.size());

            service.removeRelation(partnerId, ownerId, "Match");
            inRelationsData = service.getInRelationsData(ownerId);
            assertNotNull("Cannot be NULL", inRelationsData);
            assertFalse("Should be FALSE", inRelationsData.isEmpty());
            assertEquals("Should be TRUE", 3, inRelationsData.size());

            outRelationsData = service.getOutRelationsData(partnerId);
            assertNotNull("Cannot be NULL", outRelationsData);
            assertTrue("Should be TRUE", outRelationsData.isEmpty());
        } catch (MetaFactoryException e) {
            fail(e.getMessage());
        } catch (RelationServiceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void errorTestCase() {
        try {
            //get services
            RelationService service = MetaFactory.get(RelationService.class);
            try {
                service.addRelation(null, AccountId.generateNew(), new Relation("Match"));
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.addRelation(AccountId.generateNew(), null, new Relation("Match"));
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.addRelation(AccountId.generateNew(), AccountId.generateNew(), null);
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.removeRelation(null, AccountId.generateNew(), "Match");
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.removeRelation(AccountId.generateNew(), null, "Match");
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.removeRelation(AccountId.generateNew(), AccountId.generateNew(), null);
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.removeRelation(AccountId.generateNew(), AccountId.generateNew(), "");
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.getOutRelationsData(null);
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.getInRelationsData(null);
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.getRelationData(null, AccountId.generateNew());
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }
            try {
                service.getRelationData(AccountId.generateNew(), null);
                fail("Should fail!");
            } catch (Exception e) {
                assertTrue("Should be TRUE", e instanceof IllegalArgumentException);
            }

        } catch (MetaFactoryException e) {
            fail(e.getMessage());
        }
    }

}
