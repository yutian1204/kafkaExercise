package com.crazy.business.web.controller;

import com.crazy.business.model.DbConnectionModel;
import com.crazy.business.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
@Controller
@RequestMapping("/excel/")
public class ExcelController {
    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @Resource
    private DataService dataService;

    @RequestMapping("redirect")
    public String redirect() {
        return "excelUpload";
    }

    @RequestMapping("upload")
    public String upload(String host, Integer port, String db, String table, String user, String password, MultipartFile file) {
        DbConnectionModel dbConnectionModel = new DbConnectionModel();
        dbConnectionModel.setHost(host);
        dbConnectionModel.setPort(port);
        dbConnectionModel.setDb(db);
        dbConnectionModel.setTable(table);
        dbConnectionModel.setUser(user);
        dbConnectionModel.setPassword(password);
        dataService.insert(dbConnectionModel, file);
        return "success";
    }
}
