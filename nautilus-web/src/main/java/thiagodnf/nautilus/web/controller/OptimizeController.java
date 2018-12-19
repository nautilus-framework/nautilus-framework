package thiagodnf.nautilus.web.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.OptimizeService;
import thiagodnf.nautilus.web.service.OptimizeService.ErrorStats;
import thiagodnf.nautilus.web.service.OptimizeService.Stats;
import thiagodnf.nautilus.web.service.OptimizeService.SuccessStats;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.WebSocketService;

@Controller
@RequestMapping("/optimize/{pluginId:.+}/{problemId:.+}/{filename:.+}")
public class OptimizeController {
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private WebSocketService webSocketService;
	
	@Autowired
	private OptimizeService asyncService;
	
	@GetMapping("")
	public String optimize(Model model, 
			@PathVariable("pluginId") String pluginId, 
			@PathVariable("problemId") String problemId, 
			@PathVariable("filename") String filename){
		
		Parameters parameters = new Parameters();
		
		parameters.setPluginId(pluginId);
		parameters.setProblemId(problemId);
		parameters.setFilename(filename);
		
		model.addAttribute("parameters", parameters);
		model.addAttribute("filename", filename);
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		
		model.addAttribute("algorithmFactory", pluginService.getAlgorithmFactory(pluginId));
		model.addAttribute("selectionFactory", pluginService.getSelectionFactory(pluginId));
		model.addAttribute("crossoverFactory", pluginService.getCrossoverFactory(pluginId));
		model.addAttribute("mutationFactory", pluginService.getMutationFactory(pluginId));
		
		model.addAttribute("objectiveGroups", pluginService.getObjectivesByGroups(pluginId, problemId));
		
		return "optimize";
	}
	
	@MessageMapping("/hello.{sessionId}")
    public Future<?> teste(
    		@Valid Parameters parameters, 
    		@DestinationVariable String sessionId) {
		
		CompletableFuture<Stats> future = asyncService.execute(parameters,sessionId);
		
		future.thenRun(() -> {
			
			try {
				Stats stats = future.join();
				
				if(stats instanceof SuccessStats) {
					webSocketService.sendTitle(sessionId, "Done. Redirecting...");
					webSocketService.sendProgress(sessionId, 100);
					webSocketService.sendDone(sessionId, stats.content);
				}else if(stats instanceof ErrorStats) {
					webSocketService.sendException(sessionId, stats.content);
				}
			}catch(CompletionException ex) {
				System.out.println("oi");
				ex.printStackTrace();
			}
		});
		
		return null;
	}
	
	@MessageExceptionHandler
	public void handleException(Throwable exception, @DestinationVariable String sessionId) {
		webSocketService.sendException(sessionId, exception.getMessage());
	}
}
