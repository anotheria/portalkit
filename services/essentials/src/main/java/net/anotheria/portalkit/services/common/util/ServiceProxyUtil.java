package net.anotheria.portalkit.services.common.util;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.moskito.core.dynamic.MoskitoInvokationProxy;
import net.anotheria.moskito.core.dynamic.ProxyUtils;

/**
 * Utility which allow wrapping of any {@link Service} into {@link MoskitoInvokationProxy}.
 * <p> Note - useful for persistence services. </p>
 * 
 * @author h3llka
 */
public final class ServiceProxyUtil {

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
		final T result = ProxyUtils.createServiceInstance(implementation, producerId, category, subSystem, isLog, interfaceClazz, additionalInterfaces);
		return result;
	}

}
