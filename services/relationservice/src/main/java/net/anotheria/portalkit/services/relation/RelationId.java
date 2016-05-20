package net.anotheria.portalkit.services.relation;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.http.util.Args;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author bvanchuhov
 */
public class RelationId implements Serializable {

    private static final long serialVersionUID = -2264297123183803167L;

    private String ownerId;
    private String partnerId;
    private String relationName;

    public RelationId() {
    }

    public RelationId(String ownerId, String partnerId, String relationName) {
        Args.notEmpty(ownerId, "owner id string");
        Args.notEmpty(partnerId, "partner id string");
        Args.notEmpty(relationName, "oldrelation name");

        this.ownerId = ownerId;
        this.partnerId = partnerId;
        this.relationName = relationName;
    }

    public RelationId(AccountId owner, AccountId partner, String relationName) {
        Args.notNull(owner, "owner id");
        Args.notNull(partner, "partner id");
        Args.notEmpty(relationName, "oldrelation name");

        this.ownerId = owner.getInternalId();
        this.partnerId = partner.getInternalId();
        this.relationName = relationName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationId that = (RelationId) o;
        return Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(partnerId, that.partnerId) &&
                Objects.equals(relationName, that.relationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, partnerId, relationName);
    }

    @Override
    public String toString() {
        return "UserRelationId{" +
                "ownerId='" + ownerId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", relationName='" + relationName + '\'' +
                '}';
    }
}
