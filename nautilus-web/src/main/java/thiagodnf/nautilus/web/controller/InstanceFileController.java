package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class InstanceFileController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/instance-file/{problemKey}/{filename:.+}")
	public String viewInstanceFile(Model model, 
			@PathVariable("problemKey") String problemKey, 
			@PathVariable("filename") String filename){
		
		model.addAttribute("filename", filename);
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
		model.addAttribute("fileContent", fileService.readFileToString(problemKey, filename));
		
		return "instance-file";
	}
}
