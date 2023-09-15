package net.anotheria.portalkit.services.personaldata;

import net.anotheria.util.crypt.CryptTool;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterReConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurator for {@link PersonalDataService}.
 *
 * @author Vlad Lukjanenko
 */
@ConfigureMe(allfields = true, name = "pk-personal-data-mongo-config")
public final class PersonalDataServiceConfig {

    /**
     * {@link Logger} instance.
     */
    @DontConfigure
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalDataServiceConfig.class);

    /**
     * {@link PersonalDataServiceConfig} instance.
     * */
    @DontConfigure
    private static PersonalDataServiceConfig INSTANCE = null;

    /**
     * Decryption key for application secret.
     * */
    @DontConfigure
    private char[] configurationReadKey = {'z', 'F', 'P', (char) 121, 'T', 'b', (char) 97, 'a', (char) 5, (char) 71, 'W', 'n'};

    /**
     * Application secret.
     * */
    @Configure
    private String applicationSecret = "";

    /**
     * Mongo host.
     * */
    @Configure
    private String host = "127.0.0.1";

    /**
     * Mongo port.
     * */
    @Configure
    private int port = 27017;

    /**
     * Mongo database name.
     * */
    @Configure
    private String database = "baldur_m2m";

    /**
     * Connection string.
     */
    @Configure
    private String connectionString;


    /**
     * Default constructor.
     * */
    private PersonalDataServiceConfig() {
        try {
            ConfigurationManager.INSTANCE.configure(this);
        } catch (final IllegalArgumentException e) {
            LOGGER.warn("Configuration fail[" + e.getMessage() + "]. Relaying on defaults.");
        }

        CryptTool cryptTool = new CryptTool(new String(configurationReadKey));
        this.applicationSecret = cryptTool.decryptFromHexTrim(this.applicationSecret);
    }

    @AfterReConfiguration
    public void reconfigure() {
        CryptTool cryptTool = new CryptTool(new String(configurationReadKey));
        this.applicationSecret = cryptTool.decryptFromHexTrim(this.applicationSecret);
    }


    /**
     * Returns configured instance of {@link PersonalDataServiceConfig}.
     *
     * @return configured instance of {@link PersonalDataServiceConfig}.
     * */
    public static PersonalDataServiceConfig getInstance() {

        if (INSTANCE == null) {

            synchronized (PersonalDataServiceConfig.class) {

                if (INSTANCE == null) {
                    INSTANCE = new PersonalDataServiceConfig();
                }
            }
        }

        return INSTANCE;
    }


    public String getApplicationSecret() {
        return applicationSecret;
    }

    public void setApplicationSecret(String applicationSecret) {
        this.applicationSecret = applicationSecret;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public String toString() {
        return "PersonalDataServiceConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                '}';
    }
}
