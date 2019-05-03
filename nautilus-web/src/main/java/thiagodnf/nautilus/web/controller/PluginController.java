package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.model.GenerateParetoFront;
import thiagodnf.nautilus.web.model.UploadExecution;
import thiagodnf.nautilus.web.model.UploadRealParetoFront;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.ParetoFrontService;
import thiagodnf.nautilus.web.service.PluginService;

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
	
	@Autowired
	private ParetoFrontService paretoFrontService;
	
	@GetMapping("/{pluginId:.+}")
	public String view(Model model, 
			@PathVariable("pluginId") String pluginId) {
		
		LOGGER.info("Displaying '{}'", pluginId);
		
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problems", pluginService.getProblemExtensions(pluginId));
		model.addAttribute("executions", executionService.findByPluginId(pluginId));
		model.addAttribute("uploadExecution", new UploadExecution());
		model.addAttribute("generateParetoFront", new GenerateParetoFront());
		model.addAttribute("uploadRealParetoFront", new UploadRealParetoFront());
		
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
	
	@PostMapping("/{pluginId:.+}/generate-approx-pareto-front")
	public String generateApproxParetoFront(
			@Valid GenerateParetoFront generateParetoFront, 
			BindingResult result,
			RedirectAttributes ra,
			@PathVariable("pluginId") String pluginId,
			Model model){
		
		if (result.hasErrors()) {
			flashMessageService.error(ra, result.getAllErrors());
		} else {
			
			paretoFrontService.generateApproxParetoFront(pluginId, generateParetoFront.getProblemId());
			
			flashMessageService.success(ra, "msg.generate.approx.pareto-front.success");
		}

		return "redirect:/plugin/" + pluginId + "#executions";
	}
}
