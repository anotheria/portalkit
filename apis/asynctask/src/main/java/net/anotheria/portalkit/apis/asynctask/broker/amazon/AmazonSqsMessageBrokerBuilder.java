package net.anotheria.portalkit.apis.asynctask.broker.amazon;

import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class AmazonSqsMessageBrokerBuilder {

    private final List<AsyncTaskConfig> taskConfigs = new LinkedList<>();

    public static AmazonSqsMessageBrokerBuilder defaultBuilder() {
        return new AmazonSqsMessageBrokerBuilder();
    }

    public AmazonSqsMessageBrokerBuilder withTaskConfig(AsyncTaskConfig taskConfig) {
        taskConfigs.add(taskConfig);
        return this;
    }

    public AmazonSqsMessageBroker build() {
        Map<String, AsyncTaskConfig> taskConfigByType = new HashMap<>(taskConfigs.size());
        for (AsyncTaskConfig taskConfig : taskConfigs) {
            taskConfigByType.putIfAbsent(taskConfig.getTaskType(), taskConfig);
        }
        return new AmazonSqsMessageBroker(taskConfigByType);
    }
}
