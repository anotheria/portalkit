package net.anotheria.portalkit.apis.asynctask.executor.configuration;

/**
 * Type of Async-Executor.
 *
 * @author ykalapusha
 */
public enum ExecutorProcessorType {
    /**
     * Single executor means, that all task will be processed in single-threaded environment.
     */
    SINGLE,
    /**
     * Executor that processes tasks in a multi-threaded environment.
     */
    MULTI_THREADED
}
