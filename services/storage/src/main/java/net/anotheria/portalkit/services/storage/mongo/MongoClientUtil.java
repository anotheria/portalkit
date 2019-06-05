package net.anotheria.portalkit.services.storage.mongo;

import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import net.anotheria.portalkit.services.storage.mongo.MongoClientConfig.Host;
import net.anotheria.portalkit.services.storage.mongo.MongoClientConfig.ReadConcernType;
import net.anotheria.portalkit.services.storage.mongo.MongoClientConfig.WriteConcernType;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for mongo client.
 * 
 * @author Alexandr Bolbat
 */
public final class MongoClientUtil {

	/**
	 * Default constructor.
	 */
	private MongoClientUtil() {
		throw new IllegalAccessError();
	}

	/**
	 * Get configured addresses for mongo client.
	 * 
	 * @param configuration
	 *            mongo client configuration
	 * @return {@link List} of {@link ServerAddress}
	 */
	public static List<ServerAddress> getAddresses(final MongoClientConfig configuration) {
		List<ServerAddress> result = new ArrayList<ServerAddress>();

		for (Host host : configuration.getHosts())
			result.add(new ServerAddress(new InetSocketAddress(host.getHost(), host.getPort())));

		return result;
	}

	/**
	 * Get {@link MongoClientOptions} for mongo client.
	 * 
	 * @param configuration
	 *            mongo client configuration
	 * @return configured {@link MongoClientOptions}
	 */
	public static MongoClientOptions getOptions(final MongoClientConfig configuration) {
		try {
			MongoClientOptions.Builder optionsBuilder = new MongoClientOptions.Builder();

			optionsBuilder.description(configuration.getDescription());
			optionsBuilder.connectionsPerHost(configuration.getConnectionsPerHost());
			optionsBuilder.connectTimeout(configuration.getConnectionTimeout());
			optionsBuilder.socketTimeout(configuration.getSocketTimeout());
			optionsBuilder.socketKeepAlive(configuration.isSocketKeepAlive());
			//LEON: removed this in migration to 3.6
			//optionsBuilder.autoConnectRetry(configuration.isAutoConnectRetry());
			//optionsBuilder.maxAutoConnectRetryTime(configuration.getAutoConnectRetryMaxTimeout());


			ReadConcernType readConcernType = configuration.getReadConcernType() != null ? configuration.getReadConcernType() : ReadConcernType.DEFAULT;
			switch (readConcernType) {
			case PRIMARY:
				optionsBuilder.readPreference(ReadPreference.primary());
				break;
			case PRIMARY_PREFERRED:
				optionsBuilder.readPreference(ReadPreference.primaryPreferred());
				break;
			case SECONDARY:
				optionsBuilder.readPreference(ReadPreference.secondary());
				break;
			case SECONDARY_PREFERRED:
				optionsBuilder.readPreference(ReadPreference.secondaryPreferred());
				break;
			case NEAREST:
				optionsBuilder.readPreference(ReadPreference.nearest());
				break;
			default:
				throw new UnsupportedOperationException("ReadConcernType[" + readConcernType + "] is unsupported.");
			}

			WriteConcernType writeConcernType = configuration.getWriteConcernType() != null ? configuration.getWriteConcernType() : WriteConcernType.DEFAULT;
			switch (writeConcernType) {
			case ERRORS_IGNORED:
				optionsBuilder.writeConcern(WriteConcern.ERRORS_IGNORED);
				break;
			case ACKNOWLEDGED:
				optionsBuilder.writeConcern(WriteConcern.ACKNOWLEDGED);
				break;
			case UNACKNOWLEDGED:
				optionsBuilder.writeConcern(WriteConcern.UNACKNOWLEDGED);
				break;
			case FSYNCED:
				optionsBuilder.writeConcern(WriteConcern.FSYNCED);
				break;
			case JOURNALED:
				optionsBuilder.writeConcern(WriteConcern.JOURNALED);
				break;
			case REPLICA_ACKNOWLEDGED:
				optionsBuilder.writeConcern(WriteConcern.REPLICA_ACKNOWLEDGED);
				break;
			case MAJORITY:
				optionsBuilder.writeConcern(WriteConcern.MAJORITY);
				break;
			case CUSTOM:
				int strategy = configuration.getWriteConcernWriteStrategy();
				int timeout = configuration.getWriteConcernWriteTimeout();
				boolean forceFsync = configuration.isWriteConcernForceFSyncToDisk();
				boolean waitGroupCommit = configuration.isWriteConcernWaitGroupCommitToJournal();
				boolean continueOnErrors = configuration.isWriteConcernContinueOnInsertError();
				optionsBuilder.writeConcern(new WriteConcern(strategy, timeout, forceFsync, waitGroupCommit, continueOnErrors));
				break;
			default:
				throw new UnsupportedOperationException("WriteConcernType[" + writeConcernType + "] is unsupported.");
			}

			return optionsBuilder.build();
		} catch (IllegalArgumentException e) {
			throw new MongoClientUtilException("getOptions(" + configuration + ") fail. Configuration is wrong.", e);
		}
	}

}
