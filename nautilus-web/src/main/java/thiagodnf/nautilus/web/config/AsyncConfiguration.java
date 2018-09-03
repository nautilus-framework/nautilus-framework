package thiagodnf.nautilus.web.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

	@Bean
	public Executor taskExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		// Number of thread running at the same time. After this value new ones will wait
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(5);
		// After this value, a new thread will be rejected
		executor.setQueueCapacity(10);
		executor.setThreadNamePrefix("optimizing-");
		
		executor.initialize();

		return executor;
	}
}
