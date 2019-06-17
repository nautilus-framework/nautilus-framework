package thiagodnf.nautilus.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.dto.UploadInstanceDTO;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/problem/{problemId:.+}")
public class ProblemController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProblemController.class);
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("")
	public String view(@PathVariable String problemId, Model model){
		
		LOGGER.info("Displaying '{}'", problemId);
		
		model.addAttribute("instances", fileService.getInstances(problemId));
		model.addAttribute("problem", pluginService.getProblemById(problemId));
		model.addAttribute("uploadInstanceDTO", new UploadInstanceDTO(problemId, null));

		return "problem";
	}
}
