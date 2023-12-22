package net.anotheria.portalkit.services.bounce.persistence;

import net.anotheria.moskito.aop.annotation.Monitor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@Monitor
@Service
public interface BouncePersistenceService {

    /**
     * Save bounce.
     *
     * @throws BouncePersistenceServiceException    if error.
     * */
    void saveBounce(BounceDO bounce) throws BouncePersistenceServiceException;

    /**
     * Removes bounce.
     *
     * @param email     user email.
     * @throws BouncePersistenceServiceException    if error.
     * */
    void deleteBounce(String email) throws BouncePersistenceServiceException;

    /**
     * Get bounce by email.
     *
     * @param email     user email.
     * @throws BouncePersistenceServiceException    if error.
     * @return {@link BounceDO}.
     * */
    BounceDO getBounce(String email) throws BouncePersistenceServiceException;

    /**
     * Get all bounces.
     *
     * @throws BouncePersistenceServiceException    if error.
     * @return list of {@link BounceDO}.
     * */
    List<BounceDO> getBounces() throws BouncePersistenceServiceException;

    long getBouncesCount() throws BouncePersistenceServiceException;
}
