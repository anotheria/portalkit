package net.anotheria.portalkit.services.personaldata;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

/**
 * @author Vlad Lukjanenko
 */
@DistributeMe()
@FailBy(strategyClass=RetryCallOnce.class)
public interface PersonalDataService extends Service {

    /**
     * Returns user personal data.
     *
     * @param accountId user account id.
     * @return {@link PersonalData}.
     * @throws PersonalDataServiceException id error occurs.
     * */
    PersonalData get(AccountId accountId) throws PersonalDataServiceException;

    /**
     * Saves user personal data.
     *
     * @param personalData  user personal data.
     * @throws PersonalDataServiceException id error occurs.
     * */
    void save(PersonalData personalData) throws PersonalDataServiceException;

    /**
     * Removes user personal data.
     *
     * @param accountId user account id.
     * @throws PersonalDataServiceException id error occurs.
     * */
    void delete(AccountId accountId) throws PersonalDataServiceException;
}
