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
import thiagodnf.nautilus.web.util.NormalizerUtils;

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
				
				double value = selectedSolution.getObjectives().get(i);

				
				rankingForUserSelection[i] += value;
			}
		}
		
		//System.out.println(Arrays.toString(rankingForUserSelection));
		
		rankingForUserSelection = meanInverse(rankingForUserSelection, selectedSolutions.size());
	//	System.out.println(Arrays.toString(rankingForUserSelection));
		
		rankingForUserSelection = NormalizerUtils.normalize(rankingForUserSelection, 1, 2, 0, 1);
		
		
		model.addAttribute("rankingForUserSelection", Arrays.toString(rankingForUserSelection));
		
		
		double[] rankingForUserFeedback = new double[objectiveKeys.size()];

		for (Solution selectedSolution : selectedSolutions) {

			double feedback = 0.0;

			if (selectedSolution.getProperties().containsKey("feedback")) {
				feedback = Double.valueOf(selectedSolution.getProperties().get("feedback"));
			}
			
			for (int i = 0; i < selectedSolution.getObjectives().size(); i++) {
				
				double value = selectedSolution.getObjectives().get(i);

				value = NormalizerUtils.normalize(value, 1, 2, 0, 1);
//				value = 1.0 - value;
				rankingForUserFeedback[i] = value;
						
//				if(feedback < 0) {
//					rankingForUserFeedback[i] += (value - feedback);
//				}else {
//					rankingForUserFeedback[i] += (value + feedback);
//				rankingForUserFeedback[i] += (feedback);
//				}
			}
		}
		
		System.out.println(Arrays.toString(rankingForUserFeedback));
		//rankingForUserFeedback = meanInverse(rankingForUserFeedback, selectedSolutions.size());
		System.out.println(Arrays.toString(rankingForUserFeedback));
		
		
		
		rankingForUserFeedback = NormalizerUtils.normalize(rankingForUserFeedback, 0, 1, -1, 2);
		
		//
//		System.out.println(Arrays.toString(rankingForUserFeedback));
		
		
		model.addAttribute("rankingForUserFeedback", Arrays.toString(rankingForUserFeedback));
		
		
		
		
		
		
		
		
		// --------------------
		
		
		double[] rankings = new double[objectiveKeys.size()];
		
		double alpha = 0.0;

		for (int i = 0; i < rankings.length; i++) {
			rankings[i] = alpha * rankingForUserSelection[i] + (1.0 - alpha) * rankingForUserFeedback[i];
		}
		
		
		Map<String, Double> rankingMap = new HashMap<>();

		for(int i=0;i<rankings.length;i++) {
			rankingMap.put(objectiveKeys.get(i), rankings[i]);
		}
		
		rankingMap = sort(rankingMap);
		
		
		
		
		
		
		
		double sum = rankingMap.values().stream().reduce(Double::sum).get();
		double average = (double) sum / (double) objectiveKeys.size();
		
		Map<String, Double> selectedMap = new HashMap<>();
		
		for(Entry<String, Double> entry : rankingMap.entrySet()) {
			
			double value = entry.getValue();
			
			if(value >= average) {
				selectedMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		if(selectedMap.isEmpty()) {
			selectedMap.putAll(rankingMap);
		}
		
		selectedMap = sort(selectedMap);
		
		
		List<String> selectedObjectives = selectedMap
				.keySet()
				.stream()
				.collect(Collectors.toList());
		
		Parameters nextParameters =  execution.getParameters();
		
		nextParameters.setObjectiveKeys(selectedObjectives);
		nextParameters.setLastExecutionId(executionId);
		
		model.addAttribute("rankingMap", rankingMap);
		model.addAttribute("selectedMap", selectedMap);
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
			newArray[i] = (array[i] / size);
		}

		return newArray;
	}
	
	protected double[] meanInverse(double[] array, double size) {
		
		if (size == 0) {
			return array;
		}

		double[] newArray = new double[array.length];

		for (int i = 0; i < array.length; i++) {
			newArray[i] = 1.0 - (array[i] / size);
		}

		return newArray;
	}
}
