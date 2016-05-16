package net.anotheria.portalkit.services.userrelation;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.http.util.Args;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author bvanchuhov
 */
public class UserRelation implements Serializable {

    private AccountId owner;
    private AccountId target;
    private String relationName;
    private long created;
    private long updated;

    public UserRelation() {
    }

    public UserRelation(AccountId owner, AccountId target, String relationName) {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");
        Args.notBlank(relationName, "relation name");

        this.owner = owner;
        this.target = target;
        this.relationName = relationName;
    }

    public AccountId getOwner() {
        return owner;
    }

    public UserRelation setOwner(AccountId owner) {
        this.owner = owner;
        return this;
    }

    public AccountId getTarget() {
        return target;
    }

    public UserRelation setTarget(AccountId target) {
        this.target = target;
        return this;
    }

    public String getRelationName() {
        return relationName;
    }

    public UserRelation setRelationName(String relationName) {
        this.relationName = relationName;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public UserRelation setCreated(long created) {
        this.created = created;
        return this;
    }

    public long getUpdated() {
        return updated;
    }

    public UserRelation setUpdated(long updated) {
        this.updated = updated;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRelation that = (UserRelation) o;
        return created == that.created &&
                updated == that.updated &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(target, that.target) &&
                Objects.equals(relationName, that.relationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, target, relationName, created, updated);
    }

    @Override
    public String toString() {
        return "UserRelation{" +
                "owner=" + owner +
                ", target=" + target +
                ", relationName='" + relationName + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
