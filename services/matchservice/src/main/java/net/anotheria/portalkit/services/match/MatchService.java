package net.anotheria.portalkit.services.match;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

/**
 * The interface describes the match service, where all matches are managed.
 *
 * @author bvanchuhov
 */
public interface MatchService extends Service {

    /**
     *
     * @param owner
     * @param target
     * @param type
     * @throws MatchServiceException
     * @throws MatchAlreadyExistsException
     */
    void addMatch(AccountId owner, AccountId target, int type) throws MatchServiceException;

    /**
     *
     * @param match
     * @throws MatchServiceException
     * @throws MatchAlreadyExistsException
     */
    void addMatch(Match match) throws MatchServiceException;

    /**
     *
     * @param owner
     * @return
     * @throws MatchServiceException
     */
    List<Match> getMatches(AccountId owner) throws MatchServiceException;

    /**
     *
     * @param owner
     * @param type
     * @return
     * @throws MatchServiceException
     */
    List<Match> getMatchesByType(AccountId owner, int type) throws MatchServiceException;

    /**
     *
     * @param owner
     * @param type
     * @param limit
     * @return
     * @throws MatchServiceException
     */
    List<Match> getLatestMatchesByType(AccountId owner, int type, int limit) throws MatchServiceException;

    /**
     *
     * @param owner
     * @param limit
     * @return
     * @throws MatchServiceException
     */
    List<Match> getLatestMatches(AccountId owner, int limit) throws MatchServiceException;

    /**
     *
     * @param owner
     * @param target
     * @throws MatchServiceException
     */
    void deleteMatches(AccountId owner, AccountId target) throws MatchServiceException;

    /**
     *
     * @param owner
     * @throws MatchServiceException
     */
    void deleteMatchesByOwner(AccountId owner) throws MatchServiceException;

    /**
     *
     * @param target
     * @throws MatchServiceException
     */
    void deleteMatchesByTarget(AccountId target) throws MatchServiceException;
}
