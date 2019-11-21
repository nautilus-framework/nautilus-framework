package org.nautilus.web.config;

import org.nautilus.web.listener.TrimStringMongoEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoDBConfiguration {
	
	@Bean
	public TrimStringMongoEventListener trimStringMongoEventListener() {
	    return new TrimStringMongoEventListener();
	}
}