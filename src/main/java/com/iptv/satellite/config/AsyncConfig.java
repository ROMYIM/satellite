package com.iptv.satellite.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * AsyncConfig
 */
@Configuration 
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

	@Override
	public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setThreadNamePrefix("Update-Schedule-");
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(30 * 60);
        taskExecutor.initialize();
		return taskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExeptionHandler();
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