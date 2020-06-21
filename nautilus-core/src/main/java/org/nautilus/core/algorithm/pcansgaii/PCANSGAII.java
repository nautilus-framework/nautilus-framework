package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.listener.AlgorithmListener;
import org.nautilus.core.listener.OnProgressListener;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

public class PCANSGAII<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII<S> implements AlgorithmListener {

    private static final long serialVersionUID = -5506118915771152075L;

    public interface ProblemListener{
        
        Problem<?> getNewProblem(List<String> selectedObjectives);
    }
    
    protected ProblemListener problemListener;
    
    protected List<List<String>> I;

    public PCANSGAII(
            Problem<S> problem, 
            int maxEvaluations, 
            int populationSize,
            CrossoverOperator<S> crossoverOperator, 
            MutationOperator<S> mutationOperator,
            SelectionOperator<List<S>, S> selectionOperator,
            List<String> objectives
        ) {
        super(
            problem, 
            maxEvaluations, 
            populationSize, 
            crossoverOperator, 
            mutationOperator, 
            selectionOperator,
            new DominanceComparator<>(), 
            new SequentialSolutionListEvaluator<S>()
        );
        
        this.I = new ArrayList<>();
        
        this.I.add(objectives);
    }
    
    public PCANSGAII(Builder builder) {
        this(
            builder.getProblem(),
            builder.getMaxEvaluations(),
            builder.getPopulationSize(),
            builder.getCrossover(),
            builder.getMutation(),
            builder.getSelection(),
            ((NProblem<?>)builder.getProblem()).getObjectives().stream().map(el -> el.getId()).collect(Collectors.toList())
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        
        // Step 1: Set an iteration counter t = 0 and initial set of I0 = {1, 2, 3, ..., M}
        
        int t = 0;
        
        // Step 2:Initialize a random population for all objectives in objectives the
        // set It, run an EMO, and obtain a population Pt.

        do {
            
            List<String> objectives = I.get(t);

            super.problem = (Problem<S>) problemListener.getNewProblem(objectives);

            super.run();

            List<Solution<?>> result = (List<Solution<?>>) getResult();

            // Perform a PCA analysis on Pt using It to choose a reduced set of objectives
            // It+1 using the predefine

            PCA pca = new PCA(objectives);

            List<String> nextObjectives = pca.execute(result);

            I.add(nextObjectives);

            // If It+1 = It, stop and declare the obtained front. Else set t = t + 1 and go
            // to Step 2.

            if (equals(I.get(t), I.get(t + 1))) {
                break;
            }

            t = t + 1;

        } while (true); 
    }
    
    private boolean equals(List<String> l1, List<String> l2) {

        if (l1.size() != l2.size()) {
            return false;
        }

        Set<String> setForL1 = new HashSet<>();
        Set<String> setForL2 = new HashSet<>();

        for (int i = 0; i < l1.size(); i++) {
            setForL1.add(l1.get(i));
            setForL2.add(l2.get(i));
        }

        if (!setForL1.equals(setForL2)) {
            return false;
        }

        return true;
    }
    
    public void setProblemListener(ProblemListener problemListener) {
        this.problemListener = problemListener;
    }

    public List<List<String>> getI() {
        return I;
    }
    
    
    private OnProgressListener onProgressListener;

    protected void updateProgress() {
        
        super.updateProgress();
        
        double progress = (((double) evaluations) / ((double) maxEvaluations)) * 100.0;
        
        if (onProgressListener != null) {
            onProgressListener.onProgress(progress);
        }
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }
}
