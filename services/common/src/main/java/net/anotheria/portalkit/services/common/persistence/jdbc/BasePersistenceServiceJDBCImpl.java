package net.anotheria.portalkit.services.common.persistence.jdbc;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationInfoService;
import net.anotheria.util.StringUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.configureme.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.net.SocketException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base persistence service.
 */
public abstract class BasePersistenceServiceJDBCImpl implements BasePersistenceService {

	/**
	 * Data source.
	 */
	private BasicDataSource dataSource;

	/**
	 * Logger.
	 */
	private Logger log = LoggerFactory.getLogger(BasePersistenceServiceJDBCImpl.class);

	/**
	 * PROXY factory.
	 */
	private GenericReconnectionProxyFactory proxyFactory;

	/**
	 * Reconnection flag.
	 */
	private AtomicBoolean isBeingReconnected = new AtomicBoolean(false);

	/**
	 * Name of the configuration. Can be ommited.
	 */
	private String configName;

	/**
	 * List of registered DAO's.
	 */
	private ArrayList<DAO> daos = new ArrayList<DAO>();

    private static Map<String, BasicDataSource> dataSourceMap = new HashMap<>();

	/**
	 * Default constructor.
	 */
	protected BasePersistenceServiceJDBCImpl() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param aConfigName	name of configuration file.
	 */
	protected BasePersistenceServiceJDBCImpl(String aConfigName) {
		configName = aConfigName;
		proxyFactory = new GenericReconnectionProxyFactory();
		init();
	}

	/**
	 * Initialize data source.
	 */
	@Override
	public void init() {

        if (configName == null)
            throw new IllegalStateException("Config not set");
        JDBCConfig config = new JDBCConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configName);
        log.info("Using config: " + config);
        if (!StringUtils.isEmpty(config.getDatasourceName())) {
            if (!dataSourceMap.containsKey(config.getDatasourceName())) {
                dataSourceMap.put(config.getDatasourceName(), createDataSource(config));
            }
            this.dataSource = dataSourceMap.get(config.getDatasourceName());
        } else {
            this.dataSource = createDataSource(config);
        }

        String dbtype = "psql";
		if (config.getDriver() != null) {
			if (config.getDriver().contains("mysql")) {
				dbtype = "mysql";
			}
			if (config.getDriver().contains("postgresql")) {
				dbtype = "psql";
			}
			if (config.getDriver().contains("h2")) {
				dbtype = "h2";
			}
		}
		
		// prepare db.
		Flyway flyway = new Flyway();
		flyway.setDataSource(getDataSource());
		flyway.setLocations(getClass().getPackage().getName() + ".migrations.common", getClass().getPackage().getName() + ".migrations." + dbtype);
		flyway.setTable(getTableNameForMigration());
		flyway.setInitOnMigrate(true);
		flyway.migrate();
		MigrationInfoService flywayInfo = flyway.info();
		if (flywayInfo.current()==null){
			log.info("MigrationFailed? Current is null!");
		}else {
			log.info("Flyway current version:" + flywayInfo.current().getVersion());
		}
	}

    private BasicDataSource createDataSource(JDBCConfig config) {
        BasicDataSource newDataSource = new BasicDataSource();
        newDataSource.setDriverClassName(config.getDriver());
        newDataSource.setUrl(config.getUrl());
        newDataSource.setUsername(config.getUsername());
        newDataSource.setPassword(config.getPassword());
        if (config.getMaxConnections() != Integer.MAX_VALUE && config.getMaxConnections() > 0)
            newDataSource.setMaxActive(config.getMaxConnections());
        return newDataSource;
    }

	private String getTableNameForMigration() {
		String[] commonPackage = StringUtils.tokenize(BasePersistenceServiceJDBCImpl.class.getPackage().getName(), '.');
		String[] customPackage = StringUtils.tokenize(getClass().getPackage().getName(), '.');
		HashSet<String> parts = new HashSet<String>();
		for (String p : customPackage) {
			parts.add(p);
		}
		for (String p : commonPackage) {
			parts.remove(p);
		}

		for (String p: getPackagePartsToExcludeFromFlywayTableName()){
			parts.remove(p);
		}

		StringBuilder name = new StringBuilder();
		for (Iterator<String> it = parts.iterator(); it.hasNext();) {
			if (name.length() > 0)
				name.append('_');
			name.append(it.next());
		}
		return "flyway_" + name.toString();
	}

	protected String[] getPackagePartsToExcludeFromFlywayTableName(){
		return new String[]{};
	}

	/**
	 * Get connection from pool.
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws java.sql.SQLException	if error.
	 */
	protected Connection getConnection() throws SQLException {
		if (isBeingReconnected.get())
			throw new JDBCConnectionException("Database connection problem.");

		try {
			return Connection.class.cast(proxyFactory.makeProxy(Connection.class, dataSource.getConnection()));
		} catch (SQLException sqle) {
			handleJDBCConnectionException(sqle);
			throw sqle;
		}
	}

	protected DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Check exception for connection exception type and throw named runtime
	 * exception.
	 * 
	 * @param error
	 *            - {@link Throwable}
	 * @throws JDBCConnectionException
	 */
	private void handleJDBCConnectionException(Throwable error) throws JDBCConnectionException {
		if (isBeingReconnected.get())
			throw new JDBCConnectionException("Database connection problem.");

		if (error instanceof SocketException || error instanceof ConnectException) {
			isBeingReconnected.set(true);
			try {
				init();
			} finally {
				isBeingReconnected.set(false);
			}
			throw new JDBCConnectionException("Database connection problem.", error);
		}

		if (error.getCause() != null)
			handleJDBCConnectionException(error.getCause());
	}

	/**
	 * Factory for creating PROXY for some JDBC layer implementations for
	 * handling JDBC connection exceptions and reloading data source.
	 */
	private class GenericReconnectionProxyFactory {

		/**
		 * 
		 */
		public static final String CREATE_STATEMENT = "createStatement";
		/**
		 * 
		 */
		public static final String PREPARE_STATEMENT = "prepareStatement";
		/**
		 * 
		 */
		public static final String PREPARE_CALL = "prepareCall";
		/**
		 * 
		 */
		public static final String META_DATA = "getMetaData";
		/**
		 * 
		 */
		public static final String CLOSE = "close";
		/**
		 * 
		 */
		public static final String IS_CLOSED = "isClosed";

		/**
		 * Set of method names.
		 */
		private final Set<String> methodNames = new HashSet<String>();
		
		/**
		 * Set of class names.
		 */
		private final Set<String> classNames = new HashSet<String>();

		public GenericReconnectionProxyFactory() {
			methodNames.add(CREATE_STATEMENT);
			methodNames.add(PREPARE_STATEMENT);
			methodNames.add(PREPARE_CALL);
			methodNames.add(META_DATA);
			methodNames.add(CLOSE);
			methodNames.add(IS_CLOSED);

			classNames.add(Connection.class.getName());
			classNames.add(DatabaseMetaData.class.getName());
			classNames.add(Statement.class.getName());
			classNames.add(PreparedStatement.class.getName());
			classNames.add(CallableStatement.class.getName());
			classNames.add(ResultSet.class.getName());
		}

		public Object makeProxy(Class<?> intf, final Object obj) {
			if (classNames.contains(intf.getName()))
				return Proxy.newProxyInstance(obj.getClass().getClassLoader(), new Class[] { intf }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if (classNames.contains(method.getDeclaringClass().getName()) && methodNames.contains(method.getName()))
							return makeProxy(method.getReturnType(), invokeMethod(obj, method, args));

						return invokeMethod(obj, method, args);
					}
				});

			return obj;
		}

		public Object invokeMethod(Object proxy, Method method, Object[] args) throws Throwable {
			if (isBeingReconnected.get())
				throw new JDBCConnectionException("Database connection problem.");

			try {
				return method.invoke(proxy, args);
			} catch (InvocationTargetException e) {
				handleJDBCConnectionException(e);

				throw e.getCause();
			}
		}
	}

	/**
	 * 
	 * @param someDaos list of {@link DAO} objects.
	 */
	protected void addDaos(DAO... someDaos) {
		if (someDaos != null)
			for (DAO d : someDaos)
				daos.add(d);
	}

	/**
	 * 
	 * @throws SQLException if error.
	 * @throws DAOException if error.
	 */
	@Override
	public void cleanupFromUnitTests() throws SQLException, DAOException {
		for (DAO d : daos) {
			Connection conn = getConnection();
			try {
				d.cleanupFromUnitTests(conn);
			} finally {
				JDBCUtil.close(conn);
			}
		}
	}

}
