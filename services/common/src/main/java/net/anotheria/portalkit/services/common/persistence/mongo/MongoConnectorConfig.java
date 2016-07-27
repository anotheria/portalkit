package net.anotheria.portalkit.services.common.persistence.mongo;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Mongo configuration uri
 */
@ConfigureMe(allfields = true)
public class MongoConnectorConfig {

    @Configure
    private String dbName;

    @Configure
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"MongoConnectorConfig\", " +
                "\"dbName\":" + (dbName == null ? "null" : "\"" + dbName + "\"") + ", " +
                "\"uri\":" + (uri == null ? "null" : "\"" + uri + "\"") +
                "}";
    }
}
