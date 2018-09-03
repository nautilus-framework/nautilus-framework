package thiagodnf.nautilus.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Solution;
import thiagodnf.nautilus.web.service.ExecutionService;

@Controller
public class ContinueController {
	
	@Autowired
	private ExecutionService executionService;
	
	@GetMapping("/continue/{executionId}")
	public String view(Model model, @PathVariable("executionId") String executionId) {
		
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}
		
		Parameters parameters = execution.getParameters();
		
		List<String> objectiveKeys = parameters.getObjectiveKeys();
		
		List<Solution> selectedSolutions = new ArrayList<>();
		
		for(Solution sol : execution.getSolutions()) {
			if(sol.getProperties().containsKey("selected")) {
				selectedSolutions.add(sol);
			}
		}
		
		
		
		
		double[] rankingForUserSelection = new double[objectiveKeys.size()];

		for (Solution selectedSolution : selectedSolutions) {
			
			for (int i = 0; i < selectedSolution.getObjectives().size(); i++) {
				rankingForUserSelection[i] += selectedSolution.getObjectives().get(i);
			}
		}
		
		rankingForUserSelection = mean(rankingForUserSelection, selectedSolutions.size());
		
		
		
		model.addAttribute("rankingForUserSelection", Arrays.toString(rankingForUserSelection));
		
		
		double[] rankingForUserFeedback = new double[objectiveKeys.size()];

		for (Solution selectedSolution : selectedSolutions) {

			double feedback = 0.0;

			if (selectedSolution.getProperties().containsKey("feedback")) {
				feedback = Double.valueOf(selectedSolution.getProperties().get("feedback"));
			}
			
			for (int i = 0; i < selectedSolution.getObjectives().size(); i++) {
				rankingForUserFeedback[i] += selectedSolution.getObjectives().get(i) * feedback;
			}
		}
		
		rankingForUserFeedback = mean(rankingForUserFeedback, selectedSolutions.size());
		
		model.addAttribute("rankingForUserFeedback", Arrays.toString(rankingForUserFeedback));
		
		
		
		
		// --------------------
		
		
		double[] rankings = new double[objectiveKeys.size()];
		
		double alpha = 0.5;

		for (int i = 0; i < rankings.length; i++) {
			rankings[i] = alpha * rankingForUserSelection[i] + (1.0 - alpha) * rankingForUserFeedback[i];
		}
		
		
		Map<String, Double> ranking = new HashMap<>();

		for(int i=0;i<rankings.length;i++) {
			ranking.put(objectiveKeys.get(i), rankings[i]);
		}
		
		ranking = sort(ranking);
		
		
		
		Map<String, Double> selected = new HashMap<>();
		
		double minRanking = 0.0;
		//double range = 0.02;
		
		for(Entry<String, Double> entry : ranking.entrySet()) {
			
			double value = entry.getValue();
			
			if(value >= 0.9) {
				selected.put(entry.getKey(), entry.getValue());
			}
//			if((minRanking - value) <= range) {
//				selected.put(entry.getKey(), entry.getValue());
//				minRanking = value;
//			}
		}
		
		if(selected.isEmpty()) {
			selected.putAll(ranking);
		}
		
		selected = sort(selected);
		
		
		model.addAttribute("ranking", ranking);
		model.addAttribute("selected", selected);
		model.addAttribute("execution", execution);
		
		return "continue";
	}
	
	protected Map<String, Double> sort(Map<String, Double> ranking) {
		 return ranking.entrySet().stream()
	                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	}
	
	protected double[] mean(double[] array, double size) {
		
		if (size == 0) {
			return array;
		}

		double[] newArray = new double[array.length];

		for (int i = 0; i < array.length; i++) {
			newArray[i] = 1.0 - array[i] / size;
		}

		return newArray;
	}
}
