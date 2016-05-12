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
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

    private static final AccountId ACCOUNT_X = new AccountId("X");
    private static final AccountId ACCOUNT_Y = new AccountId("Y");

    private static final AccountId NONEXISTENT_ACCOUNT = new AccountId("Nonexistent");

    @Autowired
    private MatchService matchService;

    @Before
    public void before() throws MatchServiceException {
        fillDatabase();
    }

    public void fillDatabase() throws MatchServiceException {
        matchService.addMatch(new Match(ACCOUNT_A, ACCOUNT_B, 0).setCreated(10L));
        matchService.addMatch(new Match(ACCOUNT_A, ACCOUNT_C, 1).setCreated(1L));
        matchService.addMatch(new Match(ACCOUNT_C, ACCOUNT_D, 0).setCreated(20L));
        matchService.addMatch(new Match(ACCOUNT_A, ACCOUNT_D, 2).setCreated(100L));
        matchService.addMatch(new Match(ACCOUNT_A, ACCOUNT_E, 2).setCreated(5L));

        matchService.addMatch(new Match(ACCOUNT_X, ACCOUNT_Y, 0).setHidden(true).setCreated(30L));
    }

    @Test
    public void smokeTest() throws Exception {
        System.out.println("OK");
    }

    @Test
    public void testMatchFieldsIntegrity() throws Exception {
        List<Match> matches = matchService.getMatches(ACCOUNT_X);

        Match match = matches.get(0);
        assertThat(match.getOwner(), is(ACCOUNT_X));
        assertThat(match.getTarget(), is(ACCOUNT_Y));
        assertThat(match.isHidden(), is(true));
        assertThat(match.getCreated(), is(30L));
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
    public void testGetMatchesByTarget() throws MatchServiceException {
        List<Match> matches = matchService.getTargetMatches(ACCOUNT_D);

        assertThat(matches.size(), is(2));
        assertMatchesEqual(matches.get(0), new Match(ACCOUNT_C, ACCOUNT_D, 0));
        assertMatchesEqual(matches.get(1), new Match(ACCOUNT_A, ACCOUNT_D, 2));
    }

    @Test
    public void testGetMatchesByTarget_noSuchOwner() throws MatchServiceException {
        List<Match> matches = matchService.getTargetMatches(NONEXISTENT_ACCOUNT);

        assertThat(matches, is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatchesByTarget_nullOwner() throws MatchServiceException {
        matchService.getTargetMatches(null);
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
    public void testGetMatchesByTargetType() throws MatchServiceException {
        List<Match> matches = matchService.getTargetMatchesByType(ACCOUNT_D, 0);

        assertThat(matches.size(), is(1));
        assertMatchesEqual(matches.get(0), new Match(ACCOUNT_C, ACCOUNT_D, 0));
    }

    @Test
    public void testGetMatchesByTargetType_noSuchOwner() throws MatchServiceException {
        List<Match> matches = matchService.getTargetMatchesByType(NONEXISTENT_ACCOUNT, 1);

        assertThat(matches, is(empty()));
    }

    @Test
    public void testGetMatchesByTargetType_noSuchTypeForOwner() throws MatchServiceException {
        List<Match> matches = matchService.getTargetMatchesByType(ACCOUNT_C, 10);

        assertThat(matches, is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatchesByTargetType_nullOwner() throws MatchServiceException {
        matchService.getTargetMatchesByType(null, 2);
    }

    @Test
    public void testAddMatch() throws MatchServiceException {
        matchService.addMatch(ACCOUNT_C, ACCOUNT_A, 1);

        List<Match> matches = matchService.getMatches(ACCOUNT_C);
        assertMatchesEqualIgnoreOrder(matches, Arrays.asList(
                new Match(ACCOUNT_C, ACCOUNT_D, 0),
                new Match(ACCOUNT_C, ACCOUNT_A, 1)
        ));
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

    @Test
    public void testIsMatched_existingEntity() throws MatchServiceException {
        assertThat(matchService.isMatched(ACCOUNT_A, ACCOUNT_B, 0), is(true));
    }

    @Test
    public void testIsMatched_nonExistingEntity() throws MatchServiceException {
        assertThat(matchService.isMatched(ACCOUNT_B, ACCOUNT_A, 0), is(false));
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

    private void assertMatchesEqualIgnoreOrder(List<Match> actualMatches, List<Match> expectedMatches) {
        assertThat(actualMatches.size(), is(expectedMatches.size()));

        for (Match expectedMatch : expectedMatches) {
            if (!isContain(actualMatches, expectedMatch)) {
                fail(actualMatches + " does not contain " + expectedMatch);
            }
        }
    }

    private boolean isContain(List<Match> matches, Match expectedMatch) {
        for (Match match : matches) {
            if (isMachEqual(match, expectedMatch)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMachEqual(Match actual, Match expected) {
        return Objects.equals(actual.getOwner(), expected.getOwner())
                && Objects.equals(actual.getTarget(), expected.getTarget())
                && actual.getType() == expected.getType();
    }
}
