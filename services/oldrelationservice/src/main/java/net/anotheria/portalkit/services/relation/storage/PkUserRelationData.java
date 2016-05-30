package net.anotheria.portalkit.services.relation.storage;

import net.anotheria.anoprise.dualcrud.CrudSaveable;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.HashMap;
import java.util.Map;

/**
 * User relation data object.
 *
 * @author asamoilich
 */
@Deprecated
public class PkUserRelationData implements CrudSaveable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -2477261682140878871L;

    private String id;
    /**
     * Relation owner id.
     */
    private AccountId owner;
    /**
     * Relation partner id.
     */
    private AccountId partner;

    private PkRelation toCreate;
    /**
     * Map of relations between owner and partner.
     */
    private Map<String, PkRelation> relationMap = new HashMap<String, PkRelation>();

    /**
     * Constructor.
     *
     * @param ownerId       owner id
     * @param partnerId     partner id
     * @param firstRelation first relation
     */
    public PkUserRelationData(AccountId ownerId, AccountId partnerId, PkRelation firstRelation) {
        this.owner = ownerId;
        this.partner = partnerId;
        this.toCreate = firstRelation;
        relationMap.put(firstRelation.getName(), firstRelation);
    }

    /**
     * Constructor.
     *
     * @param ownerId   owner id
     * @param partnerId partner id
     */
    public PkUserRelationData(AccountId ownerId, AccountId partnerId) {
        this.owner = ownerId;
        this.partner = partnerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return owner.getInternalId();
    }

    public void setOwner(AccountId owner) {
        this.owner = owner;
    }

    public AccountId getPartner() {
        return partner;
    }

    public void setPartner(AccountId partner) {
        this.partner = partner;
    }

    public Map<String, PkRelation> getRelationMap() {
        return relationMap;
    }

    public void setRelationMap(Map<String, PkRelation> relationMap) {
        this.relationMap = relationMap;
    }

    public PkRelation getRelationToCreate() {
        return toCreate;
    }

    @Override
    public String toString() {
        return "UserRelationData{" +
                "owner=" + owner +
                ", partner=" + partner +
                ", relationMap=" + relationMap +
                '}';
    }
}
