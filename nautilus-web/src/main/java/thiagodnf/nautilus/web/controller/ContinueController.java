package thiagodnf.nautilus.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.core.reduction.AbstractReduction;
import thiagodnf.nautilus.core.reduction.AbstractReduction.RankingItem;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;
import thiagodnf.nautilus.web.dto.ContinueDTO;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class ContinueController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/continue/{executionId}")
	public String continueExecution( 
			@PathVariable String executionId,
			Model model) {
		
		Execution execution = executionService.findExecutionById(executionId);
		
		AlgorithmExtension algorithmExtension = pluginService.getAlgorithmExtensionById(execution.getAlgorithmId());
        
        AbstractReduction reduction = algorithmExtension.getReduction();

        if (reduction == null) {
            new RuntimeException("Reduction was not found");
        }
		
		List<RankingItem> rankings = reduction.execute(execution.getSolutions(), execution.getItemForEvaluations());
		
	    List<String> nextObjectiveIds = new ArrayList<>();

        for (RankingItem item : rankings) {
            
            if (item.isSelected()) {
                nextObjectiveIds.add(item.getObjectiveId());
            }
        }
		
		model.addAttribute("execution", execution);
		model.addAttribute("rankings", rankings);
		model.addAttribute("continueDTO", new ContinueDTO(execution.getId(), nextObjectiveIds));
		
		return "continue";
	}
}
