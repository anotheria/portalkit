package net.anotheria.portalkit.services.userrelation.exception;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.userrelation.UserRelation;

/**
 * @author bvanchuhov
 */
public class UserRelationAlreadyExistsException extends UserRelationServiceException {

    public UserRelationAlreadyExistsException(AccountId owner, AccountId target, String relationName) {
        super("User relation [owner=" + owner + ", target=" + target + ", relationName=" + relationName + "] already exists");
    }

    public UserRelationAlreadyExistsException(UserRelation relation) {
        this(relation.getOwner(), relation.getTarget(), relation.getRelationName());
    }
}
