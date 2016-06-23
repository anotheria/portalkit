package net.anotheria.portalkit.services.common.persistence.mongo;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Mongo configuration
 */
@ConfigureMe(name = "pk-mongo-auth")
public class MongoConnectorConfig {

    @Configure
    private String host;

    @Configure
    private String port;

    @Configure
    private String dbName;

    @Configure
    private String login;

    @Configure
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "MongoConfigurationServiceConfig{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", dbName='" + dbName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
