package net.anotheria.portalkit.services.photoscammer.persistence;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.photoscammer.PhotoDataBO;
import net.anotheria.portalkit.services.photoscammer.PhotoScammerBO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@Service
@Transactional
@Monitor(subsystem = "photo-scammer")
public class PhotoScammerPersistenceServiceImpl implements PhotoScammerPersistenceService {

    @PersistenceContext
    private EntityManager entityManager;


    public PhotoScammerPersistenceServiceImpl() {

    }


    @Override
    public PhotoDataBO getPhotoData(long photoId) throws PhotoScammerPersistenceServiceException {

        TypedQuery<PhotoData> query = entityManager.createNamedQuery(PhotoData.JPQL_GET_ALL_PHOTO_DATA_BY_PHOTO_ID, PhotoData.class)
                .setParameter("photoId", photoId);
        List<PhotoData> photoData = query.getResultList();

        if (photoData == null || photoData.isEmpty()) {
            return null;
        }

        return new PhotoDataBO(photoData.get(0));
    }

    @Override
    public void savePhotoData(PhotoDataBO photoData) throws PhotoScammerPersistenceServiceException {

        if (photoData.getId() != 0) {
            entityManager.merge(new PhotoData(photoData));
        } else {
            entityManager.persist(new PhotoData(photoData));
        }
    }

    @Override
    public List<PhotoDataBO> getAllPhotoData() throws PhotoScammerPersistenceServiceException {

        TypedQuery<PhotoData> query = entityManager.createNamedQuery(PhotoData.JPQL_GET_ALL_PHOTO_DATA, PhotoData.class);
        List<PhotoDataBO> result = new ArrayList<>();
        List<PhotoData> photoData = query.getResultList();

        for (PhotoData data : photoData) {
            result.add(new PhotoDataBO(data));
        }

        return result;
    }

    @Override
    public long getAllPhotoDataCount() throws PhotoScammerPersistenceServiceException {
        TypedQuery<Long> query = entityManager.createQuery("select count(a.id) from PhotoData a", Long.class);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public List<PhotoDataBO> getAllPhotoDataByUser(String userId) throws PhotoScammerPersistenceServiceException {

        TypedQuery<PhotoData> query = entityManager.createNamedQuery(PhotoData.JPQL_GET_ALL_PHOTO_DATA_BY_USER, PhotoData.class)
                .setParameter("userId", userId);
        List<PhotoDataBO> result = new ArrayList<>();
        List<PhotoData> photoData = query.getResultList();

        for (PhotoData data : photoData) {
            result.add(new PhotoDataBO(data));
        }

        return result;
    }

    @Override
    public PhotoScammerBO getPhotoScammerData(long id) throws PhotoScammerPersistenceServiceException {
        PhotoScammer exist = entityManager.find(PhotoScammer.class, id);
        return exist == null ? null : new PhotoScammerBO(exist);
    }

    @Override
    public void savePhotoScammerData(PhotoScammerBO photoScammer) throws PhotoScammerPersistenceServiceException {

        if (photoScammer.getId() != 0) {
            entityManager.merge(new PhotoScammer(photoScammer));
        } else {
            entityManager.persist(new PhotoScammer(photoScammer));
        }
    }

    @Override
    public List<PhotoScammerBO> getAllPhotoScammerData() throws PhotoScammerPersistenceServiceException {

        TypedQuery<PhotoScammer> query = entityManager.createNamedQuery(PhotoScammer.JPQL_GET_ALL_PHOTO_SCAMMER_DATA, PhotoScammer.class);
        List<PhotoScammerBO> result = new ArrayList<>();
        List<PhotoScammer> photoScammers = query.getResultList();

        for (PhotoScammer data : photoScammers) {
            result.add(new PhotoScammerBO(data));
        }

        return result;
    }

    @Override
    public long getAllPhotoScammerDataCount() throws PhotoScammerPersistenceServiceException {
        TypedQuery<Long> query = entityManager.createQuery("select count(a.id) from PhotoScammer a", Long.class);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public boolean isPhotoScammerExistsForPerseptiveHash(String perseptiveHash) throws PhotoScammerPersistenceServiceException {
        String queryStr = "select p from PhotoScammer p where p.perseptiveHash = :perseptiveHash";
        TypedQuery<PhotoScammer> query = entityManager.createQuery(queryStr, PhotoScammer.class)
                .setParameter("perseptiveHash", perseptiveHash);

        List<PhotoScammer> ret = query.getResultList();
        if (ret == null)
            throw new PhotoScammerPersistenceServiceException("Unable to get PhotoScammer by perseptiveHash: " + perseptiveHash);

        return !ret.isEmpty();
    }
}
