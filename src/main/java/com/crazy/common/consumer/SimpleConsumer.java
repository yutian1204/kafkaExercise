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

    @Value("${}")
    private String serverList;
    @Value("${}")
    private String groupId;
    @Value("${}")
    private String enableAutoCommit;
    @Value("${}")
    private String autoCommitIntervalMs;
    @Value("${}")
    private String sessionTimeoutMs;
    @Value("${}")
    private String keyDeserializer;
    @Value("${}")
    private String valueDeserializer;
    @Resource
    private Map<String, CommonConsumer> consumerMap;

    private KafkaConsumer<String, String> consumer;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
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
        } catch (Exception e) {
            logger.error("", e);
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
