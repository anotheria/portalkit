package net.anotheria.portalkit.apis.asynctask.executor;

import java.util.HashMap;
import java.util.Map;

public final class AsyncTaskQueueProcessorBuilder {

    private final Map<String, AsyncTaskExecutor> executorByTaskType = new HashMap<>();

    public AsyncTaskQueueProcessorBuilder registerExecutor(String asyncTaskType, AsyncTaskExecutor executor) {
        executorByTaskType.put(asyncTaskType, executor);
        return this;
    }

    public AsyncTaskQueueProcessor build() {
        return new AsyncTaskQueueProcessor(executorByTaskType);
    }

}
