package thiagodnf.nautilus.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import thiagodnf.nautilus.web.listener.TrimStringMongoEventListener;

@Configuration
@EnableMongoAuditing
public class MongoDBConfiguration {
	
	@Bean
	public TrimStringMongoEventListener trimStringMongoEventListener() {
	    return new TrimStringMongoEventListener();
	}
}
