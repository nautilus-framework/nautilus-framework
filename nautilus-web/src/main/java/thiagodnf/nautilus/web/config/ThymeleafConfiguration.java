package thiagodnf.nautilus.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import thiagodnf.nautilus.web.dialet.FormatterDialect;

@Configuration
public class ThymeleafConfiguration {
	
	@Bean
	public FormatterDialect myDialect() {
		return new FormatterDialect();
	}
}
