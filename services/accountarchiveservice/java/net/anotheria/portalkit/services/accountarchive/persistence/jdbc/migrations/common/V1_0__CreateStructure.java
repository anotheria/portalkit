package net.anotheria.portalkit.services.accountarchive.persistence.jdbc.migrations.common;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;
import net.anotheria.portalkit.services.accountarchive.persistence.jdbc.AccountArchiveDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author VKoulakov
 * @since 22.04.14 13:47
 */
@SuppressWarnings("unused")
public class V1_0__CreateStructure implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        PreparedStatement ddl = connection.prepareStatement("CREATE TABLE " + AccountArchiveDAO.TABLE_NAME +
                "(id VARCHAR(128) NOT NULL," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "type integer," +
                "regts bigint," +
                "dao_created bigint," +
                "dao_updated bigint," +
                "status bigint," +
                "tenant VARCHAR(10)," +
                "deleted_at bigint NOT NULL," +
                "deleted_note varchar(255),"+
                "PRIMARY KEY(id)" +
                ")");
        try {
            ddl.execute();
        } finally {
            ddl.close();
        }
    }
}
