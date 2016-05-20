package net.anotheria.portalkit.services.relation.exception;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.Relation;

/**
 * @author bvanchuhov
 */
public class RelationAlreadyExistsException extends RelationServiceException {

    public RelationAlreadyExistsException(AccountId owner, AccountId partner, String relationName) {
        super("User oldrelation [owner=" + owner + ", partner=" + partner + ", relationName=" + relationName + "] already exists");
    }

    public RelationAlreadyExistsException(Relation relation) {
        this(relation.getOwner(), relation.getPartner(), relation.getRelationName());
    }
}
