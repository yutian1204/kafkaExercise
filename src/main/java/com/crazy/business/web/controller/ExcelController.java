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
@RequestMapping("/excel/")
public class ExcelController {
    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @Resource
    private VisitService visitService;

    @RequestMapping("redirect")
    public String redirect() {
        return "excelUpload";
    }

    @RequestMapping("upload")
    public String upload(HttpServletRequest httpRequest) {
        String remoteAddress = httpRequest.getRemoteAddr();
        visitService.save(remoteAddress);
        return "success";
    }
}
