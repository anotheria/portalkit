package net.anotheria.portalkit.services.foreignid.persistence.jdbc.migrations;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:12
 */
public class V1_0__CreateStructure implements JdbcMigration {
	public void migrate(Connection connection) throws Exception {
		PreparedStatement statement = connection.prepareStatement(
			"CREATE TABLE foreignid ("+
				"accid VARCHAR(128) NOT NULL,"+
				"sourceid integer NOT NULL,"+
				"foreignid VARCHAR(100) NOT NULL,"+
				"PRIMARY KEY(accid, sourceid, foreignid)"+
			");"
			);
		try {
			statement.execute();
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			statement.close();
		}
	}
}
