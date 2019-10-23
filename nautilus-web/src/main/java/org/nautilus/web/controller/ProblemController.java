package org.nautilus.web.controller;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.dto.UploadInstanceDTO;
import org.nautilus.web.model.User;
import org.nautilus.web.service.FileService;
import org.nautilus.web.service.PluginService;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/problems")
public class ProblemController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private SecurityService securityService;
	
	@GetMapping("")
	public String view(Model model){
	    
	    User user = securityService.getLoggedUser().getUser();

        Map<String, ProblemExtension> problems = pluginService.getProblems();

        Map<String, List<Path>> instances = new HashMap<>();

        for (String key : problems.keySet()) {
            instances.put(key, fileService.getInstances(key));
        }

        model.addAttribute("problems", problems);
        model.addAttribute("instances", instances);

        model.addAttribute("uploadInstanceDTO", new UploadInstanceDTO());
        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));

        return "problems";
	}
}
