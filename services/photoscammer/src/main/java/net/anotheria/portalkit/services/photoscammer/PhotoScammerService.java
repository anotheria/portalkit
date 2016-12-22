package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.photoscammer.persistence.PhotoScammer;

import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
public interface PhotoScammerService extends Service {

    /**
     * Gets {@link PhotoDataBO} by id.
     *
     * @return {@link PhotoDataBO}
     * @throws PhotoScammerServiceException if error.
     * */
    PhotoDataBO getPhotoData(long id) throws PhotoScammerServiceException;

    /**
     * Saves {@link PhotoDataBO}.
     *
     * @throws PhotoScammerServiceException if error.
     * */
    void savePhotoData(PhotoDataBO photoData) throws PhotoScammerServiceException;

    /**
     * Gets all {@link PhotoDataBO}.
     *
     * @return list of {@link PhotoDataBO}.
     * @throws PhotoScammerServiceException if error.
     * */
    List<PhotoDataBO> getAllPhotoData() throws PhotoScammerServiceException;

    /**
     * Gets all {@link PhotoDataBO} by user.
     *
     * @return list of {@link PhotoDataBO}.
     * @throws PhotoScammerServiceException if error.
     * */
    List<PhotoDataBO> getAllPhotoDataByUser(String userId) throws PhotoScammerServiceException;

    /**
     * Gets all {@link PhotoDataBO}.
     *
     * @return list of {@link PhotoDataBO}.
     * @throws PhotoScammerServiceException if error.
     * */
    void deletePhotoData(long id) throws PhotoScammerServiceException;


    /**
     * Gets {@link PhotoScammerBO} by id.
     *
     * @return {@link PhotoScammerBO}
     * @throws PhotoScammerServiceException if error.
     * */
    PhotoScammerBO getPhotoScammerData(long id) throws PhotoScammerServiceException;

    /**
     * Saves {@link PhotoScammerBO}.
     *
     * @throws PhotoScammerServiceException if error.
     * */
    void savePhotoScammerData(PhotoScammerBO photoScammer) throws PhotoScammerServiceException;

    /**
     * Gets all {@link PhotoScammerBO}.
     *
     * @return list of {@link PhotoScammerBO}.
     * @throws PhotoScammerServiceException if error.
     * */
    List<PhotoScammerBO> getAllPhotoScammerData() throws PhotoScammerServiceException;

    /**
     * Gets all {@link PhotoScammer}.
     *
     * @return list of {@link PhotoScammer}.
     * @throws PhotoScammerServiceException if error.
     * */
    void deletePhotoScammerData(long id) throws PhotoScammerServiceException;

    /**
     * Move all user photo data to scammer db.
     *
     * @return list of {@link PhotoScammer}.
     * @throws PhotoScammerServiceException if error.
     * */
    void addAllPhotoScammer(String userId) throws PhotoScammerServiceException;
}
