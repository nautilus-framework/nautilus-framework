package org.nautilus.web.config;

import java.util.LinkedList;
import java.util.List;
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

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

    @Value("${nThreads}")
    private int nThreads = 1;
    
	@Bean
	public ExecutorService taskExecutor() {
	    
	    System.out.println("nThreads: " + nThreads);

	    return Executors.newFixedThreadPool(nThreads);
	    
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//
//		// Number of thread running at the same time. After this value new ones will wait
//		executor.setCorePoolSize(2);
//		executor.setMaxPoolSize(5);
//		
//		// After this value, a new thread will be rejected
//		executor.setQueueCapacity(10);
//		executor.setThreadNamePrefix("optimizing-");
//		
//		executor.initialize();
//		
//		return executor;
	}
	
	@Bean
    public List<Execution> pendingExecutions() {
        return new LinkedList<>();
    }
	
	@Bean
    public List<ExecutionQueueDTO> runningExecutions() {
        return new LinkedList<>();
    }
}
