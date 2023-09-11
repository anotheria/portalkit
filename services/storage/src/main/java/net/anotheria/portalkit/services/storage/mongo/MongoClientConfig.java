package net.anotheria.portalkit.services.storage.mongo;

import com.mongodb.ServerAddress;
import org.configureme.ConfigurationManager;
import org.configureme.Environment;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.configureme.environments.DynamicEnvironment;
import org.configureme.sources.ConfigurationSourceKey.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mongo client configuration.
 * 
 * @author Alexandr Bolbat
 */
@ConfigureMe(name = "pk-storage-mongo-client-config", allfields = false)
public final class MongoClientConfig implements Serializable {

	/**
	 * Generated SerialVersionUID.
	 */
	@DontConfigure
	private static final long serialVersionUID = 2199084571495485994L;

	/**
	 * {@link Logger} instance.
	 */
	@DontConfigure
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoClientConfig.class);

	/**
	 * Synchronization object.
	 */
	@DontConfigure
	private static final Object LOCK = new Object();

	/**
	 * {@link MongoClientConfig} instance.
	 */
	@DontConfigure
	private static volatile MongoClientConfig instance;

	/**
	 * Mongo client configuration description.
	 */
	@Configure
	private String description;

	/**
	 * Array with {@link Host} configurations.
	 */
	@Configure
	private Host[] hosts;

	/**
	 * Array with {@link DB} configurations.
	 */
	@Configure
	private DB[] databases;

	/**
	 * Connection string.
	 */
	@Configure
	private String connectionString;

	/**
	 * Maximum connections amount per host.
	 */
	@Configure
	private int connectionsPerHost = 100;

	/**
	 * Maximum connection timeout.
	 */
	@Configure
	private int connectionTimeout = 1000 * 10;

	/**
	 * Maximum socket timeout.
	 */
	@Configure
	private int socketTimeout = 0;

	/**
	 * Socket keep alive option.
	 */
	@Configure
	private boolean socketKeepAlive = false;

	/**
	 * Auto connection option.
	 */
	@Configure
	private boolean autoConnectRetry = false;

	/**
	 * Maximum auto connection retry timeout.
	 */
	@Configure
	private long autoConnectRetryMaxTimeout = 0;

	/**
	 * Read concern type.
	 */
	@Configure
	private ReadConcernType readConcernType = ReadConcernType.DEFAULT;

	/**
	 * Write concern type.
	 */
	@Configure
	private WriteConcernType writeConcernType = WriteConcernType.DEFAULT;

	/**
	 * Write concert write strategy. Used if 'writeConcernType' configured to 'WriteConcernType.CUSTOM'.<br>
	 * Options:<br>
	 * -1 - don't even report network errors,<br>
	 * 0 - don't wait for acknowledgment from the server,<br>
	 * 1 - wait for acknowledgment, but don't wait for secondaries to replicate,<br>
	 * 2 - wait for one or more secondaries to also acknowledge.
	 */
	@Configure
	private int writeConcernWriteStrategy;

	/**
	 * Write concern write timeout. Used if 'writeConcernType' configured to 'WriteConcernType.CUSTOM'.<br>
	 * Options: 0 - indefinite, greater then 0 - milliseconds to wait.
	 */
	@Configure
	private int writeConcernWriteTimeout;

	/**
	 * Write concern option to force fsync to disk. Used if 'writeConcernType' configured to 'WriteConcernType.CUSTOM'.<br>
	 */
	@Configure
	private boolean writeConcernForceFSyncToDisk;

	/**
	 * Write concern option for waiting group commit to journal. Used if 'writeConcernType' configured to 'WriteConcernType.CUSTOM'.<br>
	 */
	@Configure
	private boolean writeConcernWaitGroupCommitToJournal;

	/**
	 * Write concern option for continue on insert error. Used if 'writeConcernType' configured to 'WriteConcernType.CUSTOM'.<br>
	 */
	@Configure
	private boolean writeConcernContinueOnInsertError;

	/**
	 * Default constructor.
	 * 
	 * @param configuration
	 *            configuration name
	 * @param environment
	 *            configuration environment
	 */
	private MongoClientConfig(final String configuration, final Environment environment) {
		try {
			if (configuration == null || configuration.trim().isEmpty()) {
				ConfigurationManager.INSTANCE.configure(this, environment);
			} else {
				ConfigurationManager.INSTANCE.configureAs(this, environment, configuration, Format.JSON);
			}
		} catch (RuntimeException e) {
			LOGGER.warn("MongoClientConfig(conf:" + configuration + ", env: " + environment + ") Configuration fail[" + e.getMessage()
					+ "]. Relaying on defaults.", e);

			this.description = "";
			this.hosts = new Host[] { new Host(ServerAddress.defaultHost(), ServerAddress.defaultPort()) };
		}

		LOGGER.info("MongoClientConfig(conf:" + configuration + ", env: " + environment + ") Configured with[" + this.toString() + "]");
	}

	/**
	 * Get configured instance of {@link MongoClientConfig}.
	 * 
	 * @return {@link MongoClientConfig}
	 */
	public static MongoClientConfig getInstance() {
		return getInstanceForConfiguration(null);
	}

	/**
	 * Get configured instance of {@link MongoClientConfig}.
	 * 
	 * @param configuration
	 *            configuration name, can be <code>null</code> or empty
	 * @return {@link MongoClientConfig}
	 */
	public static MongoClientConfig getInstanceForConfiguration(final String configuration) {
		return getInstance(configuration, null);
	}

	/**
	 * Get configured instance of {@link MongoClientConfig}.
	 * 
	 * @param environment
	 *            environment name, can be <code>null</code> or empty
	 * @return {@link MongoClientConfig}
	 */
	public static MongoClientConfig getInstanceForEnvironment(final String environment) {
		return getInstance(null, environment);
	}

	/**
	 * Get configured instance of {@link MongoClientConfig}.
	 * 
	 * @param configuration
	 *            configuration name, can be <code>null</code> or empty
	 * @param environment
	 *            environment name, can be <code>null</code> or empty
	 * @return {@link MongoClientConfig}
	 */
	public static MongoClientConfig getInstance(final String configuration, final String environment) {
		if ((configuration == null || configuration.trim().isEmpty()) && (environment == null || environment.trim().isEmpty())) {
			if (instance != null)
				return instance;

			synchronized (LOCK) {
				if (instance == null)
					instance = new MongoClientConfig(null, ConfigurationManager.INSTANCE.getDefaultEnvironment());
			}

			return instance;
		}

		Environment env = ConfigurationManager.INSTANCE.getDefaultEnvironment();
		if (environment != null && !environment.trim().isEmpty())
			env = DynamicEnvironment.parse(environment);

		return new MongoClientConfig(configuration, env);
	}

	public String getDescription() {
		return description != null ? description : "";
	}

	public void setDescription(final String aDescription) {
		this.description = aDescription;
	}

	/**
	 * Get host configuration.<br>
	 * If configuration for requested host not found, <code>null</code> will be returned.
	 * 
	 * @param name
	 *            host name
	 * @return {@link Host}
	 */
	public Host getHost(final String name) {
		if (name == null || name.trim().length() == 0)
			return null;

		if (hosts != null && hosts.length > 0)
			for (Host host : hosts)
				if (host.getHost().equals(name))
					return host;

		return null;
	}

	/**
	 * Get hosts configuration.
	 * 
	 * @return {@link List} of {@link Host}
	 */
	public List<Host> getHosts() {
		return hosts != null ? Arrays.asList(hosts) : new ArrayList<Host>();
	}

	/**
	 * Set hosts configuration.
	 * 
	 * @param aHosts
	 *            configured hosts
	 */
	public void setHosts(final List<Host> aHosts) {
		this.hosts = aHosts != null ? aHosts.toArray(new Host[aHosts.size()]) : null;
	}

	public void setHosts(final Host[] aHosts) {
		this.hosts = aHosts != null ? aHosts.clone() : null;
	}

	/**
	 * Get database configuration.<br>
	 * If configuration for requested database not found, new one will be created with default configuration.
	 * 
	 * @param name
	 *            database name, can't be empty
	 * @return {@link DB}
	 */
	public DB getDatabase(final String name) {
		if (name == null || name.trim().length() == 0)
			throw new IllegalArgumentException("name argument is empty");

		if (databases != null && databases.length > 0)
			for (DB db : databases)
				if (db.getName().equals(name))
					return db;

		return new DB(name, false, "not-configured", "not-configured");
	}

	/**
	 * Get databases configuration.
	 * 
	 * @return {@link List} of {@link DB}
	 */
	public List<DB> getDatabases() {
		return databases != null ? Arrays.asList(databases) : new ArrayList<DB>();
	}

	/**
	 * Set databases configuration.
	 * 
	 * @param aDatabases
	 *            configured databases
	 */
	public void setDatabases(final List<DB> aDatabases) {
		this.databases = aDatabases != null ? aDatabases.toArray(new DB[aDatabases.size()]) : null;
	}

	public void setDatabases(final DB[] aDatabases) {
		this.databases = aDatabases != null ? aDatabases.clone() : null;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public int getConnectionsPerHost() {
		return connectionsPerHost;
	}

	public void setConnectionsPerHost(final int aConnectionsPerHost) {
		this.connectionsPerHost = aConnectionsPerHost;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(final int aConnectionTimeout) {
		this.connectionTimeout = aConnectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(final int aSocketTimeout) {
		this.socketTimeout = aSocketTimeout;
	}

	public boolean isSocketKeepAlive() {
		return socketKeepAlive;
	}

	public void setSocketKeepAlive(final boolean aSocketKeepAlive) {
		this.socketKeepAlive = aSocketKeepAlive;
	}

	public boolean isAutoConnectRetry() {
		return autoConnectRetry;
	}

	public void setAutoConnectRetry(final boolean aAutoConnectRetry) {
		this.autoConnectRetry = aAutoConnectRetry;
	}

	public long getAutoConnectRetryMaxTimeout() {
		return autoConnectRetryMaxTimeout;
	}

	public void setAutoConnectRetryMaxTimeout(final long aAutoConnectRetryMaxTimeout) {
		this.autoConnectRetryMaxTimeout = aAutoConnectRetryMaxTimeout;
	}

	public ReadConcernType getReadConcernType() {
		return readConcernType;
	}

	public void setReadConcernType(final ReadConcernType aReadConcernType) {
		this.readConcernType = aReadConcernType != null ? aReadConcernType : ReadConcernType.DEFAULT;
	}

	public WriteConcernType getWriteConcernType() {
		return writeConcernType;
	}

	public void setWriteConcernType(final WriteConcernType aWriteConcernType) {
		this.writeConcernType = aWriteConcernType != null ? aWriteConcernType : WriteConcernType.DEFAULT;
	}

	public int getWriteConcernWriteStrategy() {
		return writeConcernWriteStrategy;
	}

	public void setWriteConcernWriteStrategy(final int aWriteConcernWriteStrategy) {
		this.writeConcernWriteStrategy = aWriteConcernWriteStrategy;
	}

	public int getWriteConcernWriteTimeout() {
		return writeConcernWriteTimeout;
	}

	public void setWriteConcernWriteTimeout(final int aWriteConcernWriteTimeout) {
		this.writeConcernWriteTimeout = aWriteConcernWriteTimeout;
	}

	public boolean isWriteConcernForceFSyncToDisk() {
		return writeConcernForceFSyncToDisk;
	}

	public void setWriteConcernForceFSyncToDisk(final boolean aWriteConcernForceFSyncToDisk) {
		this.writeConcernForceFSyncToDisk = aWriteConcernForceFSyncToDisk;
	}

	public boolean isWriteConcernWaitGroupCommitToJournal() {
		return writeConcernWaitGroupCommitToJournal;
	}

	public void setWriteConcernWaitGroupCommitToJournal(final boolean aWriteConcernWaitGroupCommitToJournal) {
		this.writeConcernWaitGroupCommitToJournal = aWriteConcernWaitGroupCommitToJournal;
	}

	public boolean isWriteConcernContinueOnInsertError() {
		return writeConcernContinueOnInsertError;
	}

	public void setWriteConcernContinueOnInsertError(final boolean aWriteConcernContinueOnInsertError) {
		this.writeConcernContinueOnInsertError = aWriteConcernContinueOnInsertError;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tMongoClientConfig [\n\t description=");
		builder.append(description);
		builder.append(",\n\t hosts=");
		builder.append(Arrays.toString(hosts));
		builder.append(",\n\t databases=");
		builder.append(Arrays.toString(databases));
		builder.append(",\n\t connectionsPerHost=");
		builder.append(connectionsPerHost);
		builder.append(", connectionTimeout=");
		builder.append(connectionTimeout);
		builder.append(",\n\t socketTimeout=");
		builder.append(socketTimeout);
		builder.append(", socketKeepAlive=");
		builder.append(socketKeepAlive);
		builder.append(",\n\t autoConnectRetry=");
		builder.append(autoConnectRetry);
		builder.append(", autoConnectRetryMaxTimeout=");
		builder.append(autoConnectRetryMaxTimeout);
		builder.append(",\n\t readConcernType=");
		builder.append(readConcernType);
		builder.append(",\n\t writeConcernType=");
		builder.append(writeConcernType);
		if (writeConcernType == WriteConcernType.CUSTOM) {
			builder.append(", writeConcernWriteStrategy=");
			builder.append(writeConcernWriteStrategy);
			builder.append(", writeConcernWriteTimeout=");
			builder.append(writeConcernWriteTimeout);
			builder.append(", writeConcernForceFSyncToDisk=");
			builder.append(writeConcernForceFSyncToDisk);
			builder.append(", writeConcernWaitGroupCommitToJournal=");
			builder.append(writeConcernWaitGroupCommitToJournal);
			builder.append(", writeConcernContinueOnInsertError=");
			builder.append(writeConcernContinueOnInsertError);
		}
		builder.append("\n\t]\n");
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

	/**
	 * DB configuration.
	 * 
	 * @author Alexandr Bolbat
	 */
	public static class DB implements Serializable {

		/**
		 * Generated SerialVersionUID.
		 */
		@DontConfigure
		private static final long serialVersionUID = 7854810046511496206L;

		/**
		 * Database name.
		 */
		@Configure
		private String name;

		/**
		 * Is authentication required.
		 */
		@Configure
		private boolean authenticate = false;

		/**
		 * Authentication user name.
		 */
		@Configure
		private String username;

		/**
		 * Authentication password.
		 */
		@Configure
		private String password;

		/**
		 * Default constructor.
		 */
		public DB() {
		}

		/**
		 * Public constructor.
		 * 
		 * @param aName
		 *            database name
		 * @param aAuthenticate
		 *            is authentication required
		 * @param aUsername
		 *            authentication user name
		 * @param aPassword
		 *            authentication password
		 */
		public DB(final String aName, final boolean aAuthenticate, final String aUsername, final String aPassword) {
			this.name = aName;
			this.authenticate = aAuthenticate;
			this.username = aUsername;
			this.password = aPassword;
		}

		public String getName() {
			return name != null ? name : "";
		}

		public void setName(final String aName) {
			this.name = aName;
		}

		public boolean isAuthenticate() {
			return authenticate;
		}

		public void setAuthenticate(final boolean aAuthenticate) {
			this.authenticate = aAuthenticate;
		}

		public String getUsername() {
			return username != null ? username : "";
		}

		public void setUsername(final String aUsername) {
			this.username = aUsername;
		}

		public String getPassword() {
			return password != null ? password : "";
		}

		public void setPassword(final String aPassword) {
			this.password = aPassword;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("DB [name=");
			builder.append(name);
			builder.append(", authenticate=");
			builder.append(authenticate);
			builder.append(", username=");
			builder.append(username);
			builder.append(", password=");
			builder.append(password);
			builder.append("]");
			return builder.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			DB other = (DB) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}

	/**
	 * Read concern type.
	 * 
	 * @author Alexandr Bolbat
	 */
	public enum ReadConcernType {

		/**
		 * Reads from primary only.
		 */
		PRIMARY,

		/**
		 * Reads primary if available.
		 */
		PRIMARY_PREFERRED,

		/**
		 * Reads from secondary only.
		 */
		SECONDARY,

		/**
		 * Reads secondary if available.
		 */
		SECONDARY_PREFERRED,

		/**
		 * Reads nearest node.
		 */
		NEAREST;

		/**
		 * Default {@link ReadConcernType}.
		 */
		public static final ReadConcernType DEFAULT = PRIMARY_PREFERRED;

	}

	/**
	 * Write concern type.
	 * 
	 * @author Alexandr Bolbat
	 */
	public enum WriteConcernType {

		/**
		 * No exceptions are raised, even for network issues.
		 */
		ERRORS_IGNORED,

		/**
		 * Write operations that use this write concern will wait for acknowledgement from the primary server before returning.<br>
		 * Exceptions are raised for network issues, and server errors.
		 */
		ACKNOWLEDGED,

		/**
		 * Write operations that use this write concern will return as soon as the message is written to the socket.<br>
		 * Exceptions are raised for network issues, but not server errors.
		 */
		UNACKNOWLEDGED,

		/**
		 * Exceptions are raised for network issues, and server errors.<br>
		 * The write operation waits for the server to flush the data to disk.
		 */
		FSYNCED,

		/**
		 * Exceptions are raised for network issues, and server errors.<br>
		 * The write operation waits for the server to group commit to the journal file on disk.
		 */
		JOURNALED,

		/**
		 * Exceptions are raised for network issues, and server errors.<br>
		 * Waits for at least 2 servers for the write operation.
		 */
		REPLICA_ACKNOWLEDGED,

		/**
		 * Exceptions are raised for network issues, and server errors.<br>
		 * Waits on a majority of servers for the write operation.
		 */
		MAJORITY,

		/**
		 * Custom write concern configuration.
		 */
		CUSTOM;

		/**
		 * Default {@link WriteConcernType}.
		 */
		public static final WriteConcernType DEFAULT = ACKNOWLEDGED;

	}

}
