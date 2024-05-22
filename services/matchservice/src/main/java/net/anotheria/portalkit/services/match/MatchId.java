package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.hc.core5.util.Args;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for {@link Match}. Using in JPA mapping
 * @author bvanchuhov
 */
public class MatchId implements Serializable {

    private static final long serialVersionUID = 1349133656436371867L;

    private String ownerId;
    private String targetId;
    private int type;

    public MatchId() {
    }

    public MatchId(String ownerId, String targetId, int type) {
        Args.notEmpty(ownerId, "owner id string");
        Args.notEmpty(targetId, "target id string");

        this.ownerId = ownerId;
        this.targetId = targetId;
        this.type = type;
    }

    public MatchId(AccountId owner, AccountId target, int type) {
        Args.notNull(owner, "owner id");
        Args.notNull(target, "target id");

        this.ownerId = owner.getInternalId();
        this.targetId = target.getInternalId();
        this.type = type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public MatchId setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getTargetId() {
        return targetId;
    }

    public MatchId setTargetId(String targetId) {
        this.targetId = targetId;
        return this;
    }

    public int getType() {
        return type;
    }

    public MatchId setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchId matchId = (MatchId) o;
        return type == matchId.type &&
                Objects.equals(ownerId, matchId.ownerId) &&
                Objects.equals(targetId, matchId.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, targetId, type);
    }

    @Override
    public String toString() {
        return "MatchId{" +
                "ownerId='" + ownerId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", type=" + type +
                '}';
    }
}
