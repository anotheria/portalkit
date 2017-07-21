package net.anotheria.portalkit.services.relation;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.exception.RelationAlreadyExistsException;
import net.anotheria.portalkit.services.relation.exception.RelationNotFoundException;
import net.anotheria.portalkit.services.relation.exception.RelationServiceException;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.Set;

/**
 * @author bvanchuhov
 */
@DistributeMe(
        initcode = {
                "net.anotheria.portalkit.services.relation.RelationServiceSpringConfigurator.configure();"
        }
)
@FailBy(strategyClass=RetryCallOnce.class)
public interface RelationService extends Service {

    /**
     * @param owner
     * @param partner
     * @param relationName
     * @throws RelationAlreadyExistsException
     * @throws RelationServiceException
     */
    void addRelation(AccountId owner, AccountId partner, String relationName) throws RelationServiceException;

    /**
     * @param relation
     * @throws RelationAlreadyExistsException
     * @throws RelationServiceException
     */
    void addRelation(Relation relation) throws RelationServiceException;


    /**
     * @param owner
     * @param partner
     * @param relationName
     * @return
     * @throws RelationNotFoundException
     */
    Relation getRelation(AccountId owner, AccountId partner, String relationName) throws RelationServiceException;

    /**
     * @param owner
     * @param partner
     * @param relationName
     * @return
     * @throws RelationServiceException
     */
    boolean isRelated(AccountId owner, AccountId partner, String relationName) throws RelationServiceException;


    /**
     * @param owner
     * @return
     * @throws RelationServiceException
     */
    Set<Relation> getOwnerRelations(AccountId owner) throws RelationServiceException;

    /**
     * @param owner
     * @param relationName
     * @return
     * @throws RelationServiceException
     */
    Set<Relation> getOwnerRelations(AccountId owner, String relationName) throws RelationServiceException;

    /**
     * @param partner
     * @return
     * @throws RelationServiceException
     */
    Set<Relation> getPartnerRelations(AccountId partner) throws RelationServiceException;

    /**
     * @param partner
     * @param relationName
     * @return
     * @throws RelationServiceException
     */
    Set<Relation> getPartnerRelations(AccountId partner, String relationName) throws RelationServiceException;


    /**
     * @param owner
     * @param partner
     * @param relationName
     * @throws RelationServiceException
     */
    void deleteRelation(AccountId owner, AccountId partner, String relationName) throws RelationServiceException;

    /**
     * @param owner
     * @param partner
     * @throws RelationServiceException
     */
    void deleteRelations(AccountId owner, AccountId partner) throws RelationServiceException;

    /**
     * @param owner
     * @throws RelationServiceException
     */
    void deleteOwnerRelations(AccountId owner) throws RelationServiceException;

    /**
     * @param partner
     * @throws RelationServiceException
     */
    void deletePartnerRelations(AccountId partner) throws RelationServiceException;
}
