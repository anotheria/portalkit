package net.anotheria.portalkit.apis.asynctask.executor;

import net.anotheria.portalkit.apis.asynctask.executor.configuration.ExecutorProcessorConfiguration;

import java.util.HashMap;
import java.util.Map;

public final class AsyncTaskExecutorProcessorBuilder {

    /**
     * {@link ExecutorProcessorConfiguration} configuration for async-executor.
     */
    private ExecutorProcessorConfiguration executorProcessorConfiguration;
    /**
     * {@link AsyncTaskExecutor} async tasks executor.
     */
    private AsyncTaskExecutor asyncTaskExecutor;

    public AsyncTaskExecutorProcessorBuilder setExecutorProcessorConfiguration(ExecutorProcessorConfiguration executorProcessorConfiguration) {
        this.executorProcessorConfiguration = executorProcessorConfiguration;
        return this;
    }

    public AsyncTaskExecutorProcessorBuilder setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
        this.asyncTaskExecutor = asyncTaskExecutor;
        return this;
    }

    public AsyncTaskExecutorProcessor build() {
        if (executorProcessorConfiguration == null)
            throw new IllegalStateException("Async task executor processor configuration is null");

        if (asyncTaskExecutor == null)
            throw new IllegalStateException("Async task executor processor is null");

        switch (executorProcessorConfiguration.getExecutorProcessorType()) {
            case SINGLE:
                return new AsyncTaskSingleExecutorProcessor(asyncTaskExecutor, executorProcessorConfiguration);
            case MULTI_THREADED:
                return new AsyncTaskQueueExecutorProcessor(asyncTaskExecutor, executorProcessorConfiguration);
            default:
                throw new IllegalStateException("Wrong configuration");
        }
    }

}
