package net.anotheria.portalkit.services.common.util;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.moskito.core.dynamic.MoskitoInvokationProxy;
import net.anotheria.moskito.core.logging.DefaultStatsLogger;
import net.anotheria.moskito.core.logging.IntervalStatsLogger;
import net.anotheria.moskito.core.logging.Log4JOutput;
import net.anotheria.moskito.core.predefined.ServiceStatsCallHandler;
import net.anotheria.moskito.core.predefined.ServiceStatsFactory;
import net.anotheria.moskito.core.stats.DefaultIntervals;
import org.apache.log4j.Logger;

/**
 * Utility which allow wrapping of any {@link Service} into  {@link MoskitoInvokationProxy}.
 * <p/>
 * Note - useful for persistence services.
 *
 * @author h3llka
 */
public final class ServiceProxyUtil {

    /**
     * Moskito default logger name.
     */
    private static final String MOSKITO_DEFAULT = "MoskitoDefault";
    /**
     * Moskito 5m logger name.
     */
    private static final String MOSKITO_5_M = "Moskito5m";
    /**
     * Moskito 15m logger name.
     */
    private static final String MOSKITO_15_M = "Moskito15m";
    /**
     * Moskito 1h logger name.
     */
    private static final String MOSKITO_1_H = "Moskito1h";
    /**
     * Moskito 1d logger name.
     */
    private static final String MOSKITO_1_D = "Moskito1d";
    /**
     * Default category for persistence services.
     */
    private static final String CATEGORY = "persistence-service";
    /**
     * Default subSystem for persistence services.
     */
    private static final String SUBSYSTEM = "persistence";

    /**
     * Create proxy.
     *
     * @param clazz    service class itself
     * @param instance class instance
     * @param <T>      type parameter
     * @return service instance
     */
    public static <T extends Service> T createPersistenceServiceProxy(Class<T> clazz, T instance) {
        return createServiceProxy(clazz, instance, CATEGORY, SUBSYSTEM, true);

    }

    /**
     * Create service proxy.
     *
     * @param clazz     service class
     * @param instance  instance of the class
     * @param category  category to use for moskito
     * @param subSystem subsystem to use
     * @param isLog     is log enabled
     * @param <T>       type param
     * @return created service instance  wrapped with proxy
     */
    public static <T extends Service> T createServiceProxy(Class<T> clazz, T instance, final String category, final String subSystem, final boolean isLog) {
        MoskitoInvokationProxy proxy = new MoskitoInvokationProxy(instance, new ServiceStatsCallHandler(), new ServiceStatsFactory(),
                clazz.getSimpleName(), category, subSystem, clazz,
                net.anotheria.anoprise.metafactory.Service.class);

        //noinspection unchecked
        instance = (T) proxy.createProxy();
        if (isLog) {
            new DefaultStatsLogger(proxy.getProducer(), new Log4JOutput(Logger.getLogger(MOSKITO_DEFAULT)));
            new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.FIVE_MINUTES, new Log4JOutput(Logger.getLogger(MOSKITO_5_M)));
            new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.FIFTEEN_MINUTES, new Log4JOutput(Logger.getLogger(MOSKITO_15_M)));
            new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.ONE_HOUR, new Log4JOutput(Logger.getLogger(MOSKITO_1_H)));
            new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.ONE_DAY, new Log4JOutput(Logger.getLogger(MOSKITO_1_D)));

        }
        return instance;
    }

    /**
     * Constructor.
     */
    private ServiceProxyUtil() {
        throw new IllegalAccessError("Can't be instantiated");
    }
}
