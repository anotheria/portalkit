package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.hc.core5.util.Args;

import java.io.Serializable;
import java.util.Objects;

/**
 * Match BO.
 *
 * @author bvanchuhov
 */
public class Match implements Serializable {

    /**
     * Generated serial version UID.
     * */
    private static final long serialVersionUID = -5247613267567452168L;

    private AccountId owner;
    private AccountId target;
    private int type;
    private boolean hidden;
    private long created;

    public Match() {
    }

    public Match(AccountId owner, AccountId target, int type) {
        this.owner = Args.notNull(owner, "owner");
        this.target = Args.notNull(target, "target");
        this.type = type;
    }

    public AccountId getOwner() {
        return owner;
    }

    public Match setOwner(AccountId owner) {
        this.owner = owner;
        return this;
    }

    public AccountId getTarget() {
        return target;
    }

    public Match setTarget(AccountId target) {
        this.target = target;
        return this;
    }

    public int getType() {
        return type;
    }

    public Match setType(int type) {
        this.type = type;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public Match setCreated(long created) {
        this.created = created;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Match setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return type == match.type &&
                hidden == match.hidden &&
                created == match.created &&
                Objects.equals(owner, match.owner) &&
                Objects.equals(target, match.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, target, type, hidden, created);
    }

    @Override
    public String toString() {
        return "Match{" +
                "owner=" + owner +
                ", target=" + target +
                ", type=" + type +
                ", hidden=" + hidden +
                ", created=" + created +
                '}';
    }
}
