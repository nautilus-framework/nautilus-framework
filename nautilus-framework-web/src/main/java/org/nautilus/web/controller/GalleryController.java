package org.nautilus.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.dto.ExecutionSimplifiedDTO;
import org.nautilus.web.model.User;
import org.nautilus.web.model.Execution.Visibility;
import org.nautilus.web.service.ExecutionService;
import org.nautilus.web.service.PluginService;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
    private PluginService pluginService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private SecurityService securityService;
    
    @GetMapping("")
	public String showUsers(Model model){
	    
        User user = securityService.getLoggedUser().getUser(); 
        
	    Map<String, ProblemExtension> problems = pluginService.getProblems();

        Map<String, List<ExecutionSimplifiedDTO>> executions = new HashMap<>();

        for (ExecutionSimplifiedDTO execution : executionService.findExecutionSimplifiedDTOByVisibility(Visibility.PUBLIC)) {

            if (!executions.containsKey(execution.getProblemId())) {
                executions.put(execution.getProblemId(), new ArrayList<>());
            }

            executions.get(execution.getProblemId()).add(execution);
        }

        model.addAttribute("problems", problems);
		model.addAttribute("executions", executions);
		model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
		
		return "gallery";
	}
}
