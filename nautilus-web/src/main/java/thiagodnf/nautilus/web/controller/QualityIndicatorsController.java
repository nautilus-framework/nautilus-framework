package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/quality-indicators/{pluginId:.+}/{problemId:.+}")
public class QualityIndicatorsController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("")
	public String select(Model model, 
			@PathVariable("pluginId") String pluginId,
			@PathVariable("problemId") String problemId){
		
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		model.addAttribute("qualityIndicatorsFactory", pluginService.getQualityIndicatorFactory(pluginId));
		model.addAttribute("executions", executionService.findByPluginIdAndProblemId(pluginId, problemId));
		
		return "quality-indicators";
	}
}
