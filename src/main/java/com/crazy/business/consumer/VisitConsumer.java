package com.crazy.business.consumer;

import com.crazy.common.consumer.CommonConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Author: crazy.jack
 * Date:   16-1-27
 */
@Component
public class VisitConsumer implements CommonConsumer {
    private static final Logger logger = LoggerFactory.getLogger(VisitConsumer.class);

    @Override
    public void execute(String message) {

    }
}
