package net.anotheria.portalkit.services.account.persistence.jdbc.migrations;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:12
 */
public class Vaccount_1__CreateStructure implements JdbcMigration {
	public void migrate(Connection connection) throws Exception {
		PreparedStatement statement = connection.prepareStatement(
			"CREATE TABLE account ("+
				"id VARCHAR(128) NOT NULL,"+
				"name VARCHAR(100) NOT NULL,"+
				"email VARCHAR(100) NOT NULL,"+
				"type integer,"+
				"regts bigint,"+
				"PRIMARY KEY(id)"+
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
