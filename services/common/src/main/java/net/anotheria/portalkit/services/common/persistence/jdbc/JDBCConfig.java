package net.anotheria.portalkit.services.common.persistence.jdbc;

import org.configureme.annotations.ConfigureMe;

/**
 * JDBC configuration class.
 * 
 * @author lrosenberg
 * @since 08.01.13 00:31
 */
@ConfigureMe(allfields = true)
public class JDBCConfig {

	/**
	 * Connection URL.
	 */
	private String url;

	/**
	 * DB driver class name.
	 */
	private String driver;

	/**
	 * User name to DB.
	 */
	private String username;

	/**
	 * Password to {@code username}.
	 */
	private String password;

	/**
	 * Max opened connections.
	 */
	private int maxConnections;

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return getDriver() + ", " + getUrl() + ", " + getUsername();
	}
}
