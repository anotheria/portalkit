package net.anotheria.portalkit.services.userrelation;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.http.util.Args;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author bvanchuhov
 */
public class UserRelationId implements Serializable {

    private static final long serialVersionUID = -2264297123183803167L;

    private String ownerId;
    private String targetId;
    private String relationName;

    public UserRelationId() {
    }

    public UserRelationId(String ownerId, String targetId, String relationName) {
        Args.notEmpty(ownerId, "owner id string");
        Args.notEmpty(targetId, "target id string");
        Args.notEmpty(relationName, "relation name");

        this.ownerId = ownerId;
        this.targetId = targetId;
        this.relationName = relationName;
    }

    public UserRelationId(AccountId owner, AccountId target, String relationName) {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");
        Args.notEmpty(relationName, "relation name");

        this.ownerId = owner.getInternalId();
        this.targetId = target.getInternalId();
        this.relationName = relationName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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
        UserRelationId that = (UserRelationId) o;
        return Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(targetId, that.targetId) &&
                Objects.equals(relationName, that.relationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, targetId, relationName);
    }

    @Override
    public String toString() {
        return "UserRelationId{" +
                "ownerId='" + ownerId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", relationName='" + relationName + '\'' +
                '}';
    }
}
