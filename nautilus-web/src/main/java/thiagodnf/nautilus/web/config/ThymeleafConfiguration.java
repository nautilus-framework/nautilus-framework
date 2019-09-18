package thiagodnf.nautilus.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;

import thiagodnf.nautilus.web.dialet.ConverterDialect;
import thiagodnf.nautilus.web.dialet.FormatterDialect;
import thiagodnf.nautilus.web.dialet.InputValidationDialect;
import thiagodnf.nautilus.web.dialet.NumberDialect;

@Configuration
public class ThymeleafConfiguration {
	
    @Value("${dialect.use-input-validation}")
    private boolean useInputValidation;
    
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
    public InputValidationDialect getInputValidationDialect() {
	    return new InputValidationDialect(useInputValidation);
    }
	
	@Bean
    public NumberDialect getNumberDialect() {
        return new NumberDialect();
    }
}
