package org.nautilus.web.controller;

import org.nautilus.web.service.ExecutionService;
import org.nautilus.web.service.FlashMessageService;
import org.nautilus.web.service.PluginService;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/plugin")
public class PluginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginController.class);
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@Autowired
	private ExecutionService executionService;
	
//	@Autowired
//	private ParetoFrontService paretoFrontService;
	
	@GetMapping("/{pluginId:.+}")
	public String view(Model model, 
			@PathVariable("pluginId") String pluginId) {
		
		LOGGER.info("Displaying '{}'", pluginId);
		
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problems", pluginService.getProblemExtensions(pluginId));
//		model.addAttribute("executions", executionService.findByPluginId(pluginId));
		//model.addAttribute("uploadExecution", new UploadExecution());
//		model.addAttribute("generateParetoFront", new GenerateParetoFront());
//		model.addAttribute("uploadRealParetoFront", new UploadRealParetoFront());
		
		return "plugin";
	}
	
	@PostMapping("/{pluginId:.+}/delete")
	public String delete(Model model, 
			RedirectAttributes ra,
			@PathVariable("pluginId") String pluginId) {

		LOGGER.info("Deleting the plugin {}", pluginId);
		
		PluginWrapper plugin = pluginService.deletePlugin(pluginId);
		
		flashMessageService.success(ra, "msg.delete.plugin.success", plugin.getDescriptor().getPluginDescription());
		
		return "redirect:/home";
	}
}