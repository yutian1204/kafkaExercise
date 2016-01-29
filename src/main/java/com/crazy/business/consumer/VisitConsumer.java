package com.crazy.business.consumer;

import com.alibaba.fastjson.JSON;
import com.crazy.business.dao.VisitDao;
import com.crazy.business.model.VisitInfo;
import com.crazy.common.consumer.CommonConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Author: crazy.jack
 * Date:   16-1-27
 */
public class VisitConsumer implements CommonConsumer {
    private static final Logger logger = LoggerFactory.getLogger(VisitConsumer.class);

    @Resource
    private VisitDao visitDao;

    @Override
    public void execute(String message) {
        VisitInfo visitInfo = JSON.parseObject(message, VisitInfo.class);
        visitDao.insert(visitInfo);
    }
}
