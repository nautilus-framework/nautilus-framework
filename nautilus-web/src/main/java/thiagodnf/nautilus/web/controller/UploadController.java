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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import thiagodnf.nautilus.web.exception.RedirectException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.UploadExecution;
import thiagodnf.nautilus.web.model.UploadInstanceFile;
import thiagodnf.nautilus.web.model.UploadPlugin;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.FlashMessageService;
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
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@PostMapping("/plugin/")
	public String uploadPlugin(
			@Valid UploadPlugin uploadPlugin, 
			BindingResult result,
			RedirectAttributes ra,
			Model model) {

		if (result.hasErrors()) {
			flashMessageService.error(ra, result.getAllErrors());
		}else {
			MultipartFile file = uploadPlugin.getFile();
			
			String filename = file.getOriginalFilename();
			
			LOGGER.info("Storing the plugin " + filename);
			
			try {
				fileService.storePlugin(filename, file);
				pluginService.loadPluginsFromDirectory();
				flashMessageService.success(ra, "msg.upload.file.success", filename);
			} catch (RedirectException ex) {
				flashMessageService.error(ra, ex);
			}
		}
		
		return "redirect:/home";
	}
	
	@PostMapping("/instance-file/{pluginId:.+}/{problemId:.+}")
	public String uploadWithPost(
			@PathVariable("pluginId") String pluginId,
			@PathVariable("problemId") String problemId,
			@Valid UploadInstanceFile uploadInstanceFile, 
			BindingResult result, 
			RedirectAttributes ra,
			Model model) {

		if (result.hasErrors()) {
			flashMessageService.error(ra, result.getAllErrors());
		}else {
			MultipartFile file = uploadInstanceFile.getFile();
			
			String filename = file.getOriginalFilename();
			
			LOGGER.info("Storing the instance file " + filename);

			try {
				fileService.storeInstanceFile(pluginId, problemId, filename, file);
				flashMessageService.success(ra, "msg.upload.file.success", filename);
			} catch (RedirectException ex) {
				flashMessageService.error(ra, ex);
			}
		}
		
		return "redirect:/problem/" + pluginId + "/" + problemId;
	}
	
	@PostMapping("/execution/{pluginId:.+}/{problemId:.+}")
	public String uploadExecution(
			@PathVariable("pluginId") String pluginId,
			@PathVariable("problemId") String problemId,
			@Valid UploadExecution uploadExecution, 
			BindingResult result, 
			Model model) {

		LOGGER.info("Uploading the file: " + uploadExecution.getFile().getOriginalFilename());

		if (result.hasErrors()) {

			model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
			model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));

			return "upload-execution";
		}

		try {

			MultipartFile file = uploadExecution.getFile();

			String content = new String(file.getBytes(), "UTF-8");

			Execution execution = new Gson().fromJson(content, Execution.class);

			if (executionService.existsById(execution.getId())) {
				throw new RuntimeException("The execution id already exists");
			}
			
			if (!execution.getParameters().getPluginId().equalsIgnoreCase(pluginId)) {
				throw new RuntimeException("This execution is for a different plugin");
			}

			if (!execution.getParameters().getProblemId().equalsIgnoreCase(problemId)) {
				throw new RuntimeException("This execution is for a different problem");
			}

			executionService.save(execution);

			LOGGER.info("Saved");

			return "redirect:/problem/" + pluginId + "/" + problemId + "#executions";

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
