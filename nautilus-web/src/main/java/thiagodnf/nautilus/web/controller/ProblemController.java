package thiagodnf.nautilus.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.plugin.AbstractPlugin;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class ProblemController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private ExecutionService executionService;
	
	@GetMapping("/problem/{problemKey}")
	public String viewProblem(Model model, @PathVariable("problemKey") String problemKey) throws IOException {
		
		AbstractPlugin plugin = pluginService.getPlugin(problemKey);

		if (plugin == null) {
			throw new RuntimeException("The problem key is invalid");
		}
		
		model.addAttribute("problemKey", problemKey);
		model.addAttribute("problemName", plugin.getProblemName());
		model.addAttribute("description", plugin.getDescription());
		model.addAttribute("executions", executionService.findByProblemKey(problemKey));
		model.addAttribute("instanceFiles", fileService.getInstanceFiles(problemKey));
		
		return "problem";
	}
}
