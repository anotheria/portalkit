package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.http.util.Args;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Match entity
 *
 * @author bvanchuhov
 */
@Entity
@Table(name = "match")
@IdClass(MatchId.class)
@NamedQueries({
        @NamedQuery(
                name = Match.JPQL_GET_BY_OWNER,
                query = "select m from Match m where m.ownerId = :ownerId"
        ),
        @NamedQuery(
                name = Match.JPQL_GET_BY_OWNER_TYPE,
                query = "select m from Match m where m.ownerId = :ownerId and m.type = :type"
        ),
        @NamedQuery(
                name = Match.JPQL_GET_LATEST_BY_OWNER,
                query = "select m from Match m where m.ownerId = :ownerId order by m.created desc"
        ),
        @NamedQuery(
                name = Match.JPQL_GET_LATEST_BY_OWNER_TYPE,
                query = "select m from Match m where m.ownerId = :ownerId and m.type = :type order by m.created desc"
        ),
        @NamedQuery(
                name = Match.JPQL_DELETE_BY_OWNER,
                query = "delete from Match m where m.ownerId = :ownerId"
        ),
        @NamedQuery(
                name = Match.JPQL_DELETE_BY_TARGET,
                query = "delete from Match m where m.targetId = :targetId"
        ),
})
public class Match implements Serializable, Cloneable {

    public static final String JPQL_GET_BY_OWNER = "Match.getByOwner";
    public static final String JPQL_GET_BY_OWNER_TYPE = "Match.getByOwnerType";
    public static final String JPQL_GET_LATEST_BY_OWNER = "Match.getLatestByOwner";
    public static final String JPQL_GET_LATEST_BY_OWNER_TYPE = "Match.getLatestByOwnerType";
    public static final String JPQL_DELETE_BY_OWNER = "Match.deleteByOwner";
    public static final String JPQL_DELETE_BY_TARGET = "Match.deleteByTarget";

    private static final long serialVersionUID = -5247613267567452168L;

    private AccountId owner;
    private AccountId target;
    private int type;
    private long created;

    public Match() {
    }

    public Match(AccountId owner, AccountId target, int type) {
        this.owner = Args.notNull(owner, "owner");
        this.target = Args.notNull(target, "target");
        this.type = type;
    }

    @Id
    @Column(name = "owner", nullable = false)
    public String getOwnerId() {
        return owner.getInternalId();
    }

    public Match setOwnerId(String ownerId) {
        this.owner = new AccountId(ownerId);
        return this;
    }

    @Id
    @Column(name = "target", nullable = false)
    public String getTargetId() {
        return target.getInternalId();
    }

    public Match setTargetId(String targetId) {
        this.target = new AccountId(targetId);
        return this;
    }

    @Transient
    public AccountId getOwner() {
        return owner;
    }

    public Match setOwner(AccountId owner) {
        this.owner = Args.notNull(owner, "owner");
        return this;
    }

    @Transient
    public AccountId getTarget() {
        return target;
    }

    public Match setTarget(AccountId target) {
        this.target = Args.notNull(target, "target");
        return this;
    }

    @Column(name = "type", nullable = false)
    public int getType() {
        return type;
    }

    public Match setType(int type) {
        this.type = type;
        return this;
    }

    @Column(name = "created")
    public long getCreated() {
        return created;
    }

    public Match setCreated(long created) {
        this.created = created;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;
        Match match = (Match) o;
        return created == match.created &&
                Objects.equals(owner, match.owner) &&
                Objects.equals(target, match.target) &&
                type == match.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, target, type, created);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Match{");
        sb.append("owner=").append(owner);
        sb.append(", target=").append(target);
        sb.append(", type=").append(type);
        sb.append(", created=").append(new Date(created));
        sb.append('}');
        return sb.toString();
    }
}
