package org.nautilus.web.config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nautilus.web.dto.ExecutionQueueDTO;
import org.nautilus.web.model.Execution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

    @Value("${nThreads}")
    private int nThreads = 1;
    
	@Bean
	public ExecutorService taskExecutor() {
	    
	    log.info("nThreads: " + nThreads);

	    return Executors.newFixedThreadPool(nThreads);
	}
	
	@Bean
    public List<Execution> pendingExecutions() {
        return new CopyOnWriteArrayList<>();
    }
	
	@Bean
    public List<ExecutionQueueDTO> runningExecutions() {
        return new CopyOnWriteArrayList<>();
    }
}
