package thiagodnf.nautilus.web.controller;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.web.model.UploadInstance;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class InstanceFileController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/instance-file/{problemKey}/{filename:.+}")
	public String viewInstanceFile(Model model, @PathVariable("problemKey") String problemKey, @PathVariable("filename") String filename) throws IOException {
		
		Path path = fileService.getInstancesFile(problemKey, filename);
		
		model.addAttribute("filename", filename);
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
		model.addAttribute("fileContent", FileUtils.readFileToString(path.toFile()));
		
		return "instance-file";
	}
	
	@GetMapping("/instance-file/{problemKey}/upload")
	public String uploadInstanceFile(Model model, @PathVariable("problemKey") String problemKey) {
		
		model.addAttribute("uploadInstance", new UploadInstance(problemKey));
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
			
		return "upload-instance-file";
	}
}
