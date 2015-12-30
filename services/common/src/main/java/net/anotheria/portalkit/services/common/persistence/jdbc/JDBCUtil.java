package net.anotheria.portalkit.services.common.persistence.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for closing JDBC resources.
 * 
 * @author Alexandr Bolbat
 */
public final class JDBCUtil {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCUtil.class.getName());

	/**
	 * Default constructor.
	 */
	private JDBCUtil() {
		throw new IllegalAccessError("Can't instantiate utility class.");
	}

	/**
	 * Close {@link Connection} if it opened. If {@link SQLException} happen on closing it will be logged.
	 * 
	 * @param conn
	 *            - {@link Connection} object
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.warn("close(" + conn + ") fail.", e);
		}
	}

	/**
	 * Close {@link Statement} if it opened. If {@link SQLException} happen on closing it will be logged.
	 * 
	 * @param st
	 *            - {@link Statement} object
	 */
	public static void close(Statement st) {
		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
			LOGGER.warn("close(" + st + ") fail.", e);
		}
	}

	/**
	 * Close {@link ResultSet} if it opened. If {@link SQLException} happen on closing it will be logged.
	 * 
	 * @param rs
	 *            - {@link ResultSet} object
	 */
	public static void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			LOGGER.warn("close(" + rs + ") fail.", e);
		}
	}

	/**
	 * Close {@link Connection} if it opened. If {@link SQLException} happen on closing it will be logged.
	 * 
	 * @param conn
	 *            - {@link Connection} object
	 */
	public static void release(Connection conn) {
		close(conn);
	}

	/**
	 * Close {@link Statement} if it opened. If {@link SQLException} happen on closing it will be logged.
	 * 
	 * @param st
	 *            - {@link Statement} object
	 */
	public static void release(Statement st) {
		close(st);
	}

	/**
	 * Close {@link ResultSet} if it opened. If {@link SQLException} happen on closing it will be logged.
	 * 
	 * @param rs
	 *            - {@link ResultSet} object
	 */
	public static void release(ResultSet rs) {
		close(rs);
	}

	/**
	 * Roll back executed queries from last commit for given connection. If {@link SQLException} happen on closing it will be logged.
	 * 
	 * @param conn
	 *            - {@link Connection}
	 */
	public static void rollback(Connection conn) {
		try {
			if (conn != null && !conn.isClosed())
				conn.rollback();
		} catch (SQLException sqle) {
			LOGGER.warn("rollback(java.sql.Connection) rollback fail.", sqle);
		}
	}

}
