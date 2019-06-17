package thiagodnf.nautilus.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/done/{executionId:.+}")
public class DoneController {
	
	@PostMapping("")
	public String show(Model model, 
			@PathVariable("executionId") String executionId,
			BindingResult result,
			RedirectAttributes ra){
		
	   
		return "done";
	}
}
