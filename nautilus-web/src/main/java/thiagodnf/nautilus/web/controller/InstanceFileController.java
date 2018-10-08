package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/instance-file/{pluginId:.+}/{problemId:.+}/{filename:.+}")
public class InstanceFileController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("")
	public String view(Model model, 
			@PathVariable("pluginId") String pluginId, 
			@PathVariable("problemId") String problemId, 
			@PathVariable("filename") String filename){
		
		String content = fileService.readFileToString(pluginId, problemId, filename);

		model.addAttribute("filename", filename);
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		model.addAttribute("content", pluginService.formatInstanceFile(pluginId, problemId, content));
		
		return "instance-file";
	}
	
	@GetMapping("/delete")
	public String delete(Model model, 
			@PathVariable("pluginId") String pluginId, 
			@PathVariable("problemId") String problemId, 
			@PathVariable("filename") String filename){
		
		fileService.deleteInstanceFile(pluginId, problemId, filename);
		
		return "redirect:/problem/" + pluginId + "/" + problemId;
	}
			
}
