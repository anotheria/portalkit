package net.anotheria.portalkit.services.common.flyway;

/**
 * @author bvanchuhov
 */
public final class FlywayUtils {

    private FlywayUtils() {}

    public static String[] getDefaultFlywayLocations(String packageName, String driverName) {
        return new String[] {
                packageName + ".migrations.common",
                packageName + ".migrations." + getDBType(driverName)
        };
    }

    private static String getDBType(String driverName) {
        if (driverName.contains("mysql")) return "mysql";
        if (driverName.contains("postgresql")) return "postgresql";
        if (driverName.contains("h2")) return "h2";
        return "psql";
    }

    public static String getDefaultTableNameForMigration(String serviceName) {
        return "flyway_" + serviceName;
    }
}
