package net.anotheria.portalkit.services.common.util;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.moskito.core.dynamic.MoskitoInvokationProxy;
import net.anotheria.moskito.core.logging.DefaultStatsLogger;
import net.anotheria.moskito.core.logging.IntervalStatsLogger;
import net.anotheria.moskito.core.logging.SLF4JLogOutput;
import net.anotheria.moskito.core.predefined.ServiceStatsCallHandler;
import net.anotheria.moskito.core.predefined.ServiceStatsFactory;
import net.anotheria.moskito.core.stats.DefaultIntervals;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility which allow wrapping of any {@link Service} into {@link MoskitoInvokationProxy}.
 * <p> Note - useful for persistence services. </p>
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
	 * Constructor.
	 */
	private ServiceProxyUtil() {
		throw new IllegalAccessError("Can't be instantiated");
	}

	/**
	 * Create proxy.
	 * 
	 * @param <T>
	 *            type parameter
	 * @param clazz
	 *            service class itself
	 * @param instance
	 *            class instance
	 * @param additionalInterfaces
	 *            interfaces which can be called from outside
	 * @return service instance
	 */
	public static <T extends Service> T createPersistenceServiceProxy(final Class<T> clazz, final T instance, final Class<?>... additionalInterfaces) {
		return createServiceProxy(clazz, instance, CATEGORY, SUBSYSTEM, true, additionalInterfaces);

	}

	/**
	 * Create service proxy.
	 * 
	 * @param <T>
	 *            type param
	 * @param clazz
	 *            service class
	 * @param instance
	 *            instance of the class
	 * @param category
	 *            category to use for moskito
	 * @param subSystem
	 *            subsystem to use
	 * @param isLog
	 *            is log enabled
	 * @param additionalInterfaces
	 *            interfaces which can be called from outside
	 * @return created service instance wrapped with proxy
	 */
	public static <T extends Service> T createServiceProxy(final Class<T> clazz, final T instance, final String category, final String subSystem,
			final boolean isLog, final Class<?>... additionalInterfaces) {
		return createMoskitoProxy(clazz, instance, clazz.getSimpleName(), category, subSystem, isLog, additionalInterfaces);
	}

	/**
	 * Create moskito proxy.
	 * 
	 * @param <T>
	 *            type
	 * @param interfaceClazz
	 *            interface class
	 * @param implementation
	 *            implementation instance
	 * @param producerId
	 *            producer id
	 * @param category
	 *            category
	 * @param subSystem
	 *            subsystem
	 * @param isLog
	 *            is need log statistic
	 * @param additionalInterfaces
	 *            interfaces which can be called from outside
	 * @return proxy
	 */
	public static <T extends Service> T createMoskitoProxy(final Class<T> interfaceClazz, final T implementation, final String producerId,
			final String category, final String subSystem, final boolean isLog, final Class<?>... additionalInterfaces) {
		final List<Class<?>> interfaces = new ArrayList<Class<?>>();
		interfaces.add(interfaceClazz);
		if (additionalInterfaces != null && additionalInterfaces.length != 0)
			interfaces.addAll(Arrays.asList(additionalInterfaces));
		interfaces.add(Service.class);

		final MoskitoInvokationProxy proxy = new MoskitoInvokationProxy(implementation, new ServiceStatsCallHandler(), new ServiceStatsFactory(), producerId,
				category, subSystem, interfaces.toArray(new Class<?>[interfaces.size()]));

		final T result = interfaceClazz.cast(proxy.createProxy());

		if (isLog) {
			new DefaultStatsLogger(proxy.getProducer(), new SLF4JLogOutput(LoggerFactory.getLogger(MOSKITO_DEFAULT)));
			new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.FIVE_MINUTES, new SLF4JLogOutput(LoggerFactory.getLogger(MOSKITO_5_M)));
			new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.FIFTEEN_MINUTES, new SLF4JLogOutput(LoggerFactory.getLogger(MOSKITO_15_M)));
			new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.ONE_HOUR, new SLF4JLogOutput(LoggerFactory.getLogger(MOSKITO_1_H)));
			new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.ONE_DAY, new SLF4JLogOutput(LoggerFactory.getLogger(MOSKITO_1_D)));
		}

		return result;
	}

}
