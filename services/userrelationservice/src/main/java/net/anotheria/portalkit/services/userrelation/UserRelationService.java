package net.anotheria.portalkit.services.userrelation;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationAlreadyExistsException;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationNotFoundException;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationServiceException;

import java.util.Set;

/**
 * @author bvanchuhov
 */
public interface UserRelationService extends Service {

    /**
     * @param owner
     * @param target
     * @param relationName
     * @throws UserRelationAlreadyExistsException
     * @throws UserRelationServiceException
     */
    void addRelation(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException;

    /**
     * @param userRelation
     * @throws UserRelationAlreadyExistsException
     * @throws UserRelationServiceException
     */
    void addRelation(UserRelation userRelation) throws UserRelationServiceException;


    /**
     * @param owner
     * @param target
     * @param relationName
     * @return
     * @throws UserRelationNotFoundException
     */
    UserRelation getRelation(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException;

    /**
     * @param owner
     * @param target
     * @param relationName
     * @return
     * @throws UserRelationServiceException
     */
    boolean isRelated(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException;


    /**
     * @param owner
     * @return
     * @throws UserRelationServiceException
     */
    Set<UserRelation> getOwnerRelations(AccountId owner) throws UserRelationServiceException;

    /**
     * @param owner
     * @param relationName
     * @return
     * @throws UserRelationServiceException
     */
    Set<UserRelation> getOwnerRelations(AccountId owner, String relationName) throws UserRelationServiceException;

    /**
     * @param target
     * @return
     * @throws UserRelationServiceException
     */
    Set<UserRelation> getTargetRelations(AccountId target) throws UserRelationServiceException;

    /**
     * @param target
     * @param relationName
     * @return
     * @throws UserRelationServiceException
     */
    Set<UserRelation> getTargetRelations(AccountId target, String relationName) throws UserRelationServiceException;


    /**
     * @param owner
     * @param target
     * @param relationName
     * @throws UserRelationServiceException
     */
    void deleteRelation(AccountId owner, AccountId target, String relationName) throws UserRelationServiceException;

    /**
     * @param owner
     * @param target
     * @throws UserRelationServiceException
     */
    void deleteRelations(AccountId owner, AccountId target) throws UserRelationServiceException;

    /**
     * @param owner
     * @throws UserRelationServiceException
     */
    void deleteOwnerRelations(AccountId owner) throws UserRelationServiceException;

    /**
     * @param target
     * @throws UserRelationServiceException
     */
    void deleteTargetRelations(AccountId target) throws UserRelationServiceException;
}
