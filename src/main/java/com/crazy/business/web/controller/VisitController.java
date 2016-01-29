package com.crazy.business.web.controller;

import com.crazy.business.service.VisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
@Controller
@RequestMapping("/")
public class VisitController {
    private static final Logger logger = LoggerFactory.getLogger(VisitController.class);

    @Resource
    private VisitService visitService;

    @RequestMapping("redirect")
    public String redirect() {
        return "excelUpload";
    }

    @RequestMapping("index")
    public String upload(HttpServletRequest httpRequest) {
        String remoteAddress = httpRequest.getRemoteAddr();
        visitService.save(remoteAddress);
        return "success";
    }
}
