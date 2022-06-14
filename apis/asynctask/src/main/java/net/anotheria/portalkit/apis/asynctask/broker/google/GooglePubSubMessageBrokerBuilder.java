package net.anotheria.portalkit.apis.asynctask.broker.google;

import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskConfig;
import net.anotheria.portalkit.apis.asynctask.broker.kafka.KafkaMessageBroker;
import net.anotheria.portalkit.apis.asynctask.broker.kafka.KafkaMessageBrokerBuilder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GooglePubSubMessageBrokerBuilder {

    private final List<AsyncTaskConfig> taskConfigs = new LinkedList<>();

    public static GooglePubSubMessageBrokerBuilder defaultBuilder() {
        return new GooglePubSubMessageBrokerBuilder();
    }

    public GooglePubSubMessageBrokerBuilder withTaskConfig(AsyncTaskConfig taskConfig) {
        taskConfigs.add(taskConfig);
        return this;
    }

    public GooglePubSubMessageBroker build() {
        Map<String, AsyncTaskConfig> taskConfigByType = new HashMap<>(taskConfigs.size());
        for (AsyncTaskConfig taskConfig : taskConfigs) {
            taskConfigByType.putIfAbsent(taskConfig.getTaskType(), taskConfig);
        }
        return new GooglePubSubMessageBroker(taskConfigByType);
    }

}
