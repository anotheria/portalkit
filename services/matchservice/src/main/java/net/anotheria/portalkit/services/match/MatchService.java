package net.anotheria.portalkit.services.match;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.entity.Match;

import java.util.List;

/**
 * @author bvanchuhov
 */
public interface MatchService extends Service {

    void addMatch(AccountId owner, AccountId target, int type);
    List<Match> getMatches(AccountId owner);
    List<Match> getMatchesByType(AccountId owner, int type);
    List<Match> getLatestMatchesByType(AccountId owner, int type, int limit);
    List<Match> getLatestMatches(AccountId owner, int limit);
    void deleteMatchesByOwner(AccountId owner);
    void deleteMatchesByTarget(AccountId target);
}
