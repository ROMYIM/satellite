package com.iptv.satellite.controller;

import javax.validation.Valid;

import com.iptv.satellite.domain.db.DataSourceBean;
import com.iptv.satellite.service.DataSourceService;
import com.iptv.satellite.service.TaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DataSourceController
 */
@Controller
@RequestMapping("/db")
public class DataSourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceController.class);

    private final DataSourceService dataSourceService;
    private final TaskService taskService;

    @Autowired
    public DataSourceController(DataSourceService dataSourceService, TaskService taskService) {
        this.dataSourceService = dataSourceService;
        this.taskService = taskService;
    }

    @RequestMapping("/")
    public String index() {
        LOGGER.info("db.html");
        return "db";
    }

    @RequestMapping("/add")
    public String addDataSource(@Valid DataSourceBean dataSource, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            LOGGER.info(dataSource.toString());
            dataSource.formatUrl();
            if (dataSourceService.addService(dataSource)) {
                if (taskService.resetTask()) {
                    return "redirect:/";
                }
            }
        } else {
            StringBuffer stringBuffer = new StringBuffer(36);
            for (ObjectError error : result.getAllErrors()) {
                stringBuffer.append(error.getDefaultMessage()).append('\n');
            }
            model.addAttribute("message", stringBuffer.toString());
            // redirectAttributes.addFlashAttribute("message", stringBuffer.toString());
        }
        return "redirect:/error";
    }
}