package thiagodnf.nautilus.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpartakusController {
	
	@GetMapping("/spartakus")
	public String index(Model model) {
		
		return "spartakus";
	}
}
