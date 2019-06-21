package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.model.Execution.Visibility;
import thiagodnf.nautilus.web.service.ExecutionService;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
	
	@Autowired
	private ExecutionService executionService;
	
	@GetMapping("")
	public String showUsers(Model model){
		
		model.addAttribute("executions", executionService.findExecutionSimplifiedDTOByVisibility(Visibility.PUBLIC));
		
		return "gallery";
	}
}
