package thiagodnf.nautilus.web.config;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import thiagodnf.nautilus.web.dto.ExecutionQueueDTO;
import thiagodnf.nautilus.web.model.Execution;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

	@Bean
	public ExecutorService taskExecutor() {

	    return Executors.newFixedThreadPool(1);
	    
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
