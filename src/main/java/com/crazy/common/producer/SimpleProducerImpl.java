package com.crazy.common.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Properties;


public class SimpleProducerImpl implements InitializingBean, SimpleProducer {
    private static final Logger logger = LoggerFactory.getLogger(SimpleProducerImpl.class);

    private String serverList;
    private String ack;
    private Integer retries;
    private Integer batchSize;
    private Integer lingerMs;
    private Integer bufferMemory;
    private String keySerializer;
    private String valueSerializer;

    private KafkaProducer<String, String> producer;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            logger.warn("Loading simple producer start!");
            Properties props = new Properties();
            props.put("bootstrap.servers", serverList);
            props.put("key.serializer", keySerializer);
            props.put("value.serializer", valueSerializer);
            props.put("acks", ack);
            props.put("buffer.memory", bufferMemory);
            props.put("retries", retries);
            props.put("batch.size", batchSize);
            props.put("linger.ms", lingerMs);

            producer = new KafkaProducer<String, String>(props);
            logger.warn("Loading simple producer finish!");
        } catch (Exception e) {
            logger.error("Loading simple producer exception!", e);
            throw new RuntimeException("");
        }
    }

    public void setServerList(String serverList) {
        this.serverList = serverList;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public void setRetries(String retries) {
        this.retries = Integer.valueOf(retries);
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = Integer.valueOf(batchSize);
    }

    public void setLingerMs(String lingerMs) {
        this.lingerMs = Integer.valueOf(lingerMs);
    }

    public void setBufferMemory(String bufferMemory) {
        this.bufferMemory = Integer.valueOf(bufferMemory);
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void send(String topic, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
        producer.send(record);
    }
}
