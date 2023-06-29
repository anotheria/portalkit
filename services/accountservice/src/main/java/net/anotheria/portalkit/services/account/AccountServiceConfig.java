package net.anotheria.portalkit.services.account;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.LoggerFactory;

/**
 * Configuration for account service.
 *
 * @author lrosenberg
 * @since 13.12.12 16:04
 */

@ConfigureMe(name = "account-service-configuration")
public final class AccountServiceConfig {
	/**
	 * If true only one account with same email address is allowed. Default true.
	 */
    @Configure
	private boolean exclusiveMail = true;
	/**
	 * If true only one account with same name is allowed. Default true.
	 */
    @Configure
	private boolean exclusiveName = true;
    /**
     * If true all status changes for account will be stored. Default false.
     */
    @Configure
    private boolean auditEnabled = false;
    /**
     * If true we can use same email and account name for different brands.
     */
    @Configure
    private boolean brandEnabled = false;
    /**
     * Default brand name.
     */
    @Configure
    private String defaultBrand = null;

    /**
     * Config instance.
     */
    private static volatile AccountServiceConfig instance;
    /**
     * Monitor instance.
     */
    private static final Object LOCK = new Object();

    /**
     * Return {@link AccountServiceConfig} configured with data from  'account-service-configuration' config.
     * @return {@link AccountServiceConfig}
     */
    public static AccountServiceConfig getInstance() {
        if (instance != null)
            return instance;
        synchronized (LOCK) {
            if (instance != null)
                return instance;
            instance = new AccountServiceConfig();
            try {
                ConfigurationManager.INSTANCE.configure(instance);
            } catch (Exception e) {
                LoggerFactory.getLogger(AccountServiceConfig.class).warn("Configuration failed, relying on defaults. " + e.getMessage());
            }
            return instance;
        }
    }

    /**
     * Constructor.
     */
    private AccountServiceConfig() {
    }

    public boolean isExclusiveMail() {
		return exclusiveMail;
	}

	public void setExclusiveMail(boolean exclusiveMail) {
		this.exclusiveMail = exclusiveMail;
	}

	public boolean isExclusiveName() {
		return exclusiveName;
	}

	public void setExclusiveName(boolean exclusiveName) {
		this.exclusiveName = exclusiveName;
	}

    public boolean isAuditEnabled() {
        return auditEnabled;
    }

    public void setAuditEnabled(boolean auditEnabled) {
        this.auditEnabled = auditEnabled;
    }

    public boolean isBrandEnabled() {
        return brandEnabled;
    }

    public void setBrandEnabled(boolean brandEnabled) {
        this.brandEnabled = brandEnabled;
    }

    public String getDefaultBrand() {
        return defaultBrand;
    }

    public void setDefaultBrand(String defaultBrand) {
        this.defaultBrand = defaultBrand;
    }

    public static void setInstance(AccountServiceConfig instance) {
        AccountServiceConfig.instance = instance;
    }

    @Override
    public String toString() {
        return "AccountServiceConfig{" +
                "exclusiveMail=" + exclusiveMail +
                ", exclusiveName=" + exclusiveName +
                ", auditEnabled=" + auditEnabled +
                ", brandEnabled=" + brandEnabled +
                ", defaultBrand='" + defaultBrand + '\'' +
                '}';
    }
}
