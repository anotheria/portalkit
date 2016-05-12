package net.anotheria.portalkit.services.match.exception;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.Match;

/**
 * @author bvanchuhov
 */
public class MatchAlreadyExistsException extends MatchServiceException {

    public MatchAlreadyExistsException(AccountId owner, AccountId target, int type) {
        super("Match [owner=" + owner + ", target=" + target + ", type=" + type + "] already exists");
    }

    public MatchAlreadyExistsException(Match match) {
        this(match.getOwner(), match.getTarget(), match.getType());
    }
}
