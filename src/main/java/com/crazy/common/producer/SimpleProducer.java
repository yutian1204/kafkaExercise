package com.crazy.common.producer;


public interface SimpleProducer {

    void send(String topic, String message);

}
