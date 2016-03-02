package net.anotheria.portalkit.services.common.flyway;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FlywayUtilsTest {

    private static final String PACKAGE_NAME = "net.anotheria.service.person";
    private static final String DRIVER_NAME = "org.postgresql.Driver";

    @Test
    public void testGetDefaultFlywayLocations() {
        String[] defaultFlywayLocations = FlywayUtils.getDefaultFlywayLocations(PACKAGE_NAME, DRIVER_NAME);

        assertThat(defaultFlywayLocations, is(new String[]{
                "net.anotheria.service.person.migrations.common",
                "net.anotheria.service.person.migrations.postgresql"
        }));
    }
}