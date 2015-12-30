package net.anotheria.portalkit.services.profileservice;

import net.anotheria.portalkit.services.profileservice.index.Index;
import org.configureme.ConfigurationManager;
import org.configureme.Environment;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.configureme.environments.DynamicEnvironment;
import org.configureme.sources.ConfigurationSourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceConfig.class);

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
     * Array with {@link Host} configurations.
     */
    @Configure
    private Host[] hosts;

    /**
     * Default constructor.
     *
     * @param configuration configuration name
     * @param environment   configuration environment
     */
    private ProfileServiceConfig(final String configuration, final Environment environment) {
        try {
            if (configuration == null || configuration.trim().isEmpty()) {
                ConfigurationManager.INSTANCE.configure(this, environment);
            } else {
                ConfigurationManager.INSTANCE.configureAs(this, environment, configuration, ConfigurationSourceKey.Format.JSON);
            }
        } catch (RuntimeException e) {
            LOGGER.warn("MongoClientConfig(conf:" + configuration + ", env: " + environment + ") Configuration fail[" + e.getMessage()
                    + "]. Relaying on defaults.");

//            this.hosts = new Host[]{new Host(ServerAddress.defaultHost(), ServerAddress.defaultPort())};
        }

        LOGGER.info("MongoClientConfig(conf:" + configuration + ", env: " + environment + ") Configured with[" + this.toString() + "]");
    }

    /**
     * Get configured instance of {@link ProfileServiceConfig}.
     *
     * @param configuration configuration name, can be <code>null</code> or empty
     * @param environment   environment name, can be <code>null</code> or empty
     * @return {@link ProfileServiceConfig}
     */
    public static ProfileServiceConfig getInstance(final String configuration, final String environment) {
        if ((configuration == null || configuration.trim().isEmpty()) && (environment == null || environment.trim().isEmpty())) {
            if (instance != null)
                return instance;

            synchronized (LOCK) {
                if (instance == null)
                    instance = new ProfileServiceConfig(null, ConfigurationManager.INSTANCE.getDefaultEnvironment());
            }

            return instance;
        }

        Environment env = ConfigurationManager.INSTANCE.getDefaultEnvironment();
        if (environment != null && !environment.trim().isEmpty())
            env = DynamicEnvironment.parse(environment);

        return new ProfileServiceConfig(configuration, env);
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

    public Host[] getHosts() {
        return hosts;
    }

    public void setHosts(Host[] hosts) {
        this.hosts = hosts;
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
    /**
     * Host configuration.
     *
     * @author Alexandr Bolbat
     */
    public static class Host implements Serializable {

        /**
         * Generated SerialVersionUID.
         */
        @DontConfigure
        private static final long serialVersionUID = 8291540681983563129L;

        /**
         * Host.
         */
        @Configure
        private String host;

        /**
         * Port.
         */
        @Configure
        private int port;

        /**
         * Default constructor.
         */
        public Host() {
        }

        /**
         * Public constructor.
         *
         * @param aHost
         *            host
         * @param aPort
         *            port
         */
        public Host(final String aHost, int aPort) {
            this.host = aHost;
            this.port = aPort;
        }

        public String getHost() {
            return host != null ? host : "";
        }

        public void setHost(final String aHost) {
            this.host = aHost;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int aPort) {
            this.port = aPort;
        }

        @Override
        public String toString() {
            return "Host [host=" + host + ", port=" + port + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((host == null) ? 0 : host.hashCode());
            result = prime * result + port;
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
            Host other = (Host) obj;
            if (host == null) {
                if (other.host != null)
                    return false;
            } else if (!host.equals(other.host))
                return false;
            if (port != other.port)
                return false;
            return true;
        }

    }
}