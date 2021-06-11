package net.anotheria.portalkit.apis.asynctask.executor;

import net.anotheria.moskito.core.decorators.AbstractDecorator;
import net.anotheria.moskito.core.decorators.value.LongValueAO;
import net.anotheria.moskito.core.decorators.value.StatValueAO;
import net.anotheria.moskito.core.stats.TimeUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AbstractDecorator} for {@link AsyncTaskQueueProcessorStats}.
 *
 * @author ynikonchuk
 */
public class AsyncTaskQueueProcessorStatsDecorator extends AbstractDecorator<AsyncTaskQueueProcessorStats> {
    /**
     * Captions.
     */
    private static final String CAPTIONS[] = {
            "Tasks",
            "Processed Tasks",
            "Error Tasks"
    };
    /**
     * Short explanations.
     */
    private static final String SHORT_EXPLANATIONS[] = CAPTIONS;
    /**
     * Explanations.
     */
    private static final String EXPLANATIONS[] = {
            "Total amount of tasks that were received by async executor",
            "Processed tasks by async executor",
            "Errors count of tasks in async executor"
    };

    /**
     * Creates a new decorator for {@link AsyncTaskQueueProcessorStats}.
     */
    public AsyncTaskQueueProcessorStatsDecorator() {
        super("AsyncExecutorProcessor", CAPTIONS, SHORT_EXPLANATIONS, EXPLANATIONS);
    }

    @Override
    public List<StatValueAO> getValues(AsyncTaskQueueProcessorStats stats, String interval, TimeUnit unit) {
        List<StatValueAO> ret = new ArrayList<>(CAPTIONS.length);
        int i = 0;
        long totalSales = stats.getTasks(interval);
        ret.add(new LongValueAO(CAPTIONS[i++], totalSales));
        ret.add(new LongValueAO(CAPTIONS[i++], stats.getProcessedTasks(interval)));
        ret.add(new LongValueAO(CAPTIONS[i], stats.getErrorTasks(interval)));
        return ret;
    }
}
