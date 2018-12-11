package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.plugin.extension.GUIExtension;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/instance-file/{pluginId:.+}/{problemId:.+}/{filename:.+}")
public class InstanceFileController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("")
	public String view(Model model, 
			@PathVariable("pluginId") String pluginId, 
			@PathVariable("problemId") String problemId, 
			@PathVariable("filename") String filename){
		
		String content = fileService.readFileToString(pluginId, problemId, filename);

		GUIExtension guiExtension = pluginService.getGUIExtension(pluginId);
		
		Path instance = fileService.getInstanceFile(pluginId, problemId, filename);
		
//		InstanceData data = pluginService.getProblemExtension(pluginId, problemId).readInstanceData(instance);
		InstanceData data = null;
		
		model.addAttribute("filename", filename);
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		model.addAttribute("content", pluginService.formatInstanceFile(pluginId, problemId, content));
		model.addAttribute("tabs", guiExtension.getTabs(data));
		
		return "instance-file";
	}
	
	@PostMapping("/delete")
	public String delete(Model model, 
			RedirectAttributes ra,
			@PathVariable("pluginId") String pluginId, 
			@PathVariable("problemId") String problemId, 
			@PathVariable("filename") String filename){
		
		fileService.deleteInstanceFile(pluginId, problemId, filename);
		
		flashMessageService.success(ra, "msg.delete.instance-file.success", filename);
		
		return "redirect:/problem/" + pluginId + "/" + problemId;
	}
			
}
