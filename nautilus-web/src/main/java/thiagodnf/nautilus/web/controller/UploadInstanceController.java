package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.model.UploadInstance;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/upload-instance")
public class UploadInstanceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadInstanceController.class);
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@PostMapping("/upload")
	public String uploadWithPost(@Valid UploadInstance uploadInstance, BindingResult result, Model model, RedirectAttributes ra) {

		LOGGER.info("Uploading the file: " + uploadInstance.getFile().getOriginalFilename());

		if (result.hasErrors()) {
			
			model.addAttribute("plugin", pluginService.getPlugin(uploadInstance.getProblemKey()));
			
			return "upload-instance-file";
		}
		
		MultipartFile file = uploadInstance.getFile();
		
		String filename = file.getOriginalFilename();
		
		LOGGER.info("Storing the instance");

		fileService.store(uploadInstance.getProblemKey(), file, filename);
		
		LOGGER.info("Done");
		
		return "redirect:/";
	}
}
