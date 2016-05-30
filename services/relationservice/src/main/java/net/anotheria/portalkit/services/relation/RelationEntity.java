package net.anotheria.portalkit.services.relation;

import net.anotheria.portalkit.services.common.AccountId;
import org.apache.http.util.Args;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author bvanchuhov
 */
@Entity
@Table(name = "relation")
@IdClass(RelationId.class)
@NamedQueries({
        @NamedQuery(
                name = RelationEntity.JPQL_GET_BY_OWNER,
                query = "select r from RelationEntity r where r.ownerId = :ownerId"
        ),
        @NamedQuery(
                name = RelationEntity.JPQL_GET_BY_PARTNER,
                query = "select r from RelationEntity r where r.partnerId = :partnerId"
        ),
        @NamedQuery(
                name = RelationEntity.JPQL_GET_BY_OWNER_TYPE,
                query = "select r from RelationEntity r where r.ownerId = :ownerId and r.relationName = :relationName"
        ),
        @NamedQuery(
                name = RelationEntity.JPQL_GET_BY_PARTNER_TYPE,
                query = "select r from RelationEntity r where r.partnerId = :partnerId and r.relationName = :relationName"
        ),
        @NamedQuery(
                name = RelationEntity.JPQL_DELETE_BY_OWNER_PARTNER,
                query = "delete from RelationEntity r where r.ownerId = :ownerId and r.partnerId = :partnerId"
        ),
        @NamedQuery(
                name = RelationEntity.JPQL_DELETE_BY_OWNER,
                query = "delete from RelationEntity r where r.ownerId = :ownerId"
        ),
        @NamedQuery(
                name = RelationEntity.JPQL_DELETE_BY_PARTNER,
                query = "delete from RelationEntity r where r.partnerId = :partnerId"
        )
})
public class RelationEntity {

    public static final String JPQL_GET_BY_OWNER = "UserRelation.getByOwner";
    public static final String JPQL_GET_BY_PARTNER = "UserRelation.getByPartner";
    public static final String JPQL_GET_BY_OWNER_TYPE = "UserRelation.getByOwnerType";
    public static final String JPQL_GET_BY_PARTNER_TYPE = "UserRelation.getByPartnerType";

    public static final String JPQL_DELETE_BY_OWNER_PARTNER = "UserRelation.deleteByOwnerPartner";
    public static final String JPQL_DELETE_BY_OWNER = "UserRelation.deleteByOwner";
    public static final String JPQL_DELETE_BY_PARTNER = "UserRelation.deleteByPartner";

    @Id
    @Column(name = "owner", nullable = false, length = 128)
    private String ownerId;

    @Id
    @Column(name = "partner", nullable = false, length = 128)
    private String partnerId;

    @Id
    @Column(name = "type", nullable = false, length = 128)
    private String relationName;

    @Column(name = "dao_created")
    private long created;

    @Column(name = "dao_updated")
    private long updated;

    public RelationEntity() {
    }

    public RelationEntity(AccountId owner, AccountId partner, String relationName) {
        Args.notNull(owner, "owner id");
        Args.notNull(partner, "partner id");
        Args.notEmpty(relationName, "relation name");

        this.ownerId = owner.getInternalId();
        this.partnerId = partner.getInternalId();
        this.relationName = relationName;
    }

    public RelationEntity(Relation relation) {
        Args.notNull(relation, "user relation");

        this.ownerId = relation.getOwner().getInternalId();
        this.partnerId = relation.getPartner().getInternalId();
        this.relationName = relation.getRelationName();
        this.created = relation.getCreated();
        this.updated = relation.getUpdated();
    }

    public Relation toUserRelation() {
        return new Relation()
                .setOwner(new AccountId(ownerId))
                .setPartner(new AccountId(partnerId))
                .setRelationName(relationName)
                .setCreated(created)
                .setUpdated(updated);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public RelationEntity setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public RelationEntity setPartnerId(String partnerId) {
        this.partnerId = partnerId;
        return this;
    }

    public String getRelationName() {
        return relationName;
    }

    public RelationEntity setRelationName(String relationName) {
        this.relationName = relationName;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public RelationEntity setCreated(long created) {
        this.created = created;
        return this;
    }

    public long getUpdated() {
        return updated;
    }

    public RelationEntity setUpdated(long updated) {
        this.updated = updated;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationEntity that = (RelationEntity) o;
        return Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(partnerId, that.partnerId) &&
                Objects.equals(relationName, that.relationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, partnerId);
    }

    @Override
    public String toString() {
        return "RelationEntity{" +
                "ownerId='" + ownerId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", relationName='" + relationName + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
