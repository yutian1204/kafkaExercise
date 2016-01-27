package com.crazy.common.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Properties;

/**
 * Author: crazy.jack
 * Date:   16-1-27
 */
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
        Properties props = new Properties();
        props.put("bootstrap.servers", serverList);
        props.put("acks", ack);
        props.put("retries", retries);
        props.put("batch.size", batchSize);
        props.put("linger.ms", lingerMs);
        props.put("buffer.memory", bufferMemory);
        props.put("key.serializer", keySerializer);
        props.put("value.serializer", valueSerializer);

        producer = new KafkaProducer<String, String>(props);
    }

    public void setServerList(String serverList) {
        this.serverList = serverList;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public void setLingerMs(Integer lingerMs) {
        this.lingerMs = lingerMs;
    }

    public void setBufferMemory(Integer bufferMemory) {
        this.bufferMemory = bufferMemory;
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
