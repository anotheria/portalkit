package net.anotheria.portalkit.apis.asynctask.broker.kafka;

import com.google.gson.JsonParseException;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskMessageBroker;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskConfig;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTaskDeserializer;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTaskSerializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KafkaMessageBroker implements AsyncTaskMessageBroker {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageBroker.class);

    /**
     * Kafka producer.
     */
    private final Producer<String, String> producer;

    /**
     * Kafka consumers.
     */
    private final Map<String, Consumer<String, String>> consumers;

    private final Map<String, AsyncTaskConfig> taskConfigByType;

    private final ExecutorService executorService;


    KafkaMessageBroker(Map<String, AsyncTaskConfig> taskConfigByType) {
        this.taskConfigByType = taskConfigByType;

        KafkaConfig kafkaConfig = KafkaConfig.getInstance();

        Properties producerProps = new Properties();
        Properties consumerProps = new Properties();

        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getGroupId());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        producer = new KafkaProducer<>(producerProps);
        consumers = new ConcurrentHashMap<>();

        taskConfigByType.forEach((taskType, taskConfig) -> {
            Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
            consumer.subscribe(Collections.singletonList(taskType));
            consumers.put(taskType, consumer);
        });

        executorService = Executors.newFixedThreadPool(taskConfigByType.size());
    }

    @Override
    public void send(AsyncTask asyncTask) throws APIException {
        log.info("sendInternal({}) started", asyncTask.getTaskType());
        try {
            String value = serializeTask(asyncTask);
            ProducerRecord<String, String> record = new ProducerRecord<>(asyncTask.getTaskType(), String.valueOf(asyncTask.hashCode()), value);
            long time = System.currentTimeMillis();

            producer.send(record, ((recordMetadata, e) -> {

                long elapsedTime = System.currentTimeMillis() - time;

                if (recordMetadata != null) {
                    log.info("Sent record(key={} value={}), to topic={} metadata(partition={}, offset={}) elapsedTime={} ms\n",
                            record.key(),
                            record.value(),
                            asyncTask.getTaskType(),
                            recordMetadata.partition(),
                            recordMetadata.offset(),
                            elapsedTime);
                } else {
                    log.info("ERROR! Topic: {}. Record(key={} value={}), elapsedTime={} ms\n", asyncTask.getTaskType(), record.key(), record.value(), elapsedTime);
                    log.info("Exception: ", e);
                }
            }));
        } catch (Exception e) {
            throw new APIException(e.getMessage(), e);
        } finally {
            producer.flush();
        }
    }

    @Override
    public List<AsyncTask> getTasks(String topicName) throws APIException {
        List<AsyncTask> result = new LinkedList<>();
        AsyncTask task = getTopicTask(topicName);
        if (task != null)
            result.add(task);

        return result;
    }

    @Override
    public void notifyShutdown() {

    }

    private AsyncTask getTopicTask(String kafkaTopic) throws APIException {
        Consumer<String, String> consumer = consumers.get(kafkaTopic);
        synchronized (consumer) {
            log.debug("getTopicTask({})", kafkaTopic);
            ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);

            log.debug("polled {} records", consumerRecords.count());
            if (consumerRecords.isEmpty()) {
                return null;
            }

            String record = consumerRecords.iterator().next().value();

            if (record == null) {
                return null;
            }

            AsyncTask result = null;
            try {
                result = deserializeTask(kafkaTopic, record);
            } catch (JsonParseException | APIException e) {
                log.error("Unable to deserialize async task to topic=" + kafkaTopic + ". Record=" + record, e);
            }
            consumer.commitSync();
            return result;
        }
    }

    private String serializeTask(AsyncTask asyncTask) throws APIException {
        AsyncTaskConfig asyncTaskConfig = taskConfigByType.get(asyncTask.getTaskType());
        if (asyncTaskConfig == null) {
            throw new APIException("no asyncTaskConfig for task: " + asyncTask.getTaskType());
        }

        AsyncTaskSerializer serializer = asyncTaskConfig.getSerializer();
        if (serializer == null) {
            throw new APIException("no serializer for task: " + asyncTask.getTaskType());
        }
        return serializer.serialize(asyncTask);
    }

    private AsyncTask deserializeTask(String taskType, String body) throws APIException {
        AsyncTaskConfig asyncTaskConfig = taskConfigByType.get(taskType);
        if (asyncTaskConfig == null) {
            log.error("no asyncTaskConfig for task: " + taskType);
            return null;
        }

        AsyncTaskDeserializer deserializer = asyncTaskConfig.getDeserializer();
        if (deserializer == null) {
            log.error("no deserializer for task: " + taskType);
            return null;
        }

        return asyncTaskConfig.getDeserializer().deserialize(body);
    }
}
