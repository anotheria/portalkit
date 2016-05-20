package net.anotheria.portalkit.services.relation.exception;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.Relation;

/**
 * @author bvanchuhov
 */
public class RelationNotFoundException extends RelationServiceException {

    public RelationNotFoundException(AccountId owner, AccountId partner, String relationName) {
        super("User oldrelation [owner=" + owner + ", partner=" + partner + ", relationName=" + relationName + "] not found");
    }

    public RelationNotFoundException(Relation relation) {
        this(relation.getOwner(), relation.getPartner(), relation.getRelationName());
    }
}
