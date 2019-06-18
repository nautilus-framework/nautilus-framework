package thiagodnf.nautilus.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.service.ExecutionService;

@Controller
@RequestMapping("/done/{executionId:.+}")
public class DoneController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DoneController.class);
    
    @Autowired
    private ExecutionService executionService;
    
	@GetMapping("")
	public String show(@PathVariable String executionId, Model model){
		
	    LOGGER.info("Displaying '{}'", executionId);
        
        Execution execution = executionService.findExecutionById(executionId);
	    
        model.addAttribute("execution", execution);
        
		return "done";
	}
}
