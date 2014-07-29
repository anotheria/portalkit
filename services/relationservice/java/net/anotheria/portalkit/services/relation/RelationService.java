package net.anotheria.portalkit.services.relation;

import net.anotheria.anoprise.dualcrud.CrudService;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.storage.Relation;
import net.anotheria.portalkit.services.relation.storage.UserRelationData;

import java.util.List;

/**
 * Relation service interface.
 *
 * @author asamoilich
 */
public interface RelationService extends CrudService<UserRelationData> {
    /**
     * Add new relation or update old.
     *
     * @param ownerId   owner id
     * @param partnerId partner id
     * @param relation  {@link Relation}
     * @return {@link UserRelationData}
     * @throws RelationServiceException on errors
     */
    UserRelationData addRelation(AccountId ownerId, AccountId partnerId, Relation relation) throws RelationServiceException;

    /**
     * Return {@link UserRelationData} by owner and partner ids.
     *
     * @param ownerId   {@link AccountId}
     * @param partnerId {@link AccountId}
     * @return {@link UserRelationData}
     * @throws RelationServiceException on errors
     */
    UserRelationData getRelationData(AccountId ownerId, AccountId partnerId) throws RelationServiceException;

    /**
     * Delete from database relation.
     *
     * @param ownerId      {@link AccountId}
     * @param partnerId    {@link AccountId}
     * @param relationName relation name
     * @return {@link UserRelationData} without old relation
     * @throws RelationServiceException on errors
     */
    UserRelationData removeRelation(AccountId ownerId, AccountId partnerId, String relationName) throws RelationServiceException;

    /**
     * Return list of out relations.
     *
     * @param owner {@link AccountId}
     * @return {@link UserRelationData} list
     * @throws RelationServiceException on errors
     */
    List<UserRelationData>getOutRelationsData(AccountId owner) throws RelationServiceException;

    /**
     * Return list of in relations.
     *
     * @param owner {@link AccountId}
     * @return {@link UserRelationData} list
     * @throws RelationServiceException on errors
     */
    List<UserRelationData> getInRelationsData(AccountId owner) throws RelationServiceException;

    /**
     * Return count of relations with other users by relation name.
     *
     * @param ownerId      {@link AccountId}
     * @param relationName relation name
     * @return count
     * @throws RelationServiceException on errors
     */
    int getRelationCount(AccountId ownerId, String relationName) throws RelationServiceException;
}
