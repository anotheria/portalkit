package net.anotheria.portalkit.services.profileservice;

import net.anotheria.portalkit.services.profileservice.index.Index;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.configureme.sources.ConfigurationSourceKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author asamoilich.
 */
@ConfigureMe(name = "pk-profile-mongo-service-config", allfields = false)
public final class ProfileServiceConfig implements Serializable {

    /**
     * Generated SerialVersionUID.
     */
    @DontConfigure
    private static final long serialVersionUID = -1137186520917278525L;

    /**
     * {@link Logger} instance.
     */
    @DontConfigure
    private static final Logger LOGGER = Logger.getLogger(ProfileServiceConfig.class);

    /**
     * Synchronization object.
     */
    @DontConfigure
    private static final Object LOCK = new Object();

    /**
     * {@link ProfileServiceConfig} instance.
     */
    @DontConfigure
    private static volatile ProfileServiceConfig instance;

    /**
     * Database name.
     */
    @Configure
    private String databaseName;

    /**
     * Collection name.
     */
    @Configure
    private String collectionName;

    /**
     * Initialize indexes during service initialization.
     */
    @Configure
    private boolean initializeIndexes = false;

    /**
     * Collection indexes.
     */
    @Configure
    private Index[] indexes;

    /**
     * Default constructor.
     *
     * @param configuration configuration name
     */
    private ProfileServiceConfig(final String configuration) {
        try {
            if (configuration == null || configuration.trim().isEmpty()) {
                ConfigurationManager.INSTANCE.configure(this);
            } else {
                ConfigurationManager.INSTANCE.configureAs(this, ConfigurationManager.INSTANCE.getDefaultEnvironment(), configuration, ConfigurationSourceKey.Format.JSON);
            }
        } catch (RuntimeException e) {
            LOGGER.warn("ProfileServiceConfig(conf:" + configuration + ") Configuration fail[" + e.getMessage()
                    + "]. Relaying on defaults.");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(e);

        }

        LOGGER.info("ProfileServiceConfig(conf:" + configuration + ") Configured with[" + this.toString() + "]");
    }

    /**
     * Get configured instance of {@link ProfileServiceConfig}.
     *
     * @param configuration configuration name, can be <code>null</code> or empty
     * @return {@link ProfileServiceConfig}
     */
    public static ProfileServiceConfig getInstance(final String configuration) {
        if (instance != null)
            return instance;

        synchronized (LOCK) {
            if (instance == null)
                instance = new ProfileServiceConfig(configuration);
        }
        return instance;
    }

    public String getDatabaseName() {
        return databaseName != null ? databaseName : "";
    }

    public void setDatabaseName(final String aDatabaseName) {
        this.databaseName = aDatabaseName;
    }

    public String getCollectionName() {
        return collectionName != null ? collectionName : "";
    }

    public void setCollectionName(final String aCollectionName) {
        this.collectionName = aCollectionName;
    }

    public boolean isInitializeIndexes() {
        return initializeIndexes;
    }

    public void setInitializeIndexes(final boolean aInitializeIndexes) {
        this.initializeIndexes = aInitializeIndexes;
    }

    /**
     * Get indexes configuration.
     *
     * @return {@link java.util.List} of {@link Index}
     */
    public List<Index> getIndexes() {
        return indexes != null ? Arrays.asList(indexes) : new ArrayList<Index>();
    }

    /**
     * Set indexes configuration.
     *
     * @param aIndexes indexes configuration
     */
    public void setHosts(final List<Index> aIndexes) {
        this.indexes = aIndexes != null ? aIndexes.toArray(new Index[aIndexes.size()]) : null;
    }

    public void setIndexes(final Index[] aIndexes) {
        this.indexes = aIndexes != null ? aIndexes.clone() : null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((collectionName == null) ? 0 : collectionName.hashCode());
        result = prime * result + ((databaseName == null) ? 0 : databaseName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProfileServiceConfig other = (ProfileServiceConfig) obj;
        if (collectionName == null) {
            if (other.collectionName != null)
                return false;
        } else if (!collectionName.equals(other.collectionName))
            return false;
        if (databaseName == null) {
            if (other.databaseName != null)
                return false;
        } else if (!databaseName.equals(other.databaseName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n\t" + this.getClass().getSimpleName() + "[\n\t\t");
        builder.append("databaseName=").append(databaseName).append(",\n\t\t");
        builder.append("collectionName=").append(collectionName).append(",\n\t\t");
        builder.append("initializeIndexes=").append(initializeIndexes).append(",\n\t\t");
        builder.append("indexes=[");
        if (indexes != null)
            for (Index index : indexes)
                builder.append("\n\t\t\t").append(index);
        builder.append("\n\t\t],\n\t");
        builder.append("]\n");
        return builder.toString();
    }

}