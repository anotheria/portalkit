package net.anotheria.portalkit.services.relation.storage;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User relation data object.
 *
 * @author asamoilich
 */
public class UserRelationData implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -2477261682140878871L;
    /**
     * Relation owner id.
     */
    private AccountId ownerId;
    /**
     * Relation partner id.
     */
    private AccountId partnerId;
    /**
     * Map of relations between owner and partner.
     */
    private Map<String, Relation> relationMap = new HashMap<String, Relation>();

    /**
     * Constructor.
     *
     * @param ownerId       owner id
     * @param partnerId     partner id
     * @param firstRelation first relation
     */
    public UserRelationData(AccountId ownerId, AccountId partnerId, Relation firstRelation) {
        this.ownerId = ownerId;
        this.partnerId = partnerId;
        relationMap.put(firstRelation.getName(), firstRelation);
    }

    /**
     * Constructor.
     *
     * @param ownerId   owner id
     * @param partnerId partner id
     */
    public UserRelationData(AccountId ownerId, AccountId partnerId) {
        this.ownerId = ownerId;
        this.partnerId = partnerId;
    }

    public AccountId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(AccountId ownerId) {
        this.ownerId = ownerId;
    }

    public AccountId getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(AccountId partnerId) {
        this.partnerId = partnerId;
    }

    public Map<String, Relation> getRelationMap() {
        return relationMap;
    }

    public void setRelationMap(Map<String, Relation> relationMap) {
        this.relationMap = relationMap;
    }

    @Override
    public String toString() {
        return "UserRelationData{" +
                "ownerId=" + ownerId +
                ", partnerId=" + partnerId +
                ", relationMap=" + relationMap +
                '}';
    }
}
