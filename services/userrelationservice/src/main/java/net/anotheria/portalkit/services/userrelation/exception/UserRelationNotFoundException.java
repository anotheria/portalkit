package net.anotheria.portalkit.services.userrelation.exception;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.userrelation.UserRelation;

/**
 * @author bvanchuhov
 */
public class UserRelationNotFoundException extends UserRelationServiceException {

    public UserRelationNotFoundException(AccountId owner, AccountId target, String relationName) {
        super("User relation [owner=" + owner + ", target=" + target + ", relationName=" + relationName + "] not found");
    }

    public UserRelationNotFoundException(UserRelation relation) {
        this(relation.getOwner(), relation.getTarget(), relation.getRelationName());
    }
}
