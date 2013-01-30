package net.anotheria.portalkit.services.common.persistence.jdbc;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationInfoService;
import net.anotheria.util.StringUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base persistence service.
 */
public abstract class BasePersistenceServiceJDBCImpl {

	/**
	 * Data source.
	 */
	private BasicDataSource dataSource;

	/**
	 * Logger.
	 */
	protected Logger log = Logger.getLogger(BasePersistenceServiceJDBCImpl.class);

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

	private ArrayList<DAO> daos = new ArrayList<DAO>();

	/**
	 * Default constructor.
	 */
	protected BasePersistenceServiceJDBCImpl() {
		this(null);
	}

	protected BasePersistenceServiceJDBCImpl(String aConfigName) {
		configName = aConfigName;
		proxyFactory = new GenericReconnectionProxyFactory();
		init();
	}

	/**
	 * Initialize data source.
	 */
	public void init() {
		BasicDataSource newDataSource = new BasicDataSource();
		if (configName==null)
			throw new IllegalStateException("Config not set");
		JDBCConfig config = new JDBCConfig();
		ConfigurationManager.INSTANCE.configureAs(config, configName);
		log.info("Using config: " + config);

		newDataSource.setDriverClassName(config.getDriver());
		newDataSource.setUrl(config.getUrl());
		newDataSource.setUsername(config.getUsername());
		newDataSource.setPassword(config.getPassword());

		if (config.getMaxConnections() != Integer.MAX_VALUE && config.getMaxConnections()>0)
			newDataSource.setMaxActive(config.getMaxConnections());

		this.dataSource = newDataSource;

		//prepare db.
		Flyway flyway = new Flyway();
		flyway.setDataSource(getDataSource());
		flyway.setLocations(getClass().getPackage().getName() + ".migrations");
		flyway.setTable(getTableNameForMigration());
		flyway.setInitOnMigrate(true);
		flyway.migrate();
		MigrationInfoService flywayInfo =  flyway.info();
		//System.out.println("FLYWAY: ");
		//for (MigrationInfo mi : flywayInfo.applied()){
		//	System.out.println("Applied: "+mi.getVersion());
		//}
		//for (MigrationInfo mi : flywayInfo.pending()){
		//	System.out.println("Pending: "+mi.getVersion());
		//}
		log.info("Flyway current version:"+flywayInfo.current().getVersion());

	}

	private String getTableNameForMigration(){
		String[] commonPackage = StringUtils.tokenize(BasePersistenceServiceJDBCImpl.class.getPackage().getName(), '.');
		String[] customPackage = StringUtils.tokenize(getClass().getPackage().getName(), '.');
		HashSet<String> parts = new HashSet<String>();
		for (String p : customPackage){
			parts.add(p);
		}
		for (String p : commonPackage){
			parts.remove(p);
		}

		StringBuilder name = new StringBuilder();
		for (Iterator<String> it = parts.iterator(); it.hasNext();){
			if (name.length()>0)
				name.append('_');
			name.append(it.next());
		}
		return "flyway_"+name.toString();
	}

	/**
	 * Get connection from pool.
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws java.sql.SQLException
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

	protected DataSource getDataSource(){
		return dataSource;
	}

	/**
	 * Close {@link java.sql.Connection} if it opened. If {@link java.sql.SQLException} happen on closing it will be logged.
	 *
	 * @param conn
	 *            - {@link java.sql.Connection} object
	 */
	protected void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
		}
	}

	/**
	 * Close {@link java.sql.Statement} if it opened. If {@link java.sql.SQLException} happen on closing it will be logged.
	 *
	 * @param st
	 *            - {@link java.sql.Statement} object
	 */
	protected void close(Statement st) {
		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
		}
	}

	/**
	 * Close {@link java.sql.ResultSet} if it opened. If {@link java.sql.SQLException} happen on closing it will be logged.
	 *
	 * @param rs - {@link java.sql.ResultSet} object
	 */
	protected void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
		}
	}

	/**
	 * Close {@link java.sql.Connection} if it opened. If {@link java.sql.SQLException} happen on closing it will be logged.
	 *
	 * @param conn
	 *            - {@link java.sql.Connection} object
	 */
	protected void release(Connection conn) {
		close(conn);
	}

	/**
	 * Close {@link java.sql.Statement} if it opened. If {@link java.sql.SQLException} happen on closing it will be logged.
	 *
	 * @param st
	 *            - {@link java.sql.Statement} object
	 */
	protected void release(Statement st) {
		close(st);
	}

	/**
	 * Close {@link java.sql.ResultSet} if it opened. If {@link java.sql.SQLException} happen on closing it will be logged.
	 *
	 * @param rs
	 *            - {@link java.sql.ResultSet} object
	 */
	protected void release(ResultSet rs) {
		close(rs);
	}

	/**
	 * Check exception for connection exception type and throw named runtime exception.
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
	 * Factory for creating PROXY for some JDBC layer implementations for handling JDBC connection exceptions and reloading data source.
	 */
	private class GenericReconnectionProxyFactory {

		public static final String CREATE_STATEMENT = "createStatement";
		public static final String PREPARE_STATEMENT = "prepareStatement";
		public static final String PREPARE_CALL = "prepareCall";
		public static final String META_DATA = "getMetaData";
		public static final String CLOSE = "close";
		public static final String IS_CLOSED = "isClosed";

		public final Set<String> methodNames = new HashSet<String>();
		public final Set<String> classNames = new HashSet<String>();

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

	protected void addDaos(DAO ... someDaos){
		if (someDaos!=null)
			for (DAO d : someDaos)
				daos.add(d);
	}

	public void cleanupFromUnitTests() throws Exception {
		for (DAO d : daos)
			d.cleanupFromUnitTests(getConnection());
	}

}
