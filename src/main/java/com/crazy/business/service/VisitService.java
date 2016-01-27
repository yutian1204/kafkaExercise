package com.crazy.business.service;

import com.alibaba.fastjson.JSON;
import com.crazy.business.dao.VisitDao;
import com.crazy.business.model.VisitInfo;
import com.crazy.common.producer.SimpleProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    private VisitDao visitDao;
    @Resource
    private SimpleProducer simpleProducer;

    @Value("${visit.topic}")
    private String visitTopic;

    public void save(String ip) {
        VisitInfo visitInfo = new VisitInfo();
        visitInfo.setIp(ip);
        visitInfo.setTime(new Date());
        visitDao.insert(visitInfo);
        String message = JSON.toJSONString(visitInfo);
        simpleProducer.send(visitTopic, message);
    }
}
