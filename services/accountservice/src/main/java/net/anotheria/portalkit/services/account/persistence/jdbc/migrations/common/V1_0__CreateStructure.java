package net.anotheria.portalkit.services.account.persistence.jdbc.migrations.common;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * V1_0__CreateStructure.
 *
 * @author lrosenberg
 * @since 06.01.13 01:12
 */
public class V1_0__CreateStructure implements JdbcMigration {

	@Override
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
		} catch(SQLException e){
			e.printStackTrace();
		}finally {
			statement.close();
		}
	}
}
