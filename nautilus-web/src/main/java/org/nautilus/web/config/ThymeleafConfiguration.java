package org.nautilus.web.config;

import org.nautilus.web.dialet.ConverterDialect;
import org.nautilus.web.dialet.FormatterDialect;
import org.nautilus.web.dialet.NumberDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;

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
	
	@Bean
    public NumberDialect getNumberDialect() {
        return new NumberDialect();
    }
}
