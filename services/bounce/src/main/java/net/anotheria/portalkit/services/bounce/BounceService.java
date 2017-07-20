package net.anotheria.portalkit.services.bounce;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;
import java.util.Map;

/**
 * @author Vlad Lukjanenko
 */
@DistributeMe(
		initcode = {
				"net.anotheria.portalkit.services.bounce.BounceServiceSpringConfigurator.configure();"
		}
)
@FailBy(strategyClass=RetryCallOnce.class)
public interface BounceService extends Service {

    /**
     * Save bounce.
     *
     * @throws BounceServiceException    if error.
     * */
    void saveBounce(BounceBO bounce) throws BounceServiceException;

    /**
     * Removes bounce.
     *
     * @param email     user email.
     * @throws BounceServiceException    if error.
     * */
    void deleteBounce(String email) throws BounceServiceException;

    /**
     * Get bounce by email.
     *
     * @param email     user email.
     * @throws BounceServiceException    if error.
     * @return {@link BounceBO}.
     * */
    BounceBO getBounce(String email) throws BounceServiceException;

    /**
     * Get all bounces.
     *
     * @throws BounceServiceException    if error.
     * @return list of {@link BounceBO}.
     * */
    List<BounceBO> getBounces() throws BounceServiceException;

    /**
     * Get all bounces as {@link Map}.
     *
     * @throws BounceServiceException    if error.
     * @return map of {@link BounceBO}.
     * */
    Map<String, BounceBO> getBouncesMap() throws BounceServiceException;
}
