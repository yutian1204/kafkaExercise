package com.crazy.business.service;

import com.alibaba.fastjson.JSON;
import com.crazy.business.model.VisitInfo;
import com.crazy.common.producer.SimpleProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Author: crazy.jack
 * Date:   16-1-27
 */
@Service
public class VisitService {
    private static final Logger logger = LoggerFactory.getLogger(VisitService.class);

    @Resource
    private SimpleProducer simpleProducer;


    private String visitTopic = "ip.and.time";

    public void save(String ip) {
        VisitInfo visitInfo = new VisitInfo();
        visitInfo.setIp(ip);
        visitInfo.setTime(new Date());
        String message = JSON.toJSONString(visitInfo);
        simpleProducer.send(visitTopic, message);
    }
}
