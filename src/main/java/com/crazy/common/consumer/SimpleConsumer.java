package com.crazy.common.consumer;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;


@Component
public class SimpleConsumer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);

    @Value("${consumer.bootstrap.servers}")
    private String serverList;
    @Value("${consumer.group.id}")
    private String groupId;
    @Value("${consumer.enable.auto.commit}")
    private String enableAutoCommit;
    @Value("${consumer.auto.commit.interval.ms}")
    private String autoCommitIntervalMs;
    @Value("${consumer.session.timeout.ms}")
    private String sessionTimeoutMs;
    @Value("${consumer.key.deserializer}")
    private String keyDeserializer;
    @Value("${consumer.value.deserializer}")
    private String valueDeserializer;

    private final static ScheduledExecutorService SINGLE_THREAD_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    private static final ExecutorService WORK_THREADS = Executors.newFixedThreadPool(3, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    });

    private KafkaConsumer<String, String> consumer;

    @Resource
    private Map<String, CommonConsumer> consumerMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            logger.warn("Loading simple consumer start!");
            Properties props = new Properties();
            props.put("bootstrap.servers", serverList);
            props.put("group.id", groupId);
            props.put("enable.auto.commit", enableAutoCommit);
            props.put("auto.commit.interval.ms", autoCommitIntervalMs);
            props.put("session.timeout.ms",sessionTimeoutMs);
            props.put("key.deserializer", keyDeserializer);
            props.put("value.deserializer", valueDeserializer);
            consumer = new KafkaConsumer<String, String>(props);
            consumer.subscribe(Lists.newArrayList(consumerMap.keySet()));
            logger.warn("Loading simple consumer finish!");
        } catch (Exception e) {
            logger.error("Loading simple consumer exception!", e);
            throw new RuntimeException("");
        }

        SINGLE_THREAD_EXECUTOR.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    ConsumerRecords<String, String> records = consumer.poll(100);
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("offset=[{}], key=[{}], value=[{}]", record.offset(), record.key(), record.value());
                        WORK_THREADS.submit((Runnable) () -> {
                            CommonConsumer commonConsumer = consumerMap.get(record.topic());
                            commonConsumer.execute(record.value());
                        });
                    }
                } catch (Exception e) {
                    logger.error("", e);
                    throw new RuntimeException("Fuck the exception!");
                }
            }
        });

    }
}
