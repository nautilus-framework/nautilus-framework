package thiagodnf.nautilus.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.model.UploadPlugin;

@Controller
@RequestMapping("/plugin")
public class PluginController {
	
	@GetMapping("/upload")
	public String uploadFile(Model model) {
		
		model.addAttribute("uploadPlugin", new UploadPlugin());
		//model.addAttribute("plugin", pluginService.getPlugin(problemKey));
			
		return "upload-plugin";
	}
}
