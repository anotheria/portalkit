package net.anotheria.portalkit.services.match.exception;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.Match;

/**
 * @author bvanchuhov
 */
public class MatchNotFoundException extends MatchServiceException {

    public MatchNotFoundException(AccountId owner, AccountId target, int type) {
        super("Match [owner=" + owner + ", target=" + target + ", type=" + type + "] not found");
    }

    public MatchNotFoundException(Match match) {
        this(match.getOwner(), match.getTarget(), match.getType());
    }
}
