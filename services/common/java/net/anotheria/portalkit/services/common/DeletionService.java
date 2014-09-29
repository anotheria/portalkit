package net.anotheria.portalkit.services.common;

/**
 * This interface declares a services that manages user data. It allows us to define operations that can be used on any service that manages user data.
 *
 * @author asamoilich
 */
public interface DeletionService {
    /**
     * Delete all data for provided accountId.
     *
     * @param accountId {@link AccountId}
     */
    void deleteData(AccountId accountId);
}
