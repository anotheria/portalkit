package net.anotheria.portalkit.apis.asynctask.broker.kafka;

import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class KafkaMessageBrokerBuilder {

    private final List<AsyncTaskConfig> taskConfigs = new LinkedList<>();

    public static KafkaMessageBrokerBuilder defaultBuilder() {
        return new KafkaMessageBrokerBuilder();
    }

    public KafkaMessageBrokerBuilder withTaskConfig(AsyncTaskConfig taskConfig) {
        taskConfigs.add(taskConfig);
        return this;
    }

    public KafkaMessageBroker build() {
        Map<String, AsyncTaskConfig> taskConfigByType = new HashMap<>(taskConfigs.size());
        for (AsyncTaskConfig taskConfig : taskConfigs) {
            taskConfigByType.putIfAbsent(taskConfig.getTaskType(), taskConfig);
        }
        return new KafkaMessageBroker(taskConfigByType);
    }
}
