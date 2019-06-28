package thiagodnf.nautilus.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.model.Execution.Visibility;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.UserService;

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
