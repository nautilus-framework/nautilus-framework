package org.nautilus.web.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.nautilus.plugin.annotations.PluginExtension;
import org.nautilus.plugin.extension.AlgorithmExtension;
import org.nautilus.plugin.extension.CorrelationExtension;
import org.nautilus.plugin.extension.CrossoverExtension;
import org.nautilus.plugin.extension.MutationExtension;
import org.nautilus.plugin.extension.NormalizerExtension;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.RemoverExtension;
import org.nautilus.plugin.extension.SelectionExtension;
import org.nautilus.plugin.extension.algorithm.AbstractAlgorithmExtension;
import org.nautilus.plugin.extension.correlation.AbstractCorrelationExtension;
import org.nautilus.plugin.extension.crossover.AbstractCrossoverExtension;
import org.nautilus.plugin.extension.mutation.AbstractMutationExtension;
import org.nautilus.plugin.extension.normalizer.AbstractNormalizerExtension;
import org.nautilus.plugin.extension.problem.AbstractProblemExtension;
import org.nautilus.plugin.extension.remover.AbstractRemoverExtension;
import org.nautilus.plugin.extension.selection.AbstractSelectionExtension;
import org.nautilus.web.service.PluginService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class PluginConfiguration  implements ApplicationListener<ContextRefreshedEvent>{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginConfiguration.class);
    
    @Autowired
    protected PluginService pluginService;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Reflections reflections = new Reflections("org.nautilus");

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(PluginExtension.class);

        for (Class<?> c : classes) {

            if (c.isAnnotation() || c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
                continue;
            }

            Object obj = getNewInstance(c);
            
            LOGGER.info("Adding plugin {}", obj.getClass().getCanonicalName());

            if (obj instanceof AbstractProblemExtension) {
                pluginService.addProblemExtension((ProblemExtension) obj);
            }
            
            if (obj instanceof AbstractAlgorithmExtension) {
                pluginService.addAlgorithmExtension((AlgorithmExtension) obj);
            }

            if (obj instanceof AbstractSelectionExtension) {
                pluginService.addSelectionExtension((SelectionExtension) obj);
            }
            
            if (obj instanceof AbstractCrossoverExtension) {
                pluginService.addCrossoverExtension((CrossoverExtension) obj);
            }
            
            if (obj instanceof AbstractMutationExtension) {
                pluginService.addMutationExtension((MutationExtension) obj);
            }
             
            if (obj instanceof AbstractNormalizerExtension) {
                pluginService.addNormalizerExtension((NormalizerExtension) obj);
            }

            if (obj instanceof AbstractCorrelationExtension) {
                pluginService.addCorrelationExtension((CorrelationExtension) obj);
            }
            
            if (obj instanceof AbstractRemoverExtension) {
                pluginService.addRemoverExtension((RemoverExtension) obj);
            }
        }
    }
    
    protected Object getNewInstance(Class<?> c) {

        try {
            Constructor<?> ctor = c.getConstructor();
            return ctor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }
}
