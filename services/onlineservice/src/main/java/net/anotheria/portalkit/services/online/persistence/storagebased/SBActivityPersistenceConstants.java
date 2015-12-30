package net.anotheria.portalkit.services.online.persistence.storagebased;

/**
 * Config for Storage based implementation.
 *
 * @author h3llka
 */
public final class SBActivityPersistenceConstants {
    /**
     * Constant which should be used for configuring and resolving {@link net.anotheria.moskito.core.util.storage.Storage}, as underlying persistence service.
     */
    public static final String ACTIVITY_PERSISTENCE_GENERIC_STORAGE_NAME = "ActivityPersistence";
    /**
     * Primary key in context of {@link net.anotheria.portalkit.services.online.persistence.storagebased.SBActivityPersistenceServiceImpl.AccountActivityVO}.
     */
    public static final String ACTIVITY_PERSISTENCE_GENERIC_STORAGE_KEY_FIELD_NAME = "accountId";

    private SBActivityPersistenceConstants() {
        throw new IllegalAccessError("Can't be instantiated");
    }
}
