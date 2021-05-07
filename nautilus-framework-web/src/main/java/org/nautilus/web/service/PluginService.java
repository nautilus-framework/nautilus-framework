package org.nautilus.web.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.util.Strings;
import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.encoding.problem.NBinaryProblem;
import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.extension.AlgorithmExtension;
import org.nautilus.plugin.extension.CorrelationExtension;
import org.nautilus.plugin.extension.CrossoverExtension;
import org.nautilus.plugin.extension.MutationExtension;
import org.nautilus.plugin.extension.NormalizerExtension;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.RemoverExtension;
import org.nautilus.plugin.extension.SelectionExtension;
import org.nautilus.web.exception.ProblemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import lombok.Getter;

@Getter
@Service
public class PluginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginService.class);
	
	@Autowired
	private FileService fileService;

	private Map<String, ProblemExtension> problems = new TreeMap<>();
	
	private Map<String, AlgorithmExtension> algorithms = new TreeMap<>();
	
	private Map<String, CrossoverExtension> crossovers = new TreeMap<>();
	
	private Map<String, MutationExtension> mutations = new TreeMap<>();
	
	private Map<String, SelectionExtension> selections = new TreeMap<>();
	
	private Map<String, NormalizerExtension> normalizers = new TreeMap<>();
	
	private Map<String, CorrelationExtension> correlations = new TreeMap<>();
	
	private Map<String, RemoverExtension> removers = new TreeMap<>();
    
    public List<ProblemExtension> getProblemsSorted() {
        
        List<ProblemExtension> problems = new ArrayList<>(this.problems.values());
        
        Collections.sort(problems, (ProblemExtension o1, ProblemExtension o2) -> o1.getName().compareTo(o2.getName()));
        
        return problems;
    }
	
	public ProblemExtension getProblemById(String id) {
		
        if (Strings.isBlank(id) || !problems.containsKey(id)) {
            throw new ProblemNotFoundException();
        }

        return problems.get(id);
	}
	
	
	public AlgorithmExtension getAlgorithmExtensionById(String id) {

		if (algorithms.containsKey(id)) {
			return algorithms.get(id);
		}

		throw new RuntimeException("The algorithm was not found");
	}
	
	public SelectionExtension getSelectionExtensionById(String id) {

		if (selections.containsKey(id)) {
			return selections.get(id);
		}

		throw new RuntimeException("The selection was not found");
	}
	
	public CrossoverExtension getCrossoverExtensionById(String id) {

		if (crossovers.containsKey(id)) {
			return crossovers.get(id);
		}

		throw new RuntimeException("The crossover was not found");
	}
	
	public MutationExtension getMutationExtensionById(String id) {

		if (mutations.containsKey(id)) {
			return mutations.get(id);
		}

		throw new RuntimeException("The mutations was not found");
	}
	
	public NormalizerExtension getNormalizerExtensionById(String id) {

        if (normalizers.containsKey(id)) {
            return normalizers.get(id);
        }

        throw new RuntimeException("The normalizer was not found");
    }
	
	public CorrelationExtension getCorrelationExtensionById(String id) {

        if (correlations.containsKey(id)) {
            return correlations.get(id);
        }

        throw new RuntimeException("The correlation was not found");
    }
	
	public RemoverExtension getRemoverExtensionById(String id) {

        if (removers.containsKey(id)) {
            return removers.get(id);
        }

        throw new RuntimeException("The removers was not found");
    }
	
	
	public Solution<?> getSolution(String problemId, String instanceId, List<String> variables){
	    
        Path path = fileService.getInstanceLocation(problemId, instanceId);

        ProblemExtension problemExtension = getProblemById(problemId);

        Instance instance = problemExtension.getInstance(path);

        List<AbstractObjective> objectives = problemExtension.getObjectives();

        NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);
	    
	    if (problem instanceof NBinaryProblem) {
            
            NBinaryProblem pro = (NBinaryProblem) problem;

            NBinarySolution sol = (NBinarySolution) pro.createSolution();

            BinarySet set = new BinarySet(sol.getTotalNumberOfBits());

            set.clear();

            for (String variable : variables) {
                set.set(Integer.valueOf(variable), true);
            }

            sol.setVariableValue(0, set);
            
            pro.evaluate(sol);
           
            return sol;
        }
	    
	    return null;
	}
	
}
