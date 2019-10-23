package org.nautilus.web.listener;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class TrimStringMongoEventListener extends AbstractMongoEventListener<Object> {

	@Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
		
		Object source = event.getSource();
		
		for (Field field : source.getClass().getDeclaredFields()) {

			if (field.getType().isAssignableFrom(String.class) && ! Modifier.isFinal(field.getModifiers())) {

				try {
					// Set it as accessible once it can be private 
					field.setAccessible(true);
                    
					String value = (String) field.get(source);
                    
					if(value != null) {
						field.set(source, value.trim());
					}
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
			}
		}
	}
}
