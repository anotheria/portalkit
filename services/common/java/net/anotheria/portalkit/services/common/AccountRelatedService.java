package net.anotheria.portalkit.services.common;

/**
 * This interface is implemented by all services that are handling user data. It is used to fulfill the user deletion.
 *
 * @author lrosenberg
 * @since 29.12.12 00:00
 */
public interface AccountRelatedService {
	boolean deleteUserRelatedData(AccountId Id);
}
