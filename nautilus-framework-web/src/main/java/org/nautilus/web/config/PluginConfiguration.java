package org.nautilus.web.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.nautilus.plugin.annotations.ExtensionPoint;
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
import org.nautilus.web.service.FileService;
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
    
    @Autowired
    protected FileService fileService;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Reflections reflections = new Reflections("org.nautilus");

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(PluginExtension.class);

        for (Class<?> c : classes) {

            if (c.isAnnotation() || c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
                continue;
            }

            ExtensionPoint ep = getNewInstance(c);
            
            LOGGER.info("Adding plugin {}", ep.getClass().getCanonicalName());

            if (ep instanceof AbstractProblemExtension) {
                pluginService.getProblems().put(ep.getId(), (ProblemExtension) ep);
            }
            
            if (ep instanceof AbstractAlgorithmExtension) {
                pluginService.getAlgorithms().put(ep.getId(), (AlgorithmExtension) ep);
            }

            if (ep instanceof AbstractSelectionExtension) {
                pluginService.getSelections().put(ep.getId(), (SelectionExtension) ep);
            }
            
            if (ep instanceof AbstractCrossoverExtension) {
                pluginService.getCrossovers().put(ep.getId(), (CrossoverExtension) ep);
            }
            
            if (ep instanceof AbstractMutationExtension) {
                pluginService.getMutations().put(ep.getId(), (MutationExtension) ep);
            }
             
            if (ep instanceof AbstractNormalizerExtension) {
                pluginService.getNormalizers().put(ep.getId(), (NormalizerExtension) ep);
            }

            if (ep instanceof AbstractCorrelationExtension) {
                pluginService.getCorrelations().put(ep.getId(), (CorrelationExtension) ep);
            }
            
            if (ep instanceof AbstractRemoverExtension) {
                pluginService.getRemovers().put(ep.getId(), (RemoverExtension) ep);
            }
        }
        
        for (ProblemExtension problem : pluginService.getProblems().values()) {
            fileService.createProblemLocation(problem.getId());
        }
    }
     
    protected ExtensionPoint getNewInstance(Class<?> c) {

        try {
            Constructor<?> ctr = c.getConstructor();
            return (ExtensionPoint) ctr.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
