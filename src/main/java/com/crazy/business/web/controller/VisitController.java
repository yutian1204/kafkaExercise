package com.crazy.business.web.controller;

import com.crazy.business.service.VisitService;
import com.crazy.common.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/")
public class VisitController {
    private static final Logger logger = LoggerFactory.getLogger(VisitController.class);

    @Resource
    private VisitService visitService;

    @RequestMapping("index.do")
    public String upload(HttpServletRequest httpRequest) {
        visitService.save(IpUtil.getPeerIp(httpRequest));
        return "success";
    }
}
