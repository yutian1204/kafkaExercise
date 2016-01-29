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

/**
 * Author: crazy.jack
 * Date:   16-1-27
 */
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
    @Resource
    private Map<String, CommonConsumer> consumerMap;

    private KafkaConsumer<String, String> consumer;

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
        while (true) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("offset=[{}], key=[{}], value=[{}]", record.offset(), record.key(), record.value());
                    CommonConsumer commonConsumer = consumerMap.get(record.topic());
                    commonConsumer.execute(record.value());
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}