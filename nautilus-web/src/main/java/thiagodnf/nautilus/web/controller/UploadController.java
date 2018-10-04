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

import com.google.gson.Gson;

import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.UploadExecution;
import thiagodnf.nautilus.web.model.UploadInstanceFile;
import thiagodnf.nautilus.web.model.UploadPlugin;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/upload")
public class UploadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ExecutionService executionService;
	
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
	
	@PostMapping("/execution/{problemKey}")
	public String uploadExecution(@PathVariable("problemKey") String problemKey, @Valid UploadExecution uploadExecution, BindingResult result, Model model) {

		LOGGER.info("Uploading the file: " + uploadExecution.getFile().getOriginalFilename());

		if (result.hasErrors()) {

			model.addAttribute("plugin", pluginService.getPlugin(problemKey));

			return "upload-execution";
		}

		try {

			MultipartFile file = uploadExecution.getFile();

			String content = new String(file.getBytes(), "UTF-8");

			Execution execution = new Gson().fromJson(content, Execution.class);

			if (executionService.existsById(execution.getId())) {
				throw new RuntimeException("The execution id already exists");
			}
			
			if (!execution.getParameters().getProblemKey().equalsIgnoreCase(problemKey)) {
				throw new RuntimeException("This execution is for a different problem key");
			}

			executionService.save(execution);

			LOGGER.info("Done");

			return "redirect:/problem/" + problemKey + "#executions";

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@PostMapping("/plugin/")
	public String uploadExtension(@Valid UploadPlugin uploadPlugin, BindingResult result, Model model) {

		LOGGER.info("Uploading the file: " + uploadPlugin.getFile().getOriginalFilename());

		if (result.hasErrors()) {
			return "upload-plugin";
		}
		
		MultipartFile file = uploadPlugin.getFile();
		
		String filename = file.getOriginalFilename();
		
		LOGGER.info("Storing the plugin file");
		
		System.out.println(filename);

		pluginService.store(filename, file);
		
		LOGGER.info("Done");
		
		return "redirect:/";
	}
}
