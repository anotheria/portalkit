package net.anotheria.portalkit.services.online.persistence.jdbc.migrations.common;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;
import net.anotheria.portalkit.services.online.persistence.jdbc.ActivityDAO;
import net.anotheria.util.log.LogMessageUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * V-1.0  -- Activity persistence initializer.
 * Execute table creation and index add.
 *
 * @author h3llka
 */
//CHECKSTYLE:OFF
public class V1_0__InitialiseActivityPersistence implements JdbcMigration {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(V1_0__InitialiseActivityPersistence.class);

    @Override
    public void migrate(Connection connection) throws Exception {

        List<String> activityDDL = ActivityDAO.getDDL();
        if (activityDDL == null || activityDDL.isEmpty())
            throw new RuntimeException("ActivityDAO DDL  misconfiguration detected!");
        Statement st = null;
        boolean isAutoCommitEnabled = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            st = connection.createStatement();

            for (String ddlQuery : activityDDL)
                st.executeUpdate(ddlQuery);

            connection.commit();
        } catch (SQLException e) {
            String message = LogMessageUtil.failMsg(e, connection);
            LOGGER.fatal(message, e);
            throw new RuntimeException(message, e);
        } finally {
            connection.setAutoCommit(isAutoCommitEnabled);
            if (st != null)
                st.close();
        }
    }


}
