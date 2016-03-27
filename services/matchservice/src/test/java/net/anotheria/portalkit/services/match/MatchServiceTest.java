package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;
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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

/**
 * Integration test for MatchService.
 *
 * @author bvanchuhov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MatchSpringConfiguration.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MatchServiceTest {

    private static final AccountId ACCOUNT_A = new AccountId("A");
    private static final AccountId ACCOUNT_B = new AccountId("B");
    private static final AccountId ACCOUNT_C = new AccountId("C");
    private static final AccountId ACCOUNT_D = new AccountId("D");
    private static final AccountId ACCOUNT_E = new AccountId("E");

    private static final AccountId NONEXISTENT_ACCOUNT = new AccountId("Nonexistent");

    @Autowired
    private MatchService matchService;

    @Test
    public void smokeTest() throws Exception {
        System.out.println("OK");
    }

    @Before
    public void fillDatabase() throws MatchServiceException {
        matchService.addMatch(new Match(ACCOUNT_A, ACCOUNT_B, 0).setCreated(10));
        matchService.addMatch(new Match(ACCOUNT_A, ACCOUNT_C, 1).setCreated(1));
        matchService.addMatch(new Match(ACCOUNT_C, ACCOUNT_D, 0).setCreated(20));
        matchService.addMatch(new Match(ACCOUNT_A, ACCOUNT_D, 2).setCreated(100));
        matchService.addMatch(new Match(ACCOUNT_A, ACCOUNT_E, 2).setCreated(5));
    }

    @Test
    public void testGetMatchesByOwner() throws MatchServiceException {
        List<Match> matches = matchService.getMatches(ACCOUNT_A);

        assertThat(matches.size(), is(4));
        assertMatchesEqual(matches.get(0), new Match(ACCOUNT_A, ACCOUNT_B, 0));
        assertMatchesEqual(matches.get(1), new Match(ACCOUNT_A, ACCOUNT_C, 1));
        assertMatchesEqual(matches.get(2), new Match(ACCOUNT_A, ACCOUNT_D, 2));
        assertMatchesEqual(matches.get(3), new Match(ACCOUNT_A, ACCOUNT_E, 2));
    }

    @Test
    public void testGetMatchesByOwner_noSuchOwner() throws MatchServiceException {
        List<Match> matches = matchService.getMatches(NONEXISTENT_ACCOUNT);

        assertThat(matches, is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatchesByOwner_nullOwner() throws MatchServiceException {
        matchService.getMatches(null);
    }

    @Test
    public void testGetMatchesByOwnerType() throws MatchServiceException {
        List<Match> matches = matchService.getMatchesByType(ACCOUNT_A, 1);

        assertThat(matches.size(), is(1));
        assertMatchesEqual(matches.get(0), new Match(ACCOUNT_A, ACCOUNT_C, 1));
    }

    @Test
    public void testGetMatchesByOwnerType_noSuchOwner() throws MatchServiceException {
        List<Match> matches = matchService.getMatchesByType(NONEXISTENT_ACCOUNT, 1);

        assertThat(matches, is(empty()));
    }

    @Test
    public void testGetMatchesByOwnerType_noSuchTypeForOwner() throws MatchServiceException {
        List<Match> matches = matchService.getMatchesByType(ACCOUNT_C, 2);

        assertThat(matches, is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatchesByOwnerType_nullOwner() throws MatchServiceException {
        matchService.getMatchesByType(null, 2);
    }

    @Test
    public void testAddMatch() throws MatchServiceException {
        matchService.addMatch(ACCOUNT_C, ACCOUNT_A, 1);

        List<Match> matches = matchService.getMatches(ACCOUNT_C);
        assertMatchesEqual(matches.get(0), new Match(ACCOUNT_C, ACCOUNT_A, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMatch_nullOwner() throws MatchServiceException {
        matchService.addMatch(null, ACCOUNT_A, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMatch_nullTarget() throws MatchServiceException {
        matchService.addMatch(ACCOUNT_C, null, 1);
    }

    @Test(expected = MatchAlreadyExistsException.class)
    public void testAddMatch_matchAlreadyExists() throws MatchServiceException {
        matchService.addMatch(ACCOUNT_A, ACCOUNT_B, 10);
    }

    @Test
    public void testGetLatestMatchesByOwner() throws MatchServiceException {
        List<Match> matches = matchService.getLatestMatches(ACCOUNT_A, 2);

        assertThat(matches, contains(
                new Match(ACCOUNT_A, ACCOUNT_D, 2).setCreated(100),
                new Match(ACCOUNT_A, ACCOUNT_B, 0).setCreated(10)
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwner_nullOwner() throws MatchServiceException {
        matchService.getLatestMatches(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwner_notPositiveLimit_zeroLimit() throws MatchServiceException {
        matchService.getLatestMatches(ACCOUNT_A, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwner_notPositiveLimit_negativeLimit() throws MatchServiceException {
        matchService.getLatestMatches(ACCOUNT_A, -1);
    }

    @Test
    public void testGetLatestMatchesByOwnerType() throws MatchServiceException {
        List<Match> matches = matchService.getLatestMatchesByType(ACCOUNT_A, 2, 1);

        assertThat(matches, contains(
                new Match(ACCOUNT_A, ACCOUNT_D, 2).setCreated(100)
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwnerType_nullOwner() throws MatchServiceException {
        matchService.getLatestMatchesByType(null, 2, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwnerType_notPositiveLimit_zeroLimit() throws MatchServiceException {
        matchService.getLatestMatchesByType(ACCOUNT_A, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwnerType_notPositiveLimit_negativeLimit() throws MatchServiceException {
        matchService.getLatestMatchesByType(ACCOUNT_A, 2, -1);
    }

    @Test
    public void testDeleteMatches() throws MatchServiceException {
        matchService.deleteMatches(ACCOUNT_A, ACCOUNT_B);

        List<Match> matchesA = matchService.getMatches(ACCOUNT_A);
        assertThat(matchesA.contains(new Match(ACCOUNT_A, ACCOUNT_B, 0)), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMatches_nullOwner() throws MatchServiceException {
        matchService.deleteMatches(null, ACCOUNT_B);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMatches_nullTarget() throws MatchServiceException {
        matchService.deleteMatches(ACCOUNT_A, null);
    }

    @Test
    public void testDeleteMatchesByOwner() throws MatchServiceException {
        matchService.deleteMatchesByOwner(ACCOUNT_A);

        assertThat(matchService.getMatches(ACCOUNT_A), is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMatchesByOwner_nullOwner() throws MatchServiceException {
        matchService.deleteMatchesByOwner(null);
    }

    @Test
    public void testDeleteMatchesByTarget() throws MatchServiceException {
        matchService.deleteMatchesByTarget(ACCOUNT_D);

        assertThat(matchService.getMatches(ACCOUNT_C), is(empty()));

        assertMatchesEqual(matchService.getMatches(ACCOUNT_A), Arrays.asList(
                new Match(ACCOUNT_A, ACCOUNT_B, 0),
                new Match(ACCOUNT_A, ACCOUNT_C, 1),
                new Match(ACCOUNT_A, ACCOUNT_E, 2)
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMatchesByTarget_nullTarget() throws MatchServiceException {
        matchService.deleteMatchesByTarget(null);
    }

    private void assertMatchesEqual(Match a, Match b) {
        assertThat(a.getOwner(), is(b.getOwner()));
        assertThat(a.getTarget(), is(b.getTarget()));
        assertThat(a.getType(), is(b.getType()));
    }

    private void assertMatchesEqual(List<Match> result, List<Match> expected) {
        assertThat(result.size(), is(expected.size()));

        Iterator<Match> resultIterator = result.iterator();
        Iterator<Match> expectedIterator = expected.iterator();
        while (resultIterator.hasNext() && expectedIterator.hasNext()) {
            assertMatchesEqual(resultIterator.next(), expectedIterator.next());
        }
    }
}
