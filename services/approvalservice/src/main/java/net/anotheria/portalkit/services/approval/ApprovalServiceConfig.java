package net.anotheria.portalkit.services.approval;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Config for approval service.
 *
 * @author ykalapusha
 */
@ConfigureMe(name = "approval-service-config")
public class ApprovalServiceConfig implements Serializable {
    /**
     * {@link Logger} instance.
     */
    @DontConfigure
    public static final Logger LOGGER = LoggerFactory.getLogger(ApprovalServiceConfig.class);
    /**
     * Serial version UID.
     */
    @DontConfigure
    private static final long serialVersionUID = 3013519155980718706L;
    /**
     * Synchronization lock.
     */
    @DontConfigure
    private static final Object LOCK = new Object();
    /**
     * {@link ApprovalServiceConfig} configured instance.
     */
    @DontConfigure
    private static volatile ApprovalServiceConfig instance;
    /**
     * Is available function for lock tickets for agent.
     */
    @Configure
    private boolean agentTicketsLockEnabled = true;

    /**
     * Private Constructor.
     */
    private ApprovalServiceConfig(){
        try {
            ConfigurationManager.INSTANCE.configure(this);
        } catch (final IllegalArgumentException e){
            LOGGER.warn("ApprovalServiceConfig() configuration fail [" + e.getMessage() + "]. Relaying on defaults [" + this.toString() + "]" );
        }
    }

    /**
     * Get configuration instance.
     *
     * @return {@link ApprovalServiceConfig} instance
     */
    public static ApprovalServiceConfig getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ApprovalServiceConfig();
                }
            }
        }
        return instance;
    }

    public boolean isAgentTicketsLockEnabled() {
        return agentTicketsLockEnabled;
    }

    public void setAgentTicketsLockEnabled(boolean agentTicketsLockEnabled) {
        this.agentTicketsLockEnabled = agentTicketsLockEnabled;
    }

    @Override
    public String toString() {
        return "ApprovalServiceConfig{" +
                "agentTicketsLockEnabled=" + agentTicketsLockEnabled +
                '}';
    }
}
