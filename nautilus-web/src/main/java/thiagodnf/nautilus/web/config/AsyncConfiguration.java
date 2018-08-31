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

		executor.setCorePoolSize(105);
		executor.setMaxPoolSize(105);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("optimizing-");
		executor.initialize();

		return executor;
	}
}
