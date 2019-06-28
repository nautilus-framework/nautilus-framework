package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.dto.UploadExecutionDTO;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private SecurityService securityService;
	
	@GetMapping("")
	public String index(Model model) {
	    
	    User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("problems", pluginService.getProblems());
		model.addAttribute("uploadExecutionDTO", new UploadExecutionDTO());
		model.addAttribute("executions", executionService.findExecutionSimplifiedDTOByUserId(user.getId()));
		model.addAttribute("runningExecutions", executionService.findRunningExecutions());
		
		return "home";
	}
}
