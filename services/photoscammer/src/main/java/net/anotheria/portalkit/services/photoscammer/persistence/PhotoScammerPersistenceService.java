package net.anotheria.portalkit.services.photoscammer.persistence;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.photoscammer.PhotoDataBO;
import net.anotheria.portalkit.services.photoscammer.PhotoScammerBO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@Monitor
@Service
public interface PhotoScammerPersistenceService {

    /**
     * Gets {@link PhotoDataBO} by id.
     *
     * @return {@link PhotoDataBO}
     * @throws PhotoScammerPersistenceServiceException if error.
     * */
    PhotoDataBO getPhotoData(long photoId) throws PhotoScammerPersistenceServiceException;

    /**
     * Saves {@link PhotoDataBO}.
     *
     * @throws PhotoScammerPersistenceServiceException if error.
     * */
    void savePhotoData(PhotoDataBO photoData) throws PhotoScammerPersistenceServiceException;

    /**
     * Gets all {@link PhotoDataBO}.
     *
     * @return list of {@link PhotoDataBO}.
     * @throws PhotoScammerPersistenceServiceException if error.
     * */
    List<PhotoDataBO> getAllPhotoData() throws PhotoScammerPersistenceServiceException;

    /**
     * Gets all {@link PhotoDataBO} by user.
     *
     * @return list of {@link PhotoDataBO}.
     * @throws PhotoScammerPersistenceServiceException if error.
     * */
    List<PhotoDataBO> getAllPhotoDataByUser(String userId) throws PhotoScammerPersistenceServiceException;

    /**
     * Gets {@link PhotoScammerBO} by id.
     *
     * @return {@link PhotoScammerBO}
     * @throws PhotoScammerPersistenceServiceException if error.
     * */
    PhotoScammerBO getPhotoScammerData(long id) throws PhotoScammerPersistenceServiceException;

    /**
     * Saves {@link PhotoScammerBO}.
     *
     * @throws PhotoScammerPersistenceServiceException if error.
     * */
    void savePhotoScammerData(PhotoScammerBO photoScammer) throws PhotoScammerPersistenceServiceException;

    /**
     * Gets all {@link PhotoScammerBO}.
     *
     * @return list of {@link PhotoScammerBO}.
     * @throws PhotoScammerPersistenceServiceException if error.
     * */
    List<PhotoScammerBO> getAllPhotoScammerData() throws PhotoScammerPersistenceServiceException;

    /**
     * Get {@link PhotoScammerBO} by perseptiveHash.
     *
     * @param perseptiveHash    photo perseptiveHash
     * @return                  {@code true} if this id photo is scammer photo, {@code false} - otherwise
     * @throws PhotoScammerPersistenceServiceException  if error
     */
    boolean isPhotoScammerExistsForPerseptiveHash(String perseptiveHash) throws PhotoScammerPersistenceServiceException;
}
