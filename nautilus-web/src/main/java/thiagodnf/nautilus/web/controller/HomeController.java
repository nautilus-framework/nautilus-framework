package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class HomeController {
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/home")
	public String index(Model model) {
		
		model.addAttribute("startedPlugins", pluginService.getStartedPlugins());
		
		return "home";
	}
}
