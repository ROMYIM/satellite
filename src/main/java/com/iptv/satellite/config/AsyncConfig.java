package com.iptv.satellite.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * AsyncConfig
 */
@Configuration 
public class AsyncConfig extends AsyncConfigurerSupport{

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

	@Bean(name = "taskExecutor")
	@Override
	public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setThreadNamePrefix("Update-Schedule-");
		taskExecutor.setQueueCapacity(20);
        taskExecutor.setAwaitTerminationSeconds(30 * 60);
		return taskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExeptionHandler();
    }
    
    /**
	 * 生成任务调度者
	 * 设置线程池大小为2
	 * 把任务调度者声明为bean让spring管理
	 * @return 任务调度者实例
	 */
	@Bean("taskScheduler")
	public ThreadPoolTaskScheduler createTaskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(1);
		return taskScheduler;
	}

    /**
     * AsyncExeptionHandler
     */
    private class AsyncExeptionHandler implements AsyncUncaughtExceptionHandler{

		@Override
		public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            LOGGER.info("Exeception Message:" + ex.getMessage());
            LOGGER.info("Method:" + method.getName());
            for (Object param : params) {
                LOGGER.info("parameter value:" + param);
            }
		}
    
        
    }
}