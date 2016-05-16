package net.anotheria.portalkit.services.userrelation;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.http.util.Args;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author bvanchuhov
 */
@Entity
@Table(name = "userrelation")
@IdClass(UserRelationId.class)
@NamedQueries({
        @NamedQuery(
                name = UserRelationEntity.JPQL_GET_BY_OWNER,
                query = "select r from UserRelationEntity r where r.ownerId = :ownerId"
        ),
        @NamedQuery(
                name = UserRelationEntity.JPQL_GET_BY_TARGET,
                query = "select r from UserRelationEntity r where r.tagetId = :targetId"
        ),
        @NamedQuery(
                name = UserRelationEntity.JPQL_GET_BY_OWNER_TYPE,
                query = "select r from UserRelationEntity r where r.ownerId = :ownerId and r.relationName = :relationName"
        ),
        @NamedQuery(
                name = UserRelationEntity.JPQL_GET_BY_TARGET_TYPE,
                query = "select r from UserRelationEntity r where r.tagetId = :targetId and r.relationName = :relationName"
        ),
        @NamedQuery(
                name = UserRelationEntity.JPQL_DELETE_BY_OWNER_TARGET,
                query = "delete from UserRelationEntity r where r.ownerId = :ownerId and r.tagetId = :targetId"
        ),
        @NamedQuery(
                name = UserRelationEntity.JPQL_DELETE_BY_OWNER,
                query = "delete from UserRelationEntity r where r.ownerId = :ownerId"
        ),
        @NamedQuery(
                name = UserRelationEntity.JPQL_DELETE_BY_TARGET,
                query = "delete from UserRelationEntity r where r.tagetId = :targetId"
        )
})
public class UserRelationEntity {

    public static final String JPQL_GET_BY_OWNER = "UserRelation.getByOwner";
    public static final String JPQL_GET_BY_TARGET = "UserRelation.getByTarget";
    public static final String JPQL_GET_BY_OWNER_TYPE = "UserRelation.getByOwnerType";
    public static final String JPQL_GET_BY_TARGET_TYPE = "UserRelation.getByTargetType";

    public static final String JPQL_DELETE_BY_OWNER_TARGET = "UserRelation.deleteByOwnerType";
    public static final String JPQL_DELETE_BY_OWNER = "UserRelation.deleteByOwner";
    public static final String JPQL_DELETE_BY_TARGET = "UserRelation.deleteByOwner";

    @Id
    @Column(name = "owner", nullable = false, length = 128)
    private String ownerId;

    @Id
    @Column(name = "target", nullable = false, length = 128)
    private String targetId;

    @Id
    @Column(name = "type", nullable = false, length = 128)
    private String relationName;

    @Column(name = "dao_created")
    private long created;

    @Column(name = "dao_updated")
    private long updated;

    public UserRelationEntity() {
    }

    public UserRelationEntity(AccountId owner, AccountId target, String relationName) {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");
        Args.notEmpty(relationName, "relation name");

        this.ownerId = owner.getInternalId();
        this.targetId = target.getInternalId();
        this.relationName = relationName;
    }

    public UserRelationEntity(UserRelation userRelation) {
        Args.notNull(userRelation, "user relation");

        this.ownerId = userRelation.getOwner().getInternalId();
        this.targetId = userRelation.getTarget().getInternalId();
        this.relationName = userRelation.getRelationName();
        this.created = userRelation.getCreated();
        this.updated = userRelation.getUpdated();
    }

    public UserRelation toUserRelation() {
        return new UserRelation()
                .setOwner(new AccountId(ownerId))
                .setTarget(new AccountId(targetId))
                .setRelationName(relationName)
                .setCreated(created)
                .setUpdated(updated);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public UserRelationEntity setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getTargetId() {
        return targetId;
    }

    public UserRelationEntity setTargetId(String targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getRelationName() {
        return relationName;
    }

    public UserRelationEntity setRelationName(String relationName) {
        this.relationName = relationName;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public UserRelationEntity setCreated(long created) {
        this.created = created;
        return this;
    }

    public long getUpdated() {
        return updated;
    }

    public UserRelationEntity setUpdated(long updated) {
        this.updated = updated;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRelationEntity that = (UserRelationEntity) o;
        return created == that.created &&
                updated == that.updated &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(targetId, that.targetId) &&
                Objects.equals(relationName, that.relationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, targetId, relationName, created, updated);
    }

    @Override
    public String toString() {
        return "UserRelationEntity{" +
                "ownerId='" + ownerId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", relationName='" + relationName + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
