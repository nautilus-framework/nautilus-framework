package thiagodnf.nautilus.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.dto.OptimizeDTO;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.OptimizeService;
import thiagodnf.nautilus.web.service.OptimizeService.ErrorStats;
import thiagodnf.nautilus.web.service.OptimizeService.Stats;
import thiagodnf.nautilus.web.service.OptimizeService.SuccessStats;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.WebSocketService;

@Controller
@RequestMapping("/optimize/{problemId:.+}/{instance:.+}")
public class OptimizeController {
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private WebSocketService webSocketService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private OptimizeService asyncService;
	
	@GetMapping("")
	public String optimize(Model model, 
			@PathVariable String problemId, 
			@PathVariable String instance){
		
		ProblemExtension problem = pluginService.getProblemById(problemId);
		
		User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("problem", problem);
		model.addAttribute("instance", instance);
		model.addAttribute("algorithms", pluginService.getAlgorithms());
		model.addAttribute("crossovers", pluginService.getCrossovers());
		model.addAttribute("mutations", pluginService.getMutations());
		model.addAttribute("selections", pluginService.getSelections());
		model.addAttribute("optimizeDTO", new OptimizeDTO(user.getId(), problem.getId(), instance));
		
		return "optimize";
	}
	
	@MessageMapping("/hello.{sessionId}")
    public Future<?> teste(
    		@Valid OptimizeDTO optimizeDTO, 
    		@DestinationVariable String sessionId){
		
		CompletableFuture<Stats> future = asyncService.execute(optimizeDTO, sessionId);
		
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
		
		if (exception instanceof MethodArgumentNotValidException) {

			MethodArgumentNotValidException ex = (MethodArgumentNotValidException) exception;

			List<String> messages = new ArrayList<>();

			for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
				messages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
			}

			webSocketService.sendException(sessionId, messages.toString());
		} else {
			webSocketService.sendException(sessionId, exception.getMessage());
		}
	}
}
