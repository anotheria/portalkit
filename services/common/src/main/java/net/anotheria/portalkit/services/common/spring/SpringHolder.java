package net.anotheria.portalkit.services.common.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling multiple Spring contexts.
 *
 * @author bvanchuhov
 */
public final class SpringHolder {

    private SpringHolder() {}

    private static final String COMMON_CONTEXT = "CommonContext";
    private static Map<String, ApplicationContext> nameToContext = new HashMap<String, ApplicationContext>();

    static {
        register(COMMON_CONTEXT, new AnnotationConfigApplicationContext());
    }

    public static void register(Class<?> type, ApplicationContext applicationContext) {
        register(type.getName(), applicationContext);
    }

    public static void register(String name, ApplicationContext applicationContext) {
        nameToContext.put(name, applicationContext);
    }

    public static <T> T get(String contextName, Class<T> type) {
        ApplicationContext context = getContext(contextName);
        return context.getBean(type);
    }

    public static <T> T get(Class<T> type) {
        return get(type.getName(), type);
    }

    private static <T> ApplicationContext getContext(Class<T> type) {
        return getContext(type.getName());
    }

    private static <T> ApplicationContext getContext(String contextName) {
        return nameToContext.get(contextName);
    }
}
