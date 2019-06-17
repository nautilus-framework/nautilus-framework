package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.util.Messages;

@Controller
@RequestMapping("/instance/{problemId:.+}/{filename:.+}")
public class InstanceController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("")
	public String view( 
			@PathVariable String problemId, 
			@PathVariable String filename,
			Model model){
		
		ProblemExtension problemExtension = pluginService.getProblemById(problemId);
		
		Path path = fileService.getInstance(problemId, filename);
		
		Instance instance = problemExtension.getInstance(path);
		
		model.addAttribute("problem", problemExtension);
		model.addAttribute("tabs", problemExtension.getTabs(instance));
	
		return "instance";
	}
	
	@PostMapping("/delete")
	public String delete(
			@PathVariable String problemId, 
			@PathVariable String filename,
			Model model, 
			RedirectAttributes ra){
		
		ProblemExtension problem = pluginService.getProblemById(problemId);
		
		fileService.deleteInstance(problem.getId(), filename);
		
		flashMessageService.success(ra, Messages.FILE_DELETED_SUCCESS, filename);
		
		return "redirect:/problem/" + problem.getId();
	}
			
}
