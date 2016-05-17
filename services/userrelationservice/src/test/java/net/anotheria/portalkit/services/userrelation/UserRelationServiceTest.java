package net.anotheria.portalkit.services.userrelation;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationAlreadyExistsException;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationNotFoundException;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationServiceException;
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
@ContextConfiguration(classes = UserRelationSprintConfiguration.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRelationServiceTest {

    private static final AccountId ACCOUNT_A = new AccountId("A");
    private static final AccountId ACCOUNT_B = new AccountId("B");
    private static final AccountId ACCOUNT_C = new AccountId("C");
    private static final AccountId ACCOUNT_D = new AccountId("D");
    private static final AccountId ACCOUNT_E = new AccountId("E");

    private static final AccountId ACCOUNT_X = new AccountId("X");
    private static final AccountId ACCOUNT_Y = new AccountId("Y");

    private static final AccountId NONEXISTENT_ACCOUNT = new AccountId("Nonexistent");

    @Autowired
    private UserRelationService relationService;

    @Before
    public void before() throws UserRelationServiceException {
        fillDatabase();
    }

    public void fillDatabase() throws UserRelationServiceException {
        relationService.addRelation(new UserRelation(ACCOUNT_A, ACCOUNT_B, "T0").setCreated(10L));
        relationService.addRelation(new UserRelation(ACCOUNT_A, ACCOUNT_C, "T1").setCreated(1L));
        relationService.addRelation(new UserRelation(ACCOUNT_C, ACCOUNT_D, "T0").setCreated(20L));
        relationService.addRelation(new UserRelation(ACCOUNT_A, ACCOUNT_D, "T2").setCreated(100L));
        relationService.addRelation(new UserRelation(ACCOUNT_A, ACCOUNT_E, "T2").setCreated(5L));

        relationService.addRelation(new UserRelation(ACCOUNT_X, ACCOUNT_Y, "T0").setCreated(30L).setUpdated(50L));
    }

    @Test
    public void smokeTest() throws Exception {
        System.out.println("OK");
    }


    @Test
    public void testAddRelation() throws UserRelationServiceException {
        relationService.addRelation(ACCOUNT_C, ACCOUNT_A, "T1");

        assertThat(relationService.isRelated(ACCOUNT_C, ACCOUNT_A, "T1"), is(true));
    }

    @Test(expected = UserRelationAlreadyExistsException.class)
    public void testAddRelation_relationAlreadyExists() throws UserRelationServiceException {
        relationService.addRelation(ACCOUNT_A, ACCOUNT_B, "T0");
    }


    @Test
    public void testGetUserRelation_existingRelation() throws UserRelationServiceException {
        UserRelation relation = relationService.getRelation(ACCOUNT_X, ACCOUNT_Y, "T0");

        assertThat(relation.getOwner(), is(ACCOUNT_X));
        assertThat(relation.getTarget(), is(ACCOUNT_Y));
        assertThat(relation.getRelationName(), is("T0"));
        assertThat(relation.getCreated(), is(30L));
        assertThat(relation.getUpdated(), is(50L));
    }

    @Test(expected = UserRelationNotFoundException.class)
    public void testGetRelation_nonExistingRelation() throws UserRelationServiceException {
        relationService.getRelation(NONEXISTENT_ACCOUNT, NONEXISTENT_ACCOUNT, "T0");
    }


    @Test
    public void testIsRelated_existingRelations() throws UserRelationServiceException {
        assertThat(relationService.isRelated(ACCOUNT_A, ACCOUNT_B, "T0"), is(true));
    }

    @Test
    public void testIsRelated_nonExistingRelations() throws UserRelationServiceException {
        assertThat(relationService.isRelated(NONEXISTENT_ACCOUNT, NONEXISTENT_ACCOUNT, "T0"), is(false));
    }


    @Test
    public void testGetOwnerRelations() throws UserRelationServiceException {
        Set<UserRelation> ownerRelations = relationService.getOwnerRelations(ACCOUNT_A);

        assertRelationsEqual(ownerRelations, new HashSet<>(Arrays.asList(
                new UserRelation(ACCOUNT_A, ACCOUNT_B, "T0"),
                new UserRelation(ACCOUNT_A, ACCOUNT_C, "T1"),
                new UserRelation(ACCOUNT_A, ACCOUNT_D, "T2"),
                new UserRelation(ACCOUNT_A, ACCOUNT_E, "T2")
        )));
    }

    @Test
    public void testGetOwnerRelations_noSuchOwner() throws UserRelationServiceException {
        Set<UserRelation> ownerRelations = relationService.getOwnerRelations(NONEXISTENT_ACCOUNT);

        assertThat(ownerRelations, is(empty()));
    }


    @Test
    public void testGetOwnerRelationsByType() throws UserRelationServiceException {
        Set<UserRelation> ownerRelations = relationService.getOwnerRelations(ACCOUNT_A, "T1");

        assertRelationsEqual(ownerRelations, new HashSet<>(Arrays.asList(
                new UserRelation(ACCOUNT_A, ACCOUNT_C, "T1")
        )));
    }

    @Test
    public void testGetRelationsByType_noSuchOwner() throws UserRelationServiceException {
        Set<UserRelation> ownerRelations = relationService.getOwnerRelations(NONEXISTENT_ACCOUNT, "T1");

        assertThat(ownerRelations, is(empty()));
    }

    @Test
    public void testGetRelationsByType_noSuchTypeForOwner() throws UserRelationServiceException {
        Set<UserRelation> ownerRelations = relationService.getOwnerRelations(ACCOUNT_C, "T2");

        assertThat(ownerRelations, is(empty()));
    }


    @Test
    public void testGetTargetRelations() throws UserRelationServiceException {
        Set<UserRelation> targetRelations = relationService.getTargetRelations(ACCOUNT_D);

        assertRelationsEqual(targetRelations, new HashSet<>(Arrays.asList(
                new UserRelation(ACCOUNT_C, ACCOUNT_D, "T0"),
                new UserRelation(ACCOUNT_A, ACCOUNT_D, "T2")
        )));
    }

    @Test
    public void testGetTargetRelations_noSuchTarget() throws UserRelationServiceException {
        Set<UserRelation> targetRelations = relationService.getTargetRelations(NONEXISTENT_ACCOUNT);

        assertThat(targetRelations, is(empty()));
    }


    @Test
    public void testGetTargetRelationsByType() throws UserRelationServiceException {
        Set<UserRelation> targetRelations = relationService.getTargetRelations(ACCOUNT_D, "T0");

        assertRelationsEqual(targetRelations, new HashSet<>(Arrays.asList(
                new UserRelation(ACCOUNT_C, ACCOUNT_D, "T0")
        )));
    }


    @Test(expected = UserRelationNotFoundException.class)
    public void testDeleteRelation() throws UserRelationServiceException {
        relationService.deleteRelation(ACCOUNT_A, ACCOUNT_B, "T0");

        relationService.getRelation(ACCOUNT_A, ACCOUNT_B, "T0");
    }

    @Test(expected = UserRelationNotFoundException.class)
    public void testDeleteRelation_nonExistingRelation() throws UserRelationServiceException {
        relationService.deleteRelation(NONEXISTENT_ACCOUNT, NONEXISTENT_ACCOUNT, "T0");
    }


    @Test
    public void testDeleteRelations() throws UserRelationServiceException {
        relationService.deleteRelations(ACCOUNT_A, ACCOUNT_B);

        assertThat(relationService.isRelated(ACCOUNT_A, ACCOUNT_B, "T0"), is(false));
    }


    @Test
    public void testDeleteOwnerRelations() throws UserRelationServiceException {
        relationService.deleteOwnerRelations(ACCOUNT_A);

        assertThat(relationService.getOwnerRelations(ACCOUNT_A), is(empty()));
    }


    @Test
    public void testDeleteTargetRelations() throws UserRelationServiceException {
        relationService.deleteTargetRelations(ACCOUNT_D);

        assertThat(relationService.getTargetRelations(ACCOUNT_D), is(empty()));
    }


    private void assertRelationsEqual(UserRelation a, UserRelation b) {
        assertThat(a.getOwner(), is(b.getOwner()));
        assertThat(a.getTarget(), is(b.getTarget()));
        assertThat(a.getRelationName(), is(b.getRelationName()));
    }

    private void assertRelationsEqual(Collection<UserRelation> actualRelations, Collection<UserRelation> expectedRelations) {
        assertThat(actualRelations.size(), is(expectedRelations.size()));

        for (UserRelation expectedRelation : expectedRelations) {
            if (!isContain(actualRelations, expectedRelation)) {
                fail(actualRelations + " does not contain " + expectedRelation);
            }
        }
    }

    private boolean isContain(Collection<UserRelation> relations, UserRelation expectedRelation) {
        for (UserRelation relation : relations) {
            if (isRelationsEqual(relation, expectedRelation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRelationsEqual(UserRelation actual, UserRelation expected) {
        return Objects.equals(actual.getOwner(), expected.getOwner())
                && Objects.equals(actual.getTarget(), expected.getTarget())
                && Objects.equals(actual.getRelationName(), expected.getRelationName());
    }
}
