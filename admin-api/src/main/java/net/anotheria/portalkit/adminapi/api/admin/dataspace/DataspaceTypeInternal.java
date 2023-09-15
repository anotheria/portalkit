package net.anotheria.portalkit.adminapi.api.admin.dataspace;

import net.anotheria.portalkit.services.accountsettings.DataspaceType;

/**
 * Only for internal usage.
 * Used to delete dataspace, because AccountSettingsService.deleteDataspace accepts accountId and object of DataspaceType interface.
 */
public class DataspaceTypeInternal implements DataspaceType {

    private final int id;

    public DataspaceTypeInternal(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String toString() {
        return "DataspaceTypeInternal{" +
                "id=" + id +
                '}';
    }
}
