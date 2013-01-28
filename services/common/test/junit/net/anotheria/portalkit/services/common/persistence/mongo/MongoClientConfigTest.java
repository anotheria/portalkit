package net.anotheria.portalkit.services.common.persistence.mongo;

import java.util.List;

import net.anotheria.portalkit.services.common.persistence.mongo.MongoClientConfig.DB;
import net.anotheria.portalkit.services.common.persistence.mongo.MongoClientConfig.Host;
import net.anotheria.portalkit.services.common.persistence.mongo.MongoClientConfig.ReadConcernType;
import net.anotheria.portalkit.services.common.persistence.mongo.MongoClientConfig.WriteConcernType;

import org.junit.Assert;
import org.junit.Test;

import com.mongodb.ServerAddress;

/**
 * {@link MongoClientConfig} test.
 * 
 * @author Alexandr Bolbat
 */
public class MongoClientConfigTest {

	/**
	 * Test general configuration functionality.
	 */
	@Test
	public void testConfiguration() {
		MongoClientConfig configuration = MongoClientConfig.getInstance();
		Assert.assertNotNull("Configuration can't be null", configuration);
		Assert.assertEquals("Descriptions should be equal", "DEFAULT CLIENT", configuration.getDescription());
		Assert.assertNotNull("Hosts can't be null", configuration.getHosts());
		Assert.assertEquals("One host should be configured", 1, configuration.getHosts().size());
		Assert.assertEquals("Host should be the same", "localhost", configuration.getHosts().get(0).getHost());
		Assert.assertEquals("Port should be the same", 27017, configuration.getHosts().get(0).getPort());
	}

	/**
	 * Test default configuration.
	 */
	@Test
	public void testDefaultConfiguration() {
		MongoClientConfig configuration = MongoClientConfig.getInstanceForConfiguration("not-exist-configuration");
		Assert.assertNotNull("Configuration can't be null", configuration);
		Assert.assertEquals("Descriptions should be equal", "", configuration.getDescription());
		Assert.assertNotNull("Hosts can't be null", configuration.getHosts());
		Assert.assertEquals("One host should be configured", 1, configuration.getHosts().size());
		Assert.assertEquals("Host should be the same", ServerAddress.defaultHost(), configuration.getHosts().get(0).getHost());
		Assert.assertEquals("Port should be the same", ServerAddress.defaultPort(), configuration.getHosts().get(0).getPort());
	}

	/**
	 * Test full configuration.
	 */
	@Test
	public void testFullConfiguration() {
		MongoClientConfig configuration = MongoClientConfig.getInstanceForEnvironment("full-configuration-example");
		Assert.assertNotNull("Configuration can't be null", configuration);
		Assert.assertEquals("Descriptions should be equal", "FULLY CONFIGURED CLIENT", configuration.getDescription());

		// hosts
		Assert.assertNotNull("Hosts can't be null", configuration.getHosts());
		Assert.assertEquals("Two hosts should be configured", 2, configuration.getHosts().size());
		Assert.assertEquals("Host should be the same", "full-localhost-1", configuration.getHosts().get(0).getHost());
		Assert.assertEquals("Host should be the same", "full-localhost-2", configuration.getHosts().get(1).getHost());
		Assert.assertEquals("Port should be the same", 27017, configuration.getHosts().get(0).getPort());
		Assert.assertEquals("Port should be the same", 27018, configuration.getHosts().get(1).getPort());

		Host host1 = configuration.getHost("full-localhost-1");
		Assert.assertNotNull("Host can't be null", host1);
		Assert.assertEquals("Host should be the same", "full-localhost-1", host1.getHost());
		Assert.assertEquals("Port should be the same", 27017, host1.getPort());

		// databases
		List<DB> databases = configuration.getDatabases();
		Assert.assertNotNull("Databases can't be null", databases);
		Assert.assertEquals("Two databases should be configured", 2, databases.size());
		Assert.assertEquals("Database name should be the same", "full-test-1", databases.get(0).getName());
		Assert.assertEquals("Database name should be the same", "full-test-2", databases.get(1).getName());
		Assert.assertEquals("Database authentication should be disabled", false, databases.get(0).isAuthenticate());
		Assert.assertEquals("Database authentication should be enabled", true, databases.get(1).isAuthenticate());
		Assert.assertEquals("User name should be the same", "not-required", databases.get(0).getUsername());
		Assert.assertEquals("User name should be the same", "user-123", databases.get(1).getUsername());
		Assert.assertEquals("Password should be the same", "not-configured", databases.get(0).getPassword());
		Assert.assertEquals("Password should be the same", "password-321", databases.get(1).getPassword());

		DB database2 = configuration.getDatabase("full-test-2");
		Assert.assertNotNull("Database can't be null", database2);
		Assert.assertEquals("Database name should be the same", "full-test-2", database2.getName());
		Assert.assertEquals("Database authentication should be disabled", true, database2.isAuthenticate());
		Assert.assertEquals("User name should be the same", "user-123", database2.getUsername());
		Assert.assertEquals("Password should be the same", "password-321", database2.getPassword());

		// other options
		Assert.assertEquals("Connections amount per host should be the same", 250, configuration.getConnectionsPerHost());
		Assert.assertEquals("Connection timeout should be the same", 15000, configuration.getConnectionTimeout());
		Assert.assertEquals("Socket timeout should be the same", 0, configuration.getSocketTimeout());
		Assert.assertEquals("Socket keep alive should be enabled", true, configuration.isSocketKeepAlive());
		Assert.assertEquals("Auto connect retry should be enabled", true, configuration.isAutoConnectRetry());
		Assert.assertEquals("Auto connect retry timout should be the same", 1000, configuration.getAutoConnectRetryMaxTimeout());
		Assert.assertEquals("Read concern type should be the same", ReadConcernType.NEAREST, configuration.getReadConcernType());
		Assert.assertEquals("Write concern type should be the same", WriteConcernType.CUSTOM, configuration.getWriteConcernType());
		Assert.assertEquals("Write strategy should be the same", 1, configuration.getWriteConcernWriteStrategy());
		Assert.assertEquals("Write timeout should be the same", 0, configuration.getWriteConcernWriteTimeout());
		Assert.assertEquals("Force 'fsync' should be disabled", false, configuration.isWriteConcernForceFSyncToDisk());
		Assert.assertEquals("Waiting group commit to journal should be disabled", false, configuration.isWriteConcernWaitGroupCommitToJournal());
		Assert.assertEquals("Continue on insert error should be disabled", false, configuration.isWriteConcernContinueOnInsertError());
	}

}
