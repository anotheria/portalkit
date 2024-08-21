package net.anotheria.portalkit.services.common;

import net.anotheria.portalkit.services.common.integrity.IntegrityCheckHelper;
import net.anotheria.portalkit.services.common.integrity.IntegrityCheckResult;

/**
 * This interface declares a services that manages user data. It allows us to define operations that can be used on any service that manages user data.
 *
 * @author lrosenberg
 * @since 21.09.14 13:12
 */
public interface UserDataManagingService {
    /**
     * Deletes all data for the given accountId.
     * @param accountId account ID to delete from
     */
    void deleteUserData(AccountId accountId);

    /**
     * Returns type of data managed by this service. Required for logging/debugging purposes.
     * @return type of data managed by this service
     */
    String describeData();

    /**
     * Perform integrity check for given service.
     *
     * @param helper {@link IntegrityCheckHelper} instance
     * @return  {@link IntegrityCheckResult} result of integrity check
     * @throws Exception if any errors occurs
     */
    IntegrityCheckResult performIntegrityCheck(IntegrityCheckHelper helper) throws Exception;
}
