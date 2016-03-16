package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;

import java.text.MessageFormat;

/**
 * @author bvanchuhov
 */
public class MatchNotFoundException extends MatchServiceException {

    public MatchNotFoundException(AccountId owner, AccountId target) {
        super(MessageFormat.format("Match [owner={0}, target={1}] not found", owner, target));
    }

    public MatchNotFoundException(Match match) {
        this(match.getOwner(), match.getTarget());
    }
}
