package net.anotheria.portalkit.services.relation;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.http.util.Args;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author bvanchuhov
 */
public class Relation implements Serializable {

    /**
     * Generated serial version UID.
     * */
    private static final long serialVersionUID = -8647974212069449297L;

    private AccountId owner;
    private AccountId partner;
    private String relationName;
    private long created;
    private long updated;

    public Relation() {
    }

    public Relation(AccountId owner, AccountId partner, String relationName) {
        Args.notNull(owner, "owner id");
        Args.notNull(partner, "partner id");
        Args.notBlank(relationName, "relation name");

        this.owner = owner;
        this.partner = partner;
        this.relationName = relationName;
    }

    public AccountId getOwner() {
        return owner;
    }

    public Relation setOwner(AccountId owner) {
        this.owner = owner;
        return this;
    }

    public AccountId getPartner() {
        return partner;
    }

    public Relation setPartner(AccountId partner) {
        this.partner = partner;
        return this;
    }

    public String getRelationName() {
        return relationName;
    }

    public Relation setRelationName(String relationName) {
        this.relationName = relationName;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public Relation setCreated(long created) {
        this.created = created;
        return this;
    }

    public long getUpdated() {
        return updated;
    }

    public Relation setUpdated(long updated) {
        this.updated = updated;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation that = (Relation) o;
        return Objects.equals(owner, that.owner) &&
                Objects.equals(partner, that.partner) &&
                Objects.equals(relationName, that.relationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, partner, relationName);
    }

    @Override
    public String toString() {
        return "UserRelation{" +
                "owner=" + owner +
                ", partner=" + partner +
                ", relationName='" + relationName + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
