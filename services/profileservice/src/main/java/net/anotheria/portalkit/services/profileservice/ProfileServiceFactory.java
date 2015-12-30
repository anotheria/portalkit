package net.anotheria.portalkit.services.profileservice;

import net.anotheria.anoprise.metafactory.AbstractParameterizedServiceFactory;

import java.io.Serializable;

/**
 * @author asamoilich.
 */
public class ProfileServiceFactory<T extends Profile> extends AbstractParameterizedServiceFactory<ProfileService<T>> {
    /**
     * Parameter name for entity class.<br>
     * Required.
     */
    public static final String PARAMETER_ENTITY_CLASS = "entityClass";

    /**
     * Parameter name for {@link ProfileServiceConfig} configuration name.<br>
     * Not required. If <code>null</code> default configuration name will be used.
     */
    public static final String PARAMETER_CONFIGURATION = "conf";

    /**
     * Parameter name for ConfigureMe {@link org.configureme.Environment} configuration.<br>
     * Not required. If <code>null</code> current system environment will be used.
     */
    public static final String PARAMETER_ENVIRONMENT = "env";

    @Override
    public ProfileService<T> create() {
        Serializable rawEntityClass = getParameterValue(PARAMETER_ENTITY_CLASS);
        if (rawEntityClass == null || !(rawEntityClass instanceof Class))
            throw new RuntimeException("Wrong entity class[" + rawEntityClass + "] configuration.");

        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) rawEntityClass;
        String conf = getParameterValueAsString(PARAMETER_CONFIGURATION);
        String env = getParameterValueAsString(PARAMETER_ENVIRONMENT);
        return new ProfileServiceImpl<T>(entityClass, conf, env);
    }

}
