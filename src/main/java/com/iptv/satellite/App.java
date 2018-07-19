package com.iptv.satellite;

import com.iptv.satellite.util.BeanUtil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 程序入口
 *
 */
@EntityScan("com.iptv.domain")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class App 
{
	public static void main( String[] args )
    {
        ApplicationContext context = SpringApplication.run(App.class, args);
        BeanUtil.initBeanFactory(context);
    }
}
