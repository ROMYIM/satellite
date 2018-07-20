package com.iptv.satellite.controller;

import com.iptv.satellite.domain.db.DataSourceBean;
import com.iptv.satellite.service.DataSourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DataSourceController
 */
@Controller
@RequestMapping("/db")
public class DataSourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceController.class);

    private final DataSourceService dataSourceService;

    @Autowired
    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @RequestMapping("/")
    public String index() {
        LOGGER.info("db.html");
        return "db";
    }

    @RequestMapping("/add")
    public String addDataSource(DataSourceBean model) {
        LOGGER.info(model.toString());
        model.formatUrl();
        if (dataSourceService.addServiceTest(model)) {
            if (!dataSourceService.runtimeServiceTest()) {
                return "redirect:/";
            }
        }
        return "success";
    }
}