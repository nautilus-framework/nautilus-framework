package thiagodnf.nautilus.web.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.UserService;

@Controller
@RequestMapping("/done/{executionId:.+}")
public class DoneController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DoneController.class);
    
    @Autowired
    private ExecutionService executionService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PluginService pluginService;
    
    @Autowired
    private SecurityService securityService;
    
	@GetMapping("")
	public String show(@PathVariable String executionId, Model model){
		
	    LOGGER.info("Displaying '{}'", executionId);
        
	    User user = securityService.getLoggedUser().getUser();
	    
	    Map<String, ProblemExtension> problems = pluginService.getProblems();
	    
        Execution execution = executionService.findExecutionById(executionId);
	    
        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
        model.addAttribute("executions", executionService.findExecutionSimplifiedDTOByUserId(user.getId()));
        model.addAttribute("problems", problems);
        model.addAttribute("execution", execution);
        
		return "done";
	}
}
