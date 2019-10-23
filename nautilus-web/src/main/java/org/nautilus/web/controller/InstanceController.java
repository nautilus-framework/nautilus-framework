package org.nautilus.web.controller;

import java.nio.file.Path;

import org.nautilus.core.model.Instance;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.exception.InstanceNotFoundException;
import org.nautilus.web.service.FileService;
import org.nautilus.web.service.PluginService;
import org.nautilus.web.util.Messages;
import org.nautilus.web.util.Redirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instance/{problemId:.+}/{filename:.+}")
public class InstanceController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private Redirect redirect;
	
	@GetMapping("")
	public String view( 
			@PathVariable String problemId, 
			@PathVariable String filename,
			Model model){
		
		ProblemExtension problemExtension = pluginService.getProblemById(problemId);
		
		if (!fileService.containsInstance(problemId, filename)) {
            throw new InstanceNotFoundException();
        }
		
		Path path = fileService.getInstance(problemId, filename);
		
		Instance instance = problemExtension.getInstance(path);
		
		model.addAttribute("problem", problemExtension);
		model.addAttribute("tabs", instance.getTabs(instance));
	
		return "instance";
	}
	
	@PostMapping("/delete")
	public String delete(
			@PathVariable String problemId, 
			@PathVariable String filename,
			RedirectAttributes ra,
			Model model ){
		
		ProblemExtension problem = pluginService.getProblemById(problemId);
		
		fileService.deleteInstance(problem.getId(), filename);
		
		return redirect.to("/problems").withSuccess(ra, Messages.FILE_DELETED_SUCCESS, filename);
	}
}
