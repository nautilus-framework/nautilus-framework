package thiagodnf.nautilus.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;

import thiagodnf.nautilus.web.dialet.ConverterDialect;
import thiagodnf.nautilus.web.dialet.FormatterDialect;

@Configuration
public class ThymeleafConfiguration {
	
	@Bean
	public FormatterDialect getFormatterDialect() {
		return new FormatterDialect();
	}
	
	@Bean
	public ConverterDialect getConverterDialect() {
		return new ConverterDialect();
	}
	
	@Bean
	public DataAttributeDialect getDataAttributeDialect() {
		return new DataAttributeDialect();
	}
}
