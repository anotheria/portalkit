package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.http.util.Args;

import java.io.Serializable;
import java.util.Objects;

/**
 * Match BO.
 *
 * @author bvanchuhov
 */
public class Match implements Serializable, Cloneable {

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
        return "Match{" +
                "owner=" + owner +
                ", target=" + target +
                ", type=" + type +
                ", created=" + created +
                '}';
    }
}
