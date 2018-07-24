package com.iptv.satellite.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * ErrorController
 */
@RequestMapping("/error")
@RestController
public class ErrorController {

    @RequestMapping("/")
    public ModelAndView index(ModelAndView modelAndView, @RequestParam("message")String message) {
        modelAndView.addObject("message", message);
        modelAndView.setViewName("error");
        return modelAndView;
    }
}