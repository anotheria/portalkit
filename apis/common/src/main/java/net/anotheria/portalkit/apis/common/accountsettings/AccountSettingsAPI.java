package net.anotheria.portalkit.apis.common.accountsettings;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.accountsettings.DataspaceType;
import net.anotheria.portalkit.services.accountsettings.attribute.Attribute;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.Map;
import java.util.Optional;

/**
 * General realisation of AccountSettingsAPI to be used in all projects.
 */
public interface AccountSettingsAPI extends API {

    /**
     * Returns dataspace of user. Note, it is better not to retrieve the whole dataspace, but retrieve attributes instead.
     * Attribute access is protected by a lock against concurrent access from the same user.
     *
     * @param accountId     user id.
     * @param dataspaceType type of dataspace to return.
     * @return {@link Dataspace}
     * @throws APIException
     */
    Dataspace getDataspace(AccountId accountId, DataspaceType dataspaceType) throws APIException;

    /**
     * Save dataspace to database. Again, it is better to use attribute access methods instead of saving the whole dataspace.
     *
     * @param dataspace dataspace to save.
     * @throws APIException
     */
    void saveDataspace(Dataspace dataspace) throws APIException;

    /**
     * Deletes user dataspaces.
     *
     * @param accountId user id.
     * @throws APIException
     */
    void deleteDataspaces(AccountId accountId) throws APIException;


    void setAttribute(AccountId accountId, DataspaceType dataspaceType, Attribute... attribute) throws APIException;

    Optional<Attribute> getAttribute(AccountId accountId, DataspaceType dataspaceType, String attributeName) throws APIException;
}
