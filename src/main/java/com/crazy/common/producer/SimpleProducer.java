package com.crazy.common.producer;

/**
 * Author: crazy.jack
 * Date:   16-1-27
 */
public interface SimpleProducer {

    void send(String topic, String message);

}
