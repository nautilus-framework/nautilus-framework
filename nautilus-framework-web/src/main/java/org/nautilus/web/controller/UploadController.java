package org.nautilus.web.controller;

import javax.validation.Valid;

import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.dto.UploadExecutionDTO;
import org.nautilus.web.dto.UploadFileDTO;
import org.nautilus.web.exception.AbstractRedirectException;
import org.nautilus.web.model.Execution;
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
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/execution")
    public String formUploadExecution(Model model, UploadExecutionDTO uploadExecutionDTO) {
        
        model.addAttribute("uploadExecutionDTO", uploadExecutionDTO);
        
        return "form-upload-execution";
    }
	
	@PostMapping("/execution/")
    public String uploadExecution(
            @Valid UploadExecutionDTO uploadExecutionDTO, 
            BindingResult result, 
            RedirectAttributes ra,
            Model model) {

        LOGGER.info("Uploading file: {}", uploadExecutionDTO.getFile().getOriginalFilename());

        if (result.hasErrors()) {
            return formUploadExecution(model, uploadExecutionDTO);
        }
            
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
        
        return "redirect:/home/";
    }
}
