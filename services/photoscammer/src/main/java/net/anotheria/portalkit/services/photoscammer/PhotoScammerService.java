package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.photoscammer.persistence.PhotoScammer;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@DistributeMe()
@FailBy(strategyClass=RetryCallOnce.class)
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
	 * @param id scammer data id.
     * @throws PhotoScammerServiceException if error.
     * */
    void deletePhotoData(long id) throws PhotoScammerServiceException;


    /**
     * Gets {@link PhotoScammerBO} by id.
     *
	 * @param id scammer data id.
     * @return {@link PhotoScammerBO}
     * @throws PhotoScammerServiceException if error.
     * */
    PhotoScammerBO getPhotoScammerData(long id) throws PhotoScammerServiceException;

    /**
     * Saves {@link PhotoScammerBO}.
     *
	 * @param photoScammer photo scammer data.
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
     * DeletePhotoScammerData by id.
     *
	 * @param id scammer data id.
     * @throws PhotoScammerServiceException if error.
     * */
    void deletePhotoScammerData(long id) throws PhotoScammerServiceException;

    /**
     * Move all user photo data to scammer db.
     *
     * @throws PhotoScammerServiceException if error.
     * */
    void addAllPhotoScammer(String userId) throws PhotoScammerServiceException;
}
