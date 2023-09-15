package net.anotheria.portalkit.services.phoneverification;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.slf4j.LoggerFactory;

/**
 * Configurator for {@link PhoneVerificationService}.
 *
 * @author vberkunov
 */
@ConfigureMe(name = "pk-phone-verification-twilio-config")
public class PhoneVerificationConfig {

    /**
     * {@link PhoneVerificationConfig} instance.
     * */
    @DontConfigure
    private static PhoneVerificationConfig INSTANCE = null;

    /**
     * Twilio username.
     * */
    @Configure
    private String username;

    /**
     * Twilio password.
     * */
    @Configure
    private String password;

    /**
     * Twilio path service cid.
     * */
    @Configure
    private String pathServiceSid;

    public static synchronized PhoneVerificationConfig getInstance() {
        if (INSTANCE == null)
            INSTANCE = new PhoneVerificationConfig();

        return INSTANCE;
    }

    /**
     * Default constructor.
     */
    private PhoneVerificationConfig() {
        try {
            ConfigurationManager.INSTANCE.configure(this);
        } catch (Exception e) {
            LoggerFactory.getLogger(PhoneVerificationConfig.class).error("PhoneVerificationConfig() Configuration failed. Configuring with defaults.", e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPathServiceSid() {
        return pathServiceSid;
    }

    public void setPathServiceSid(String pathServiceSid) {
        this.pathServiceSid = pathServiceSid;
    }
}