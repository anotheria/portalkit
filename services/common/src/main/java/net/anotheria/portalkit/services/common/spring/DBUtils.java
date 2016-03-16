package net.anotheria.portalkit.services.common.spring;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.springframework.orm.jpa.vendor.Database;

/**
 * @author bvanchuhov
 */
public final class DBUtils {

    private DBUtils() {}

    public static Database getDatabase(String driverName) {
        if (driverName.contains("mysql")) return Database.MYSQL;
        if (driverName.contains("h2")) return Database.DB2;
        if (driverName.contains("postgresql")) return Database.POSTGRESQL;
        return Database.DEFAULT;
    }

    public static Class<? extends Dialect> getHibernateDialect(Database database) {
        switch (database) {
            case MYSQL: return MySQL5Dialect.class;
            case H2: return H2Dialect.class;
            default: case POSTGRESQL: return PostgreSQL82Dialect.class;
        }
    }
}
