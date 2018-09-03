package thiagodnf.nautilus.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class ProblemController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/problem/{problemKey}")
	public String viewProblem(Model model, @PathVariable("problemKey") String problemKey) throws IOException {
		
		model.addAttribute("problemKey", problemKey);
		model.addAttribute("problemName", pluginService.getPlugin(problemKey).getProblemName());
		model.addAttribute("instanceFiles", fileService.getInstanceFiles(problemKey));
		
		return "problem";
	}
}
