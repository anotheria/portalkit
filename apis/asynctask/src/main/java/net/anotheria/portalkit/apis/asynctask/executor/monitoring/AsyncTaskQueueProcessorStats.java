package net.anotheria.portalkit.apis.asynctask.executor.monitoring;

import net.anotheria.moskito.core.decorators.DecoratorRegistryFactory;
import net.anotheria.moskito.core.predefined.Constants;
import net.anotheria.moskito.core.producers.GenericStats;
import net.anotheria.moskito.core.stats.StatValue;
import net.anotheria.moskito.core.stats.impl.StatValueFactory;

/**
 * {@link GenericStats} for Async Executor processor.
 *
 * @author ynikonchuk
 */
public class AsyncTaskQueueProcessorStats extends GenericStats {

    //register decorator for this.
    static {
        DecoratorRegistryFactory.getDecoratorRegistry().addDecorator(AsyncTaskQueueProcessorStats.class, new AsyncTaskQueueProcessorStatsDecorator());
    }

    /**
     * Common {@link StatValue} for all async executor tasks.
     */
    private StatValue tasks;
    /**
     * {@link StatValue} for processed tasks by async executor.
     */
    private StatValue processedTasks;
    /**
     * {@link StatValue} for all errors with tasks in async executor.
     */
    private StatValue errorTasks;

    /**
     * Constructs an instance of GenericStats.
     *
     * @param aName
     *         name of the stats object.
     */
    public AsyncTaskQueueProcessorStats(String aName) {
        super(aName);

        tasks = StatValueFactory.createStatValue(0, "tasks", Constants.getDefaultIntervals());
        processedTasks = StatValueFactory.createStatValue(0, "processedTasks", Constants.getDefaultIntervals());
        errorTasks = StatValueFactory.createStatValue(0, "errorTasks", Constants.getDefaultIntervals());
    }

    public void incTasks() {
        tasks.increase();
    }

    public void incProcessedTasks() {
        processedTasks.increase();
    }

    public void incErrorTasks() {
        errorTasks.increase();
    }

    public long getTasks(String intervalName) {
        return tasks.getValueAsLong(intervalName);
    }

    public long getProcessedTasks(String intervalName) {
        return processedTasks.getValueAsLong(intervalName);
    }

    public long getErrorTasks(String intervalName) {
        return errorTasks.getValueAsLong(intervalName);
    }
}
