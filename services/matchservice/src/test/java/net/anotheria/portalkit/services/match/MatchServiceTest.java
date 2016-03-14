package net.anotheria.portalkit.services.match;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.conf.MatchSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

/**
 * Integration test for MatchService.
 * @author bvanchuhov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MatchSpringConfiguration.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup("/dbunit/match/all-matches.xml")
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
    public void testGetMatchesByOwner() {
        List<Match> matches = matchService.getMatches(ACCOUNT_A);

        assertThat(matches.size(), is(4));
        assertMatchesEqual(matches.get(0), new Match(ACCOUNT_A, ACCOUNT_B, 0));
        assertMatchesEqual(matches.get(1), new Match(ACCOUNT_A, ACCOUNT_C, 1));
        assertMatchesEqual(matches.get(2), new Match(ACCOUNT_A, ACCOUNT_D, 2));
        assertMatchesEqual(matches.get(3), new Match(ACCOUNT_A, ACCOUNT_E, 2));
    }

    @Test
    public void testGetMatchesByOwner_noSuchOwner() {
        List<Match> matches = matchService.getMatches(NONEXISTENT_ACCOUNT);

        assertThat(matches, is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatchesByOwner_nullOwner() {
        matchService.getMatches(null);
    }

    @Test
    public void testGetMatchesByOwnerType() {
        List<Match> matches = matchService.getMatchesByType(ACCOUNT_A, 1);

        assertThat(matches.size(), is(1));
        assertMatchesEqual(matches.get(0), new Match(ACCOUNT_A, ACCOUNT_C, 1));
    }

    @Test
    public void testGetMatchesByOwnerType_noSuchOwner() {
        List<Match> matches = matchService.getMatchesByType(NONEXISTENT_ACCOUNT, 1);

        assertThat(matches, is(empty()));
    }

    @Test
    public void testGetMatchesByOwnerType_noSuchTypeForOwner() {
        List<Match> matches = matchService.getMatchesByType(ACCOUNT_C, 2);

        assertThat(matches, is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatchesByOwnerType_nullOwner() {
        matchService.getMatchesByType(null, 2);
    }

    @Test
    @ExpectedDatabase(value = "/dbunit/match/matches-after-insert.xml", assertionMode = NON_STRICT_UNORDERED)
    public void testAddMatch() {
        matchService.addMatch(ACCOUNT_C, ACCOUNT_A, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMatch_nullOwner() {
        matchService.addMatch(null, ACCOUNT_A, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMatch_nullTarget() {
        matchService.addMatch(ACCOUNT_C, null, 1);
    }

    @Test
    public void testGetLatestMatchesByOwner() {
        List<Match> matches = matchService.getLatestMatches(ACCOUNT_A, 2);

        assertThat(matches, contains(
                new Match(ACCOUNT_A, ACCOUNT_D, 2).setCreated(100),
                new Match(ACCOUNT_A, ACCOUNT_B, 0).setCreated(10)
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwner_nullOwner() {
        matchService.getLatestMatches(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwner_notPositiveLimit_zeroLimit() {
        matchService.getLatestMatches(ACCOUNT_A, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwner_notPositiveLimit_negativeLimit() {
        matchService.getLatestMatches(ACCOUNT_A, -1);
    }

    @Test
    public void testGetLatestMatchesByOwnerType() {
        List<Match> matches = matchService.getLatestMatchesByType(ACCOUNT_A, 2, 1);

        assertThat(matches, contains(
                new Match(ACCOUNT_A, ACCOUNT_D, 2).setCreated(100)
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwnerType_nullOwner() {
        matchService.getLatestMatchesByType(null, 2, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwnerType_notPositiveLimit_zeroLimit() {
        matchService.getLatestMatchesByType(ACCOUNT_A, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestMatchesByOwnerType_notPositiveLimit_negativeLimit() {
        matchService.getLatestMatchesByType(ACCOUNT_A, 2, -1);
    }

    @Test
    @ExpectedDatabase(value = "/dbunit/match/matches-after-delete-by-owner.xml", assertionMode = NON_STRICT_UNORDERED)
    public void testDeleteMatchesByOwner() {
        matchService.deleteMatchesByOwner(ACCOUNT_A);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMatchesByOwner_nullOwner() {
        matchService.deleteMatchesByOwner(null);
    }

    @Test
    @ExpectedDatabase(value = "/dbunit/match/matches-after-delete-by-target.xml", assertionMode = NON_STRICT_UNORDERED)
    public void testDeleteMatchesByTarget() {
        matchService.deleteMatchesByTarget(ACCOUNT_D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMatchesByTarget_nullTarget() {
        matchService.deleteMatchesByTarget(null);
    }

    private void assertMatchesEqual(Match a, Match b) {
        assertThat(a.getOwner(), is(b.getOwner()));
        assertThat(a.getTarget(), is(b.getTarget()));
        assertThat(a.getType(), is(b.getType()));
    }
}

