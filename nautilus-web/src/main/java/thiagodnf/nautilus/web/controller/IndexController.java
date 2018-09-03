package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thiagodnf.nautilus.web.service.FileService;

@Controller
public class IndexController {
	
	@Autowired
	private FileService fileService;
	
	@GetMapping("/")
	public String index(Model model) {
				
		model.addAttribute("problems", fileService.getProblems());
		
		return "index";
	}
}
