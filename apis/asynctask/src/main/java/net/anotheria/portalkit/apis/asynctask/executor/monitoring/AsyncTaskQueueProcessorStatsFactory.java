package net.anotheria.portalkit.apis.asynctask.executor.monitoring;

import net.anotheria.moskito.core.dynamic.IOnDemandStatsFactory;

/**
 * {@link AsyncTaskQueueProcessorStats} factory.
 *
 * @author ynikonchuk
 */
public class AsyncTaskQueueProcessorStatsFactory implements IOnDemandStatsFactory<AsyncTaskQueueProcessorStats> {
    @Override
    public AsyncTaskQueueProcessorStats createStatsObject(String name) {
        return new AsyncTaskQueueProcessorStats(name);
    }
}
