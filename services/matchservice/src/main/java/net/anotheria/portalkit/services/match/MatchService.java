package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.entity.Match;
import net.anotheria.portalkit.services.match.entity.MatchType;

import java.util.List;

/**
 * @author bvanchuhov
 */
public interface MatchService {

    void addMatch(AccountId owner, AccountId target, MatchType type);
    List<Match> getMatches(AccountId owner);
    List<Match> getMatchesByType(AccountId owner, MatchType type);
    List<Match> getLatestMatchesByType(AccountId owner, MatchType type, int limit);
    List<Match> getLatestMatches(AccountId owner, int limit);
    void deleteMatchesByOwner(AccountId owner);
    void deleteMatchesByTarget(AccountId target);
}
