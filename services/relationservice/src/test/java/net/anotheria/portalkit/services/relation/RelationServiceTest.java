package net.anotheria.portalkit.services.relation;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.exception.RelationAlreadyExistsException;
import net.anotheria.portalkit.services.relation.exception.RelationNotFoundException;
import net.anotheria.portalkit.services.relation.exception.RelationServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author bvanchuhov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RelationSpringConfiguration.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RelationServiceTest {

    private static final AccountId ACCOUNT_A = new AccountId("A");
    private static final AccountId ACCOUNT_B = new AccountId("B");
    private static final AccountId ACCOUNT_C = new AccountId("C");
    private static final AccountId ACCOUNT_D = new AccountId("D");
    private static final AccountId ACCOUNT_E = new AccountId("E");

    private static final AccountId ACCOUNT_X = new AccountId("X");
    private static final AccountId ACCOUNT_Y = new AccountId("Y");

    private static final AccountId NONEXISTENT_ACCOUNT = new AccountId("Nonexistent");

    @Autowired
    private RelationService relationService;

    @Before
    public void before() throws RelationServiceException {
        fillDatabase();
    }

    public void fillDatabase() throws RelationServiceException {
        relationService.addRelation(new Relation(ACCOUNT_A, ACCOUNT_B, "T0").setCreated(10L));
        relationService.addRelation(new Relation(ACCOUNT_A, ACCOUNT_C, "T1").setCreated(1L));
        relationService.addRelation(new Relation(ACCOUNT_C, ACCOUNT_D, "T0").setCreated(20L));
        relationService.addRelation(new Relation(ACCOUNT_A, ACCOUNT_D, "T2").setCreated(100L));
        relationService.addRelation(new Relation(ACCOUNT_A, ACCOUNT_E, "T2").setCreated(5L));

        relationService.addRelation(new Relation(ACCOUNT_X, ACCOUNT_Y, "T0").setCreated(30L).setUpdated(50L));
    }

    @Test
    public void smokeTest() throws Exception {
        System.out.println("OK");
    }


    @Test
    public void testAddRelation() throws RelationServiceException {
        relationService.addRelation(ACCOUNT_C, ACCOUNT_A, "T1");

        assertThat(relationService.isRelated(ACCOUNT_C, ACCOUNT_A, "T1"), is(true));
    }

    @Test(expected = RelationAlreadyExistsException.class)
    public void testAddRelation_relationAlreadyExists() throws RelationServiceException {
        relationService.addRelation(ACCOUNT_A, ACCOUNT_B, "T0");
    }


    @Test
    public void testGetUserRelation_existingRelation() throws RelationServiceException {
        Relation relation = relationService.getRelation(ACCOUNT_X, ACCOUNT_Y, "T0");

        assertThat(relation.getOwner(), is(ACCOUNT_X));
        assertThat(relation.getPartner(), is(ACCOUNT_Y));
        assertThat(relation.getRelationName(), is("T0"));
        assertThat(relation.getCreated(), is(30L));
        assertThat(relation.getUpdated(), is(50L));
    }

    @Test(expected = RelationNotFoundException.class)
    public void testGetRelation_nonExistingRelation() throws RelationServiceException {
        relationService.getRelation(NONEXISTENT_ACCOUNT, NONEXISTENT_ACCOUNT, "T0");
    }


    @Test
    public void testIsRelated_existingRelations() throws RelationServiceException {
        assertThat(relationService.isRelated(ACCOUNT_A, ACCOUNT_B, "T0"), is(true));
    }

    @Test
    public void testIsRelated_nonExistingRelations() throws RelationServiceException {
        assertThat(relationService.isRelated(NONEXISTENT_ACCOUNT, NONEXISTENT_ACCOUNT, "T0"), is(false));
    }


    @Test
    public void testGetOwnerRelations() throws RelationServiceException {
        Set<Relation> ownerRelations = relationService.getOwnerRelations(ACCOUNT_A);

        assertRelationsEqual(ownerRelations, new HashSet<>(Arrays.asList(
                new Relation(ACCOUNT_A, ACCOUNT_B, "T0"),
                new Relation(ACCOUNT_A, ACCOUNT_C, "T1"),
                new Relation(ACCOUNT_A, ACCOUNT_D, "T2"),
                new Relation(ACCOUNT_A, ACCOUNT_E, "T2")
        )));
    }

    @Test
    public void testGetOwnerRelations_noSuchOwner() throws RelationServiceException {
        Set<Relation> ownerRelations = relationService.getOwnerRelations(NONEXISTENT_ACCOUNT);

        assertThat(ownerRelations, is(empty()));
    }


    @Test
    public void testGetOwnerRelationsByType() throws RelationServiceException {
        Set<Relation> ownerRelations = relationService.getOwnerRelations(ACCOUNT_A, "T1");

        assertRelationsEqual(ownerRelations, new HashSet<>(Arrays.asList(
                new Relation(ACCOUNT_A, ACCOUNT_C, "T1")
        )));
    }

    @Test
    public void testGetRelationsByType_noSuchOwner() throws RelationServiceException {
        Set<Relation> ownerRelations = relationService.getOwnerRelations(NONEXISTENT_ACCOUNT, "T1");

        assertThat(ownerRelations, is(empty()));
    }

    @Test
    public void testGetRelationsByType_noSuchTypeForOwner() throws RelationServiceException {
        Set<Relation> ownerRelations = relationService.getOwnerRelations(ACCOUNT_C, "T2");

        assertThat(ownerRelations, is(empty()));
    }


    @Test
    public void testGetPartnerRelations() throws RelationServiceException {
        Set<Relation> partnerRelations = relationService.getPartnerRelations(ACCOUNT_D);

        assertRelationsEqual(partnerRelations, new HashSet<>(Arrays.asList(
                new Relation(ACCOUNT_C, ACCOUNT_D, "T0"),
                new Relation(ACCOUNT_A, ACCOUNT_D, "T2")
        )));
    }

    @Test
    public void testGetPartnerRelations_noSuchPartner() throws RelationServiceException {
        Set<Relation> partnerRelations = relationService.getPartnerRelations(NONEXISTENT_ACCOUNT);

        assertThat(partnerRelations, is(empty()));
    }


    @Test
    public void testGetPartnerRelationsByType() throws RelationServiceException {
        Set<Relation> partnerRelations = relationService.getPartnerRelations(ACCOUNT_D, "T0");

        assertRelationsEqual(partnerRelations, new HashSet<>(Arrays.asList(
                new Relation(ACCOUNT_C, ACCOUNT_D, "T0")
        )));
    }


    @Test(expected = RelationNotFoundException.class)
    public void testDeleteRelation() throws RelationServiceException {
        relationService.deleteRelation(ACCOUNT_A, ACCOUNT_B, "T0");

        relationService.getRelation(ACCOUNT_A, ACCOUNT_B, "T0");
    }

    @Test(expected = RelationNotFoundException.class)
    public void testDeleteRelation_nonExistingRelation() throws RelationServiceException {
        relationService.deleteRelation(NONEXISTENT_ACCOUNT, NONEXISTENT_ACCOUNT, "T0");
    }


    @Test
    public void testDeleteRelations() throws RelationServiceException {
        relationService.deleteRelations(ACCOUNT_A, ACCOUNT_B);

        assertThat(relationService.isRelated(ACCOUNT_A, ACCOUNT_B, "T0"), is(false));
    }


    @Test
    public void testDeleteOwnerRelations() throws RelationServiceException {
        relationService.deleteOwnerRelations(ACCOUNT_A);

        assertThat(relationService.getOwnerRelations(ACCOUNT_A), is(empty()));
    }


    @Test
    public void testDeletePartnerRelations() throws RelationServiceException {
        relationService.deletePartnerRelations(ACCOUNT_D);

        assertThat(relationService.getPartnerRelations(ACCOUNT_D), is(empty()));
    }


    private void assertRelationsEqual(Relation a, Relation b) {
        assertThat(a.getOwner(), is(b.getOwner()));
        assertThat(a.getPartner(), is(b.getPartner()));
        assertThat(a.getRelationName(), is(b.getRelationName()));
    }

    private void assertRelationsEqual(Collection<Relation> actualRelations, Collection<Relation> expectedRelations) {
        assertThat(actualRelations.size(), is(expectedRelations.size()));

        for (Relation expectedRelation : expectedRelations) {
            if (!isContain(actualRelations, expectedRelation)) {
                fail(actualRelations + " does not contain " + expectedRelation);
            }
        }
    }

    private boolean isContain(Collection<Relation> relations, Relation expectedRelation) {
        for (Relation relation : relations) {
            if (isRelationsEqual(relation, expectedRelation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRelationsEqual(Relation actual, Relation expected) {
        return Objects.equals(actual.getOwner(), expected.getOwner())
                && Objects.equals(actual.getPartner(), expected.getPartner())
                && Objects.equals(actual.getRelationName(), expected.getRelationName());
    }
}
