package thiagodnf.nautilus.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.model.UploadExecution;
import thiagodnf.nautilus.web.model.UploadInstanceFile;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/problem")
public class ProblemController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private ExecutionService executionService;
	
	@GetMapping("/{problemKey}")
	public String viewProblem(Model model, @PathVariable("problemKey") String problemKey) throws IOException {
		
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
		model.addAttribute("executions", executionService.findByProblemKey(problemKey));
		model.addAttribute("instanceFiles", fileService.getInstanceFiles(problemKey));
		
		return "problem";
	}
	
	@GetMapping("/{problemKey}/upload/execution/")
	public String uploadExecution(Model model, @PathVariable("problemKey") String problemKey) {
		
		model.addAttribute("uploadExecution", new UploadExecution());
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
			
		return "upload-execution";
	}
	
	@GetMapping("/{problemKey}/upload/instance-file/")
	public String uploadInstanceFile(Model model, @PathVariable("problemKey") String problemKey) {
		
		model.addAttribute("uploadInstanceFile", new UploadInstanceFile());
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
			
		return "upload-instance-file";
	}
}
