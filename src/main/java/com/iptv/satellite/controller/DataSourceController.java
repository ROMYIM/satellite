package com.iptv.satellite.controller;

import com.iptv.satellite.domain.model.DataSourceModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * DataSourceController
 */
@RestController
@RequestMapping("/db")
public class DataSourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceController.class);

    @RequestMapping("/")
    public ModelAndView index(ModelAndView mView) {
        LOGGER.info("db.html");
        mView.setViewName("db");
        return mView;
    }

    @RequestMapping("/add")
    public String addDataSource(DataSourceModel model) {
        LOGGER.info(model.toString());
        model.formatUrl();
        return "success";
    }
}