package net.anotheria.portalkit.services.bounce.persistence;

import net.anotheria.moskito.aop.annotation.Monitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@Service
@Transactional
@Monitor(subsystem = "bounce", category = "portalkit-persistence-service")
public class BouncePersistenceServiceImpl implements BouncePersistenceService {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void saveBounce(BounceDO bounce) throws BouncePersistenceServiceException {
        entityManager.persist(bounce);
    }

    @Override
    public void deleteBounce(String email) throws BouncePersistenceServiceException {

        Query query = entityManager.createNamedQuery(BounceDO.JPQL_DELETE_BOUNCE_BY_ID)
                .setParameter("email", email);

        query.executeUpdate();
    }

    @Override
    public BounceDO getBounce(String email) throws BouncePersistenceServiceException {

        List<BounceDO> bounces = null;
        TypedQuery<BounceDO> query = entityManager.createNamedQuery(BounceDO.JPQL_GET_BOUNCE_BY_ID, BounceDO.class)
                .setParameter("email", email);

        bounces = query.getResultList();

        if (bounces == null || bounces.isEmpty()) {
            throw new BouncePersistenceServiceException("Unable to get bounce");
        }

        return bounces.get(0);
    }

    @Override
    public List<BounceDO> getBounces() throws BouncePersistenceServiceException {

        List<BounceDO> bounces = new ArrayList<>();
        TypedQuery<BounceDO> query = entityManager.createNamedQuery(BounceDO.JPQL_GET_ALL_BOUNCES, BounceDO.class);

        bounces = query.getResultList();

        if (bounces == null) {
            throw new BouncePersistenceServiceException("Unable to get bounce");
        }

        return bounces;
    }
}
