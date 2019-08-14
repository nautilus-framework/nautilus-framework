package test.thiagodnf.nautilus.core.reduction;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.problem.NIntegerProblem;
import thiagodnf.nautilus.core.encoding.solution.NIntegerSolution;
import thiagodnf.nautilus.core.gui.Tab;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.reduction.AbstractReduction;
import thiagodnf.nautilus.core.reduction.AbstractReduction.ItemForEvaluation;
import thiagodnf.nautilus.core.reduction.AbstractReduction.RankingItem;
import thiagodnf.nautilus.core.reduction.ConfidenceBasedReduction;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionAttribute;

public class TestConfidenceBasedReduction2 {

    static class FakeObjective extends AbstractObjective {

        private int index;

        public FakeObjective(int index) {
            this.index = index;
        }

        @Override
        public String getName() {
            return "Fake Objective " + index;
        }

        @Override
        public double calculate(Instance data, Solution<?> solution) {

            NIntegerSolution sol = (NIntegerSolution) solution;

            int total = 0;

            for (int i = 0; i < sol.getNumberOfVariables(); i++) {

                if (sol.getVariableValue(i) == index) {
                    total++;
                }
            }

            return (double) total / (double) sol.getNumberOfVariables();
        }
    }

    static class FakeIntegerProblem extends NIntegerProblem {

        private static final long serialVersionUID = 193373637332L;

        public FakeIntegerProblem(Instance data, List<AbstractObjective> objectives) {
            super(data, objectives);
        }
    };

    static class FakeInstance extends Instance {

        @Override
        public List<Tab> getTabs(Instance data) {
            return new ArrayList<>();
        }
    }

    private NIntegerSolution getIntegerSolution(double... objectiveValues) {

        List<String> objectiveIds = objectives.stream()
                .map(e -> e.getId())
                .collect(Collectors.toList());
        
        NIntegerSolution sol = new NIntegerSolution(objectiveValues.length, 1);

        sol.setVariableValue(0, 1);
        sol.setAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES, Converter.toJson(objectiveIds));
        
        for (int i = 0; i < objectiveValues.length; i++) {
            sol.setObjective(i, objectiveValues[i]);
        }

        return sol;
    }
    
    private ItemForEvaluation getFeedback(List<NSolution<?>> population, int solutionIndex, int objectiveIndex, double feedback) {

        ItemForEvaluation item = new ItemForEvaluation();

        item.setObjectiveIndex(objectiveIndex);
        item.setSolutionIndex(solutionIndex);
        item.setObjectiveValue(population.get(solutionIndex).getObjective(objectiveIndex));
        item.setFeedback(feedback);

        return item;
    }
    
    private boolean validate(List<NSolution<?>> population, List<ItemForEvaluation> feedbacks, int... objectiveIndexes) {
        
        List<RankingItem> items = reduction.execute(objectives, population, feedbacks);
        
        List<String> objectiveNames = objectives.stream()
                .map(e -> e.getId())
                .collect(Collectors.toList());
        
        List<Integer> selectedObjectives = items.stream()
                .filter(e -> e.isSelected())
                .map(e -> objectiveNames.indexOf(e.getObjectiveId()))
                .collect(Collectors.toList());
        
        if (selectedObjectives.size() != objectiveIndexes.length) {
            return false;
        }
        
        for (int i = 0; i < objectiveIndexes.length; i++) {

            if (!selectedObjectives.contains(objectiveIndexes[i])) {
                return false;
            }
        }
        
        return true;
    }

    private static List<AbstractObjective> objectives = new ArrayList<>();

    private static FakeInstance instance;

    private static FakeIntegerProblem problem;
    
    private static AbstractReduction reduction;

    static {
        objectives.add(new FakeObjective(1));
        objectives.add(new FakeObjective(2));
        objectives.add(new FakeObjective(3));

        instance = new FakeInstance();

        problem = new FakeIntegerProblem(instance, objectives);
        
        reduction = new ConfidenceBasedReduction();
    }

    private void printPopulation(List<NSolution<?>> population) {

        System.out.println("Population");
        
        for (int i = 0; i < population.size(); i++) {
            System.out.println(i + " - " + Arrays.toString(population.get(i).getObjectives()));
        }
    }
    
    @Test
    public void testScenario1() {

        List<NSolution<?>> population = Arrays.asList(
                getIntegerSolution(1.0, 1.0, 1.0)
        );
        
        List<ItemForEvaluation> feedbacks = null;

        feedbacks = Arrays.asList(
                getFeedback(population, 0, 2, -1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 1));  
        
        feedbacks = Arrays.asList(
                getFeedback(population, 0, 1, -1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 2));
        
        feedbacks = Arrays.asList(
                getFeedback(population, 0, 0, -1.0)
        );
        
        assertTrue(validate(population, feedbacks, 2, 2));
    }
    
    @Test
    public void testScenario4() {

        List<NSolution<?>> population = Arrays.asList(
                getIntegerSolution(1.0, 1.0, 0.0),
                getIntegerSolution(0.0, 0.0, 1.0)
        );
        
        List<ItemForEvaluation> feedbacks = null;

        feedbacks = Arrays.asList(
                getFeedback(population, 0, 2, -1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 1));  
        
        feedbacks = Arrays.asList(
                getFeedback(population, 1, 2, -1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 1, 2));  
    }
    
    @Test
    public void testScenario6() {

        List<NSolution<?>> population = Arrays.asList(
                getIntegerSolution(1.0, 1.0, 1.0),
                getIntegerSolution(0.0, 0.0, 1.0)
        );
        
        List<ItemForEvaluation> feedbacks = null;

        feedbacks = Arrays.asList(
                getFeedback(population, 0, 2, -1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 1)); 
        
        feedbacks = Arrays.asList(
                getFeedback(population, 0, 1, -1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 1, 2)); 
        
        feedbacks = Arrays.asList(
                getFeedback(population, 0, 2, 1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 1, 2)); 
    }
    
    @Test
    public void testScenario7() {
        
        List<NSolution<?>> population = Arrays.asList(
                getIntegerSolution(1.0, 1.0, 1.0),
                getIntegerSolution(0.8, 0.8, 0.8),
                getIntegerSolution(0.4, 0.4, 0.4),
                getIntegerSolution(0.0, 0.0, 0.0)
        );
        
        List<ItemForEvaluation> feedbacks = null;

        feedbacks = Arrays.asList(
                getFeedback(population, 0, 2, 1.0),
                getFeedback(population, 1, 2, -1.0),
                getFeedback(population, 2, 2, -1.0),
                getFeedback(population, 3, 2, 1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 1, 2));
        
        feedbacks = Arrays.asList(
                getFeedback(population, 0, 2, -1.0),
                getFeedback(population, 1, 2, 1.0),
                getFeedback(population, 2, 2, 1.0),
                getFeedback(population, 3, 2, -1.0)
        );
        
        assertTrue(validate(population, feedbacks, 0, 1));
    }
}
