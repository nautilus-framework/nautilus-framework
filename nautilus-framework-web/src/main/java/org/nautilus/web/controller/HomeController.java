package org.nautilus.web.controller;

import org.nautilus.web.model.User;
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
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@GetMapping("")
	public String index(Model model) {
	    
	    User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("problems", pluginService.getProblemsSorted());
		model.addAttribute("executions", executionService.findExecutionSimplifiedDTOByUserId(user.getId()));
		model.addAttribute("runningExecutions", executionService.findRunningExecutions());
		model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
		
		return "home";
	}
}
