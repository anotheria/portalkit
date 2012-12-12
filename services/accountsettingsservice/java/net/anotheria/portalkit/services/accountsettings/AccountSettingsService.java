package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 11.12.12 13:16
 */
public interface AccountSettingsService extends Service {
	Dataspace getDataspace(AccountId accountId, int dataspaceId)  throws AccountSettingsServiceException;

	void saveDataspace(Dataspace dataspace) throws AccountSettingsServiceException;
}
