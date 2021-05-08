package org.nautilus.web.controller;

import java.nio.file.Path;

import javax.validation.Valid;

import org.nautilus.core.model.Instance;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.dto.UploadFileDTO;
import org.nautilus.web.exception.InstanceNotFoundException;
import org.nautilus.web.service.FileService;
import org.nautilus.web.service.InstanceService;
import org.nautilus.web.service.PluginService;
import org.nautilus.web.util.Messages;
import org.nautilus.web.util.Redirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instances")
public class InstanceController {
	
    @Autowired
	private FileService fileService;
	
	@Autowired
	private InstanceService instanceService;
	
	@Autowired
    private PluginService pluginService;
	
	@Autowired
	private Redirect redirect;
	
    @GetMapping("")
    public String view(Model model) {

        model.addAttribute("problems", instanceService.getProblemAndInstances());

        return "instances/instances";
    }
	
    @GetMapping("/upload/{problemId:.+}")
    public String formUpload(Model model, @PathVariable String problemId, UploadFileDTO uploadFileDTO) {

        model.addAttribute("problem", pluginService.getProblemById(problemId));
        model.addAttribute("uploadFileDTO", uploadFileDTO);

        return "instances/form-upload-instance";
    }
    
	@PostMapping("/upload/{problemId:.+}/save")
    public String save(Model model, RedirectAttributes ra,
            @PathVariable String problemId,
            @Valid UploadFileDTO uploadFileDTO, 
            BindingResult form) {

        if (form.hasErrors()) {
            return formUpload(model, problemId, uploadFileDTO);
        }

        Path file = instanceService.save(problemId, uploadFileDTO);

        return redirect.to("/instances").withSuccess(ra, Messages.FILE_UPLOADED_SUCCESS, file.getFileName().toString());
    }
	
	@GetMapping("/download/{problemId:.+}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String problemId, @PathVariable String filename) {

        Resource file = instanceService.getFileAsResource(problemId, filename);
          
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename()+ "\"")
                .body(file);
    }
	
	@PostMapping("/delete/{problemId:.+}/{filename:.+}")
    public String delete(Model model, RedirectAttributes ra,
            @PathVariable String problemId, 
            @PathVariable String filename){
        
	    Path file = instanceService.delete(problemId, filename);
	     
        return redirect.to("/instances").withSuccess(ra, Messages.FILE_DELETED_SUCCESS, file.getFileName().toString());
    }
	
	@GetMapping("/{problemId:.+}/{filename:.+}")
    public String view(Model model,  
            @PathVariable String problemId, 
            @PathVariable String filename){
        
        ProblemExtension problemExtension = pluginService.getProblemById(problemId);
        
        if (!fileService.instanceExists(problemId, filename)) {
            throw new InstanceNotFoundException();
        }
        
        Path path = fileService.getInstanceLocation(problemId, filename);
        
        Instance instance = problemExtension.getInstance(path);
        
        model.addAttribute("problem", problemExtension);
        model.addAttribute("tabs", instance.getTabs(instance));
    
        return "instance";
    }
}
