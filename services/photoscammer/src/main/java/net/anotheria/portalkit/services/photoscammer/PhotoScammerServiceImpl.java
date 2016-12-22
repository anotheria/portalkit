package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.portalkit.services.photoscammer.persistence.PhotoScammerPersistenceService;
import net.anotheria.portalkit.services.photoscammer.persistence.PhotoScammerPersistenceServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@Service
public class PhotoScammerServiceImpl implements PhotoScammerService {

    @Autowired
    private PhotoScammerPersistenceService photoScammerPersistenceService;


    @Override
    public PhotoDataBO getPhotoData(long id) throws PhotoScammerServiceException {
        return null;
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
    public void deletePhotoData(long id) throws PhotoScammerServiceException {

    }

    @Override
    public PhotoScammerBO getPhotoScammerData(long id) throws PhotoScammerServiceException {
        return null;
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
    public void deletePhotoScammerData(long id) throws PhotoScammerServiceException {

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
}
