package thiagodnf.nautilus.web.util;

import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import thiagodnf.nautilus.plugin.extension.ProblemExtension;

public class MainClass {

	public static void main(String[] args) {
		
		System.out.println("oi");
		
		// create the plugin manager
        final PluginManager pluginManager = new DefaultPluginManager();
        
     // load the plugins
       // pluginManager.loadPlugins();
        
        pluginManager.loadPlugin(Paths.get("nautilus-plugin-mip-1.0.0.jar"));
        
        pluginManager.startPlugins();
        
        List<ProblemExtension> greetings = pluginManager.getExtensions(ProblemExtension.class);
		
        
        System.out.println(String.format("Found %d extensions for extension point '%s'", greetings.size(), ProblemExtension.class.getName()));
        for (ProblemExtension greeting : greetings) {
        	System.out.println(greeting);
            //System.out.println(">>> " + greeting.getGreeting());
        }
        
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
		 
		 System.out.println("Extensions added by classpath:");
	        Set<String> extensionClassNames = pluginManager.getExtensionClassNames(null);
	        for (String extension : extensionClassNames) {
	            System.out.println("   " + extension);
	        }
		 
		 for (PluginWrapper plugin : startedPlugins) {
	            String pluginId = plugin.getDescriptor().getPluginId();
	            System.out.println(String.format("Extensions added by plugin '%s':", pluginId));
	            extensionClassNames = pluginManager.getExtensionClassNames(pluginId);
	            for (String extension : extensionClassNames) {
	                System.out.println("   " + extension);
	            }
	            
	            System.out.println(plugin.getPluginId());
	            System.out.println(plugin.getDescriptor().getProvider());
	            System.out.println(plugin.getDescriptor().getPluginDescription());
	        }
		 
		for (PluginWrapper plugin : startedPlugins) {
           String pluginId = plugin.getDescriptor().getPluginId();

           System.out.println(plugin);
           
           List<ProblemExtension> extensions = pluginManager.getExtensions(ProblemExtension.class, pluginId);
           
           
           System.out.println(extensions);
		}
		
		  // print the extensions instances for Greeting extension point for each started plugin
		for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            System.out.println(String.format("Extensions instances added by plugin '%s' for extension point '%s':", pluginId, ProblemExtension.class.getName()));
            List<ProblemExtension> extensions = pluginManager.getExtensions(ProblemExtension.class, pluginId);
            for (Object extension : extensions) {
                System.out.println("   " + extension);
            }
        }
	}
}

