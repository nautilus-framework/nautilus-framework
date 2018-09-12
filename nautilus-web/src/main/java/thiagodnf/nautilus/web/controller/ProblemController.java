package thiagodnf.nautilus.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.web.model.UploadExecution;
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
		
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
		model.addAttribute("executions", executionService.findByProblemKey(problemKey));
		model.addAttribute("instanceFiles", fileService.getInstanceFiles(problemKey));
		
		return "problem";
	}
	
	@GetMapping("/problem/{problemKey}/upload/execution/")
	public String uploadInstanceFile(Model model, @PathVariable("problemKey") String problemKey) {
		
		model.addAttribute("uploadExecution", new UploadExecution());
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
			
		return "upload-execution";
	}
}
