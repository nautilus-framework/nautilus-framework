package thiagodnf.nautilus.web.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import thiagodnf.nautilus.core.distance.EuclideanDistance;
import thiagodnf.nautilus.core.model.Variable;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/done")
public class DoneController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	@GetMapping("/{executionId}")
	public String view(Model model, @PathVariable("executionId") String executionId) throws IOException {
		
		Execution execution = executionService.findById(executionId);

		Parameters parameters = execution.getParameters();
		
		String problemKey = parameters.getProblemKey();
	
		AbstractPlugin plugin = pluginService.getPlugin(problemKey);
		
		List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, parameters.getObjectiveKeys());
		
		double[] minimumValues = new double[objectives.size()];
		double[] maximumValues = new double[objectives.size()];
		
		for (int i = 0; i < objectives.size(); i++) {
			minimumValues[i] = objectives.get(i).getMinimumValue();
			maximumValues[i] = objectives.get(i).getMaximumValue();
		}
		
		Path instance = fileService.getInstancesFile(problemKey, parameters.getFilename());
    	
		Problem problem = plugin.getProblem(instance, objectives);
		
		List<? extends Solution<?>> solutions = Converter.toJMetalSolutions(problem, execution.getSolutions());
		
		//
		
	    FrontNormalizer frontNormalizer = new FrontNormalizer(minimumValues, maximumValues) ;

	    Front referenceFront = new ArrayFront(1, objectives.size());
	    
		for (int i = 0; i < 1; i++) {
			
			Point point = new ArrayPoint(objectives.size());
			
			for (int j = 0; j < objectives.size(); j++) {
				point.setValue(j, objectives.get(j).getMinimumValue());
				
				if(j == 0 || j == 9) {
					point.setValue(j, -0.5);
				}
			}

			referenceFront.setPoint(i, point);
		}
	    
	    Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront) ;
	    
	    Front normalizedFront = frontNormalizer.normalize(new ArrayFront(solutions)) ;
	    
	    List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront) ;	
	
	    
	    double number1 = 0.0;
	    double number2 = 0.0;
	    double number3 = 0.0;
	    double number4 = 0.0;
	    int variables = 0;
	    
	    for(thiagodnf.nautilus.core.model.Solution s : execution.getSolutions()) {
	    	
	    	for(Variable v : s.getVariables()) {
	    		
	    		if(v.getValue().equalsIgnoreCase("1")) {
	    			number1++;
	    		}
	    		if(v.getValue().equalsIgnoreCase("2")) {
	    			number2++;
	    		}
	    		if(v.getValue().equalsIgnoreCase("3")) {
	    			number3++;
	    		}
	    		if(v.getValue().equalsIgnoreCase("4")) {
	    			number4++;
	    		}
	    		
	    		variables++;
	    	}
	    }
	    
	    number1 = (double) number1 / (double) variables;
	    number2 = (double) number2 / (double) variables;
	    number3 = (double) number3 / (double) variables;
	    number4 = (double) number4 / (double) variables;
	    
	   
	    model.addAttribute("number1", number1);
	    model.addAttribute("number2", number2);
	    model.addAttribute("number3", number3);
	    model.addAttribute("number4", number4);
	    
	   
	    
	    //
	    
	    model.addAttribute("objectives", objectives);
		model.addAttribute("plugin", plugin);
		model.addAttribute("execution", execution);
		model.addAttribute("hypervolume", new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("gd", new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("igd", new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("epsilon", new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("spread", new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("euclideanDistance", EuclideanDistance.calculate(normalizedReferenceFront, normalizedFront));
		
		
		
		return "done";
	}
}
