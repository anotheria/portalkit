package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.photoscammer.persistence.PhotoScammerPersistenceService;
import net.anotheria.portalkit.services.photoscammer.persistence.PhotoScammerPersistenceServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@Service
public class PhotoScammerServiceImpl implements PhotoScammerService, EntityManagingService {

    @Autowired
    private PhotoScammerPersistenceService photoScammerPersistenceService;

    public PhotoScammerServiceImpl() {
        EntityManagingServices.createEntityCounter(this, "PhotoScammers", "PhotoDatas");
    }

    @Override
    public int getEntityCount(String s) {
        switch (s){
            case"PhotoScammers":
                try {
                    return Long.valueOf(photoScammerPersistenceService.getAllPhotoScammerDataCount()).intValue();
                } catch (PhotoScammerPersistenceServiceException e) {
                    return 0;
                }
            case "PhotoDatas":
                try {
                    return Long.valueOf(photoScammerPersistenceService.getAllPhotoDataCount()).intValue();
                } catch (PhotoScammerPersistenceServiceException e) {
                    return 0;
                }
        }
        return 0;
    }

    @Override
    public PhotoDataBO getPhotoData(long photoId) throws PhotoScammerServiceException {
        try {
            return photoScammerPersistenceService.getPhotoData(photoId);
        } catch (PhotoScammerPersistenceServiceException e) {
            throw new PhotoScammerServiceException("Error occurred while getting photo data by id=" + photoId, e);
        }
    }

    @Override
    public void savePhotoData(PhotoDataBO photoData) throws PhotoScammerServiceException {
        try {
            photoScammerPersistenceService.savePhotoData(photoData);
        } catch (PhotoScammerPersistenceServiceException e) {
            throw new PhotoScammerServiceException("Error occurred while saving photo data", e);
        }
    }

    @Override
    public List<PhotoDataBO> getAllPhotoData() throws PhotoScammerServiceException {
        try {
            return photoScammerPersistenceService.getAllPhotoData();
        } catch (PhotoScammerPersistenceServiceException e) {
            throw new PhotoScammerServiceException("Error occurred while getting all photo data", e);
        }
    }

    @Override
    public List<PhotoDataBO> getAllPhotoDataByUser(String userId) throws PhotoScammerServiceException {
        try {
            return photoScammerPersistenceService.getAllPhotoDataByUser(userId);
        } catch (PhotoScammerPersistenceServiceException e) {
            throw new PhotoScammerServiceException("Error occurred while getting all photo data by userid" + userId, e);
        }
    }

    @Override
    public PhotoScammerBO getPhotoScammerData(long id) throws PhotoScammerServiceException {
        try {
            return photoScammerPersistenceService.getPhotoScammerData(id);
        } catch (PhotoScammerPersistenceServiceException e) {
            throw new PhotoScammerServiceException("Unable to get photo scammer by id: " + id, e);
        }
    }

    @Override
    public void savePhotoScammerData(PhotoScammerBO photoScammer) throws PhotoScammerServiceException {
        try {
            photoScammerPersistenceService.savePhotoScammerData(photoScammer);
        } catch (PhotoScammerPersistenceServiceException e) {
            throw new PhotoScammerServiceException("Error occurred while saving photo scammer data", e);
        }
    }

    @Override
    public List<PhotoScammerBO> getAllPhotoScammerData() throws PhotoScammerServiceException {
        try {
            return photoScammerPersistenceService.getAllPhotoScammerData();
        } catch (PhotoScammerPersistenceServiceException e) {
            throw new PhotoScammerServiceException("Error occurred while getting photo scammer data", e);
        }
    }

    @Override
    public void addAllPhotoScammer(String userId) throws PhotoScammerServiceException {

        List<PhotoDataBO> photoData = getAllPhotoDataByUser(userId);

        for (PhotoDataBO data : photoData) {

            PhotoScammerBO photoScammer = new PhotoScammerBO();

            photoScammer.setUserId(userId);
            photoScammer.setPerseptiveHash(data.getPerseptiveHash());
            photoScammer.setPhotoHash(data.getPhotoHash());
            photoScammer.setCreated(System.currentTimeMillis());

            try {
                savePhotoScammerData(photoScammer);
            } catch(PhotoScammerServiceException ea) {
                continue;
            }
        }
    }

    @Override
    public boolean isScammerPhoto(long photoId) throws PhotoScammerServiceException {
        PhotoDataBO photoDataBO = getPhotoData(photoId);
        try {
            if(photoDataBO != null) {
                return photoScammerPersistenceService.isPhotoScammerExistsForPerseptiveHash(photoDataBO.getPerseptiveHash());
            }
            return false;
        } catch (PhotoScammerPersistenceServiceException e) {
            throw new PhotoScammerServiceException("Unable to check photo scammer perseptive cash for photo " + photoId, e);
        }
    }
}
