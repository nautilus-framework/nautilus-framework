package org.nautilus.web.controller;

import javax.validation.Valid;

import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.dto.UploadExecutionDTO;
import org.nautilus.web.dto.UploadInstanceDTO;
import org.nautilus.web.exception.AbstractRedirectException;
import org.nautilus.web.model.Execution;
import org.nautilus.web.model.UploadPlugin;
import org.nautilus.web.model.User;
import org.nautilus.web.service.ExecutionService;
import org.nautilus.web.service.FileService;
import org.nautilus.web.service.FlashMessageService;
import org.nautilus.web.service.PluginService;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.util.Messages;
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
	private SecurityService securityService;
	
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
				flashMessageService.success(ra, "msg.upload.plugin.success", filename);
			} catch (AbstractRedirectException ex) {
				flashMessageService.error(ra, ex);
			}
		}
		
		return "redirect:/home";
	}
	
	@PostMapping("/instance/")
	public String uploadInstance(
			@Valid UploadInstanceDTO uploadInstanceDTO, 
			BindingResult result, 
			RedirectAttributes ra,
			Model model) {

		ProblemExtension problem = pluginService.getProblemById(uploadInstanceDTO.getProblemId());
		
		if (result.hasErrors()) {
			flashMessageService.error(ra, result.getAllErrors());
		}else {
			
			MultipartFile file = uploadInstanceDTO.getFile();
			
			String filename = file.getOriginalFilename();
			
			LOGGER.info("Storing the instance {}", filename);

			try {
				fileService.storeInstance(problem.getId(), filename, file);
				flashMessageService.success(ra, Messages.FILE_UPLOADED_SUCCESS, filename);
			} catch (AbstractRedirectException ex) {
				flashMessageService.error(ra, ex);
			}
		}
		
		return "redirect:/problems/";
	}
	
	@PostMapping("/execution/")
	public String uploadExecution(
			@Valid UploadExecutionDTO uploadExecutionDTO, 
			BindingResult result, 
			RedirectAttributes ra,
			Model model) {

		LOGGER.info("Uploading the file: {}", uploadExecutionDTO.getFile().getOriginalFilename());

		if (result.hasErrors()) {
			flashMessageService.error(ra, result.getAllErrors());
		}else {
			
			User user = securityService.getLoggedUser().getUser(); 
			
			try {

				MultipartFile file = uploadExecutionDTO.getFile();

				String content = null;

				try {
					content = new String(file.getBytes(), "UTF-8");
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 

				Execution execution = Converter.fromJson(content, Execution.class);

				execution.setId(null);
				execution.setUserId(user.getId());
				execution.setCreationDate(null);
				execution.setLastChangeDate(null);
				
				executionService.save(execution);
				
				flashMessageService.success(ra, Messages.EXECUTION_UPLOADED_SUCCESS);
			} catch (AbstractRedirectException ex) {
				flashMessageService.error(ra, ex);
			}
		}
		
		return "redirect:/home/";
	}
}