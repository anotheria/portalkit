package net.anotheria.portalkit.services.match;

import javax.persistence.*;
import java.util.Objects;

/**
 * Match entity for JPA.
 *
 * @author bvanchuhov
 */
@Entity
@Table(name = "match")
@IdClass(MatchId.class)
@NamedQueries({
        @NamedQuery(
                name = MatchEntity.JPQL_GET_BY_OWNER,
                query = "select m from MatchEntity m where m.ownerId = :ownerId"
        ),
        @NamedQuery(
                name = MatchEntity.JPQL_GET_BY_TARGET,
                query = "select m from MatchEntity m where m.targetId = :targetId"
        ),
        @NamedQuery(
                name = MatchEntity.JPQL_GET_BY_OWNER_TYPE,
                query = "select m from MatchEntity m where m.ownerId = :ownerId and m.type = :type"
        ),
        @NamedQuery(
                name = MatchEntity.JPQL_GET_BY_TARGET_TYPE,
                query = "select m from MatchEntity m where m.targetId = :targetId and m.type = :type"
        ),
        @NamedQuery(
                name = MatchEntity.JPQL_GET_LATEST_BY_OWNER,
                query = "select m from MatchEntity m where m.ownerId = :ownerId order by m.created desc"
        ),
        @NamedQuery(
                name = MatchEntity.JPQL_GET_LATEST_BY_OWNER_TYPE,
                query = "select m from MatchEntity m where m.ownerId = :ownerId and m.type = :type order by m.created desc"
        ),
        @NamedQuery(
                name = MatchEntity.JPQL_DELETE_BY_OWNER_TARGET,
                query = "delete from MatchEntity m where m.ownerId = :ownerId and m.targetId = :targetId"
        ),
        @NamedQuery(
                name = MatchEntity.JPQL_DELETE_BY_OWNER,
                query = "delete from MatchEntity m where m.ownerId = :ownerId"
        ),
        @NamedQuery(
                name = MatchEntity.JPQL_DELETE_BY_TARGET,
                query = "delete from MatchEntity m where m.targetId = :targetId"
        ),
})
public class MatchEntity {

    public static final String JPQL_GET_BY_OWNER = "Match.getByOwner";
    public static final String JPQL_GET_BY_TARGET = "Match.getByTarget";
    public static final String JPQL_GET_BY_OWNER_TYPE = "Match.getByOwnerType";
    public static final String JPQL_GET_BY_TARGET_TYPE = "Match.getByTargetType";
    public static final String JPQL_GET_LATEST_BY_OWNER = "Match.getLatestByOwner";
    public static final String JPQL_GET_LATEST_BY_OWNER_TYPE = "Match.getLatestByOwnerType";
    public static final String JPQL_DELETE_BY_OWNER_TARGET = "Match.deleteByOwnerTarget";
    public static final String JPQL_DELETE_BY_OWNER = "Match.deleteByOwner";
    public static final String JPQL_DELETE_BY_TARGET = "Match.deleteByTarget";

    @Id
    @Column(name = "owner", nullable = false, length = 128)
    private String ownerId;

    @Id
    @Column(name = "target", nullable = false, length = 128)
    private String targetId;

    @Id
    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "hidden")
    private boolean hidden;

    @Column(name = "created")
    private long created;

    public String getOwnerId() {
        return ownerId;
    }

    public MatchEntity setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getTargetId() {
        return targetId;
    }

    public MatchEntity setTargetId(String targetId) {
        this.targetId = targetId;
        return this;
    }

    public int getType() {
        return type;
    }

    public MatchEntity setType(int type) {
        this.type = type;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public MatchEntity setCreated(long created) {
        this.created = created;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public MatchEntity setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchEntity that = (MatchEntity) o;
        return type == that.type &&
                hidden == that.hidden &&
                created == that.created &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(targetId, that.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, targetId, type, hidden, created);
    }

    @Override
    public String toString() {
        return "MatchEntity{" +
                "ownerId='" + ownerId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", type=" + type +
                ", hidden=" + hidden +
                ", created=" + created +
                '}';
    }
}
