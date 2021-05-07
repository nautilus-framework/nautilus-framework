package org.nautilus.web.controller;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.model.Execution;
import org.nautilus.web.service.ExecutionService;
import org.nautilus.web.service.FileService;
import org.nautilus.web.service.PluginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/download")
public class DownloadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	
	@GetMapping("/execution/{executionId:.+}/json")
	@ResponseBody
	public ResponseEntity<Resource> downloadExecutionAsJsonFile(
			@PathVariable("executionId") String executionId) {

		LOGGER.info("Downloading as json file the execution id {}", executionId);

		Execution execution = executionService.findExecutionById(executionId);
		
		String content = Converter.toJson(execution);

		Resource file = new ByteArrayResource(content.getBytes());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + executionId + ".json\"")
				.header(HttpHeaders.CONTENT_TYPE, "application/json")
				.header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
				.body(file);
	}
	
	@GetMapping("/execution/{executionId:.+}/fun")
	@ResponseBody
	public ResponseEntity<Resource> downloadExecutionAsFunFile(
			@PathVariable("executionId") String executionId) {

		LOGGER.info("Downloading as fun file the execution id {}", executionId);

		Execution execution = executionService.findExecutionById(executionId);
		
		String content = Converter.toFUN(execution.getSolutions());

		Resource file = new ByteArrayResource(content.getBytes());

		String filename = "FUN_" + executionId + ".txt";
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
				.header(HttpHeaders.CONTENT_TYPE, "text/plain")
				.header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
				.body(file);
	}
	
	@GetMapping("/execution/{executionId:.+}/var")
	@ResponseBody
	public ResponseEntity<Resource> downloadExecutionAsVarFile(
			@PathVariable("executionId") String executionId) {

		LOGGER.info("Downloading as json file the execution id {}", executionId);

		Execution execution = executionService.findExecutionById(executionId);
		
		String content = Converter.toVAR(execution.getSolutions());

		Resource file = new ByteArrayResource(content.getBytes());
		
		String filename = "VAR_" + executionId + ".txt";

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
				.header(HttpHeaders.CONTENT_TYPE, "text/plain")
				.header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
				.body(file);
	}
}
