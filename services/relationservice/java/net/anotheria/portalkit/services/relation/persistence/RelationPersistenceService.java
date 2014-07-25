package net.anotheria.portalkit.services.relation.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.storage.Relation;
import net.anotheria.portalkit.services.relation.storage.UserRelationData;

import java.util.List;

/**
 * Relation persistence service.
 *
 * @author asamoilich
 */
public interface RelationPersistenceService extends Service {
    /**
     * Add new relation or update old.
     *
     * @param owner     {@link AccountId}
     * @param partnerId {@link AccountId}
     * @param relation  {@link Relation}
     * @return created relation
     * @throws RelationPersistenceServiceException on errors
     */
    Relation addRelation(AccountId owner, AccountId partnerId, Relation relation) throws RelationPersistenceServiceException;

    /**
     * Return list of relations.
     *
     * @param owner   {@link AccountId}
     * @param partner {@link AccountId}
     * @return relations list
     * @throws RelationPersistenceServiceException on errors
     */
    List<Relation> getRelations(AccountId owner, AccountId partner) throws RelationPersistenceServiceException;

    /**
     * Remove relation from database.
     *
     * @param owner        {@link AccountId}
     * @param partner      {@link AccountId}
     * @param relationName relation name
     * @throws RelationPersistenceServiceException on errors
     */
    void removeRelation(AccountId owner, AccountId partner, String relationName) throws RelationPersistenceServiceException;

    /**
     * Return list of out relations.
     *
     * @param ownerId {@link AccountId}
     * @return out relations
     * @throws RelationPersistenceServiceException on errors
     */
    List<UserRelationData> getOutRelations(AccountId ownerId) throws RelationPersistenceServiceException;

    /**
     * Return list of in relations.
     *
     * @param ownerId {@link AccountId}
     * @return in relations
     * @throws RelationPersistenceServiceException on errors
     */
    List<UserRelationData> getInRelations(AccountId ownerId) throws RelationPersistenceServiceException;
}
