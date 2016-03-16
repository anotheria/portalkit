package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;

import java.text.MessageFormat;

/**
 * @author bvanchuhov
 */
public class MatchAlreadyExistsException extends MatchServiceException {

    public MatchAlreadyExistsException(AccountId owner, AccountId target) {
        super(MessageFormat.format("Match [owner={0}, target={1}] already exists", owner, target));
    }

    public MatchAlreadyExistsException(Match match) {
        this(match.getOwner(), match.getTarget());
    }
}
