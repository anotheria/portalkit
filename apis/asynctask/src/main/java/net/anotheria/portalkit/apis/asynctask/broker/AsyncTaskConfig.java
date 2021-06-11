package net.anotheria.portalkit.apis.asynctask.broker;

import net.anotheria.portalkit.apis.asynctask.task.AsyncTaskDeserializer;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTaskSerializer;

import java.util.Objects;

/**
 * Async task configuration for message broker.
 * It contains info how task should serialized/deserialized.
 */
public final class AsyncTaskConfig {
    /**
     * Async task type.
     */
    private final String taskType;
    /**
     * Async task deserializer.
     */
    private final AsyncTaskDeserializer deserializer;
    /**
     * Async task serializer.
     */
    private final AsyncTaskSerializer serializer;


    public AsyncTaskConfig(String taskType, AsyncTaskDeserializer deserializer, AsyncTaskSerializer serializer) {
        this.taskType = taskType;
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    public String getTaskType() {
        return taskType;
    }

    public AsyncTaskDeserializer getDeserializer() {
        return deserializer;
    }

    public AsyncTaskSerializer getSerializer() {
        return serializer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsyncTaskConfig that = (AsyncTaskConfig) o;
        return Objects.equals(taskType, that.taskType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskType);
    }

    @Override
    public String toString() {
        return "AsyncTaskConfig{" +
                "taskType='" + taskType + '\'' +
                ", deserializer=" + deserializer +
                ", serializer=" + serializer +
                '}';
    }
}
