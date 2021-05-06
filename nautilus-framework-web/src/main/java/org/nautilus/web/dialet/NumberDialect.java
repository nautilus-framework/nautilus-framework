package org.nautilus.web.dialet;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.nautilus.web.model.User;
import org.nautilus.web.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.expression.Numbers;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.NumberPointType;

public class NumberDialect extends AbstractProcessorDialect {

    @Autowired
    private SecurityService securityService;
    
    public NumberDialect() {
        super(
            "Number Dialect",   // Dialect name
            "number",           // Dialect prefix (hello:*)
            1000                // Dialect precedence
        );
    }

    /*
     * Initialize the dialect's processors.
     *
     * Note the dialect prefix is passed here because, although we set "hello" to be
     * the dialect's prefix at the constructor, that only works as a default, and at
     * engine configuration time the user might have chosen a different prefix to be
     * used.
     */
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        
        processors.add(new RealAttributeTagProcessor(dialectPrefix));
        
        return processors;
    }

    
    public class RealAttributeTagProcessor extends AbstractStandardExpressionAttributeTagProcessor {
        
        private static final String ATTR_NAME = "real";
        
        private static final int PRECEDENCE = 10000;
        
        private Numbers numbers = new Numbers(Locale.getDefault());
        
        protected RealAttributeTagProcessor(final String dialectPrefix) {
            super(
                TemplateMode.HTML, 
                dialectPrefix, 
                ATTR_NAME, 
                PRECEDENCE, 
                true
            );
        }

        @Override
        protected void doProcess(
                ITemplateContext context, 
                IProcessableElementTag elementTag, 
                AttributeName attributeName,
                String attributeValue, 
                Object expressionResult, 
                IElementTagStructureHandler structureHandler) {
            
            if (expressionResult == null) {
                throw new TemplateProcessingException("The expression result is null for " + attributeValue);
            }

            boolean isNumber = expressionResult instanceof Number;

            if (!isNumber) {
                throw new TemplateProcessingException("The expression result is not a number for " + attributeValue);
            }
            
            Number number = (Number) expressionResult;
            
            User user = securityService.getLoggedUser().getUser();
            
            String formatted = numbers.formatDecimal(
                    number, 
                    1, 
                    NumberPointType.COMMA.toString(), 
                    user.getDecimalPlaces(), 
                    user.getDecimalSeparator());
            
            // Report the result to the engine, whichever the type of process we have applied
            structureHandler.setBody(formatted, false);
        }
    }
}
