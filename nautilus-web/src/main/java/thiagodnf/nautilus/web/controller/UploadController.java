package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import thiagodnf.nautilus.web.model.UploadInstanceFile;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/upload")
public class UploadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@PostMapping("/instance-file/{problemKey}")
	public String uploadWithPost(@PathVariable("problemKey") String problemKey, @Valid UploadInstanceFile uploadInstanceFile, BindingResult result, Model model) {

		LOGGER.info("Uploading the file: " + uploadInstanceFile.getFile().getOriginalFilename());

		if (result.hasErrors()) {
			
			model.addAttribute("plugin", pluginService.getPlugin(problemKey));
			
			return "upload-instance-file";
		}
		
		MultipartFile file = uploadInstanceFile.getFile();
		
		String filename = file.getOriginalFilename();
		
		LOGGER.info("Storing the instance");

		fileService.store(problemKey, file, filename);
		
		LOGGER.info("Done");
		
		return "redirect:/problem/" + problemKey;
	}
}
