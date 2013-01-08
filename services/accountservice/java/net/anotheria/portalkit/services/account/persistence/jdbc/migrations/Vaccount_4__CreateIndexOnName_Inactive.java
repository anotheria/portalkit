package net.anotheria.portalkit.services.account.persistence.jdbc.migrations;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:12
 */
public class Vaccount_4__CreateIndexOnName_Inactive implements JdbcMigration {
	public void migrate(Connection connection) throws Exception {
/*		PreparedStatement statement = connection.prepareStatement("CREATE UNIQUE INDEX name_idx ON account (name);");
		try {
			statement.execute();
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			statement.close();
		}
*/
	}
}
