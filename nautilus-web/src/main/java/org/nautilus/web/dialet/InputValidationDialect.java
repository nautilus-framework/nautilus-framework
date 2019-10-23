package org.nautilus.web.dialet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.nautilus.web.annotation.HTMLAutoComplete;
import org.nautilus.web.annotation.HTMLAutoFocus;
import org.nautilus.web.annotation.HTMLMaxLength;
import org.nautilus.web.annotation.HTMLMinLength;
import org.nautilus.web.annotation.HTMLReadonly;
import org.nautilus.web.annotation.HTMLRequired;
import org.nautilus.web.annotation.HTMLSpellCheck;
import org.nautilus.web.annotation.HTMLStep;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class InputValidationDialect extends AbstractProcessorDialect {

    private boolean useInputValidation;
    
    public InputValidationDialect(boolean useInputValidation) {
        super("Input Validation Dialect", // Dialect name
                "th", // Dialect prefix (hello:*)
                1000); // Dialect precedence
        
        this.useInputValidation = useInputValidation;
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
        
        processors.add(new FormValidationFieldAttributeTagProcessor(dialectPrefix, useInputValidation));
        
        return processors;
    }

    public class FormValidationFieldAttributeTagProcessor extends AbstractAttributeTagProcessor {

        private static final String ATTR_NAME = "validation";
 
        private static final int PRECEDENCE = 10000;
        
        private boolean useInputValidation;

        public FormValidationFieldAttributeTagProcessor(final String dialectPrefix, boolean useInputValidation) {
            super(TemplateMode.HTML, // This processor will apply only to HTML mode
                    dialectPrefix, // Prefix to be applied to name for matching
                    null, // No tag name: match any tag name
                    false, // No prefix to be applied to tag name
                    ATTR_NAME, // Name of the attribute that will be matched
                    true, // Apply dialect prefix to attribute name
                    PRECEDENCE, // Precedence (inside dialect's precedence)
                    true); // Remove the matched attribute afterwards
            
            this.useInputValidation = useInputValidation;
        }

        protected void doProcess(
                final ITemplateContext context,  
                final IProcessableElementTag elementTag,
                final AttributeName attributeName, 
                final String attributeValue,
                final IElementTagStructureHandler structureHandler) {

            if(!useInputValidation) {
                return;
            }
            
            Object target = context.getSelectionTarget();
            
            List<String> tags = new ArrayList<>();
            
            try {

                Field field = target.getClass().getDeclaredField(attributeValue);

                for (Annotation annotation : field.getAnnotations()) {

                    if (annotation instanceof DecimalMin) {
                        tags.add(getMin((DecimalMin) annotation));
                    }
                    
                    if (annotation instanceof Min) {
                        tags.add(getMin((Min) annotation));
                    }

                    if (annotation instanceof DecimalMax) {
                        tags.add(getMax((DecimalMax) annotation));
                    }
                    
                    if (annotation instanceof Max) {
                        tags.add(getMax((Max) annotation));
                    }
                    
                    
                    
                    if (annotation instanceof HTMLAutoFocus) {
                        tags.add(getAutoFocus());
                    }
                    
                    if (annotation instanceof HTMLAutoComplete) {
                        tags.add(getAutoComplete((HTMLAutoComplete) annotation));
                    }
                    
                    
                    if (annotation instanceof HTMLStep) {
                        tags.add(getStep((HTMLStep) annotation));
                    }
                    

                    if (annotation instanceof NotNull) {
                        tags.add(getRequired());
                    }

                    if (annotation instanceof NotBlank) {
                        tags.add(getRequired());
                    }

                    if (annotation instanceof NotEmpty) {
                        tags.add(getRequired());
                    }
                    
                    if (annotation instanceof HTMLRequired) {
                        tags.add(getRequired());
                    }
                    
                    
                    if (annotation instanceof HTMLSpellCheck) {
                        tags.add(getSpellCheck((HTMLSpellCheck) annotation));
                    }
                    
                    if (annotation instanceof HTMLMinLength) {
                        tags.add(getMinLength((HTMLMinLength) annotation));
                    }
                    
                    if (annotation instanceof HTMLMaxLength) {
                        tags.add(getMaxLength((HTMLMaxLength) annotation));
                    }
                    
                    if (annotation instanceof HTMLReadonly) {
                        tags.add(getReadonly());
                    }
                }


            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            
            for (String tag : tags) {
                structureHandler.setAttribute(tag, null);
            }
        }
        
        private String getMin(DecimalMin decimalMin) {
            return String.format("min=\"%s\"", decimalMin.value());
        }
        
        private String getMin(Min min) {
            return String.format("min=\"%s\"", min.value());
        }
        
        private String getMax(DecimalMax decimalMax) {
            return String.format("max=\"%s\"", decimalMax.value());
        }
        
        private String getMax(Max max) {
            return String.format("max=\"%s\"", max.value());
        }
        
        private String getStep(HTMLStep step) {
            return String.format("step=\"%s\"", step.value());
        }
        
        private String getSpellCheck(HTMLSpellCheck spellCheck) {
            return String.format("spellcheck=\"%s\"", spellCheck.value());
        }
        
        private String getRequired() {
            return "required";
        }
        
        private String getAutoFocus() {
            return "autofocus";
        }
        
        private String getReadonly() {
            return "readonly";
        }
        
        private String getAutoComplete(HTMLAutoComplete autoComplete) {
            return String.format("autocomplete=\"%s\"", autoComplete.value());
        }
        
        private String getMinLength(HTMLMinLength minLength) {
            return String.format("minlength=\"%s\"", minLength.value());
        }
        
        private String getMaxLength(HTMLMaxLength maxLength) {
            return String.format("maxlength=\"%s\"", maxLength.value());
        }
    }

}
