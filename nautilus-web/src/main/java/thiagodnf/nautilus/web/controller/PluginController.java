package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;

import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.model.UploadPlugin;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/plugin")
public class PluginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginController.class);
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FlashMessageService flashMessageService;

	@GetMapping("/{pluginId:.+}")
	public String view(Model model, 
			@PathVariable("pluginId") String pluginId) {
		
		LOGGER.info("Displaying '{}'", pluginId);
		
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problems", pluginService.getProblemExtensions(pluginId));
		
		return "plugin";
	}
	
	@PostMapping("/{pluginId:.+}/delete")
	public String delete(Model model, 
			RedirectAttributes ra,
			@PathVariable("pluginId") String pluginId) {

		LOGGER.info("Deleting the plugin {}", pluginId);
		
		PluginWrapper plugin = pluginService.getPluginWrapper(pluginId);

		Path path = plugin.getPluginPath();
		
		pluginService.stopAndUnload(pluginId);
		
		//fileService.delete(path);
		
		flashMessageService.success(ra, "msg.delete.plugin.success", plugin.getDescriptor().getPluginDescription());
		
		return "redirect:/";
	}

	@GetMapping("/upload")
	public String uploadFile(Model model) {

		model.addAttribute("uploadPlugin", new UploadPlugin());

		return "upload-plugin";
	}
}
