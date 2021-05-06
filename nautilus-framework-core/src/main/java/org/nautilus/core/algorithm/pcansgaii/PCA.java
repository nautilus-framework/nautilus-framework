package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;
import org.jfree.chart.util.ExportUtils;
import org.uma.jmetal.solution.Solution;

public class PCA {
    
    protected List<String> objectivesToBeOptimized;

    public PCA(List<String> objectivesToBeOptimized) {
        this.objectivesToBeOptimized = objectivesToBeOptimized;
    }

    public List<String> execute(List<? extends Solution<?>> population) {

        RealMatrix m = RealMatrixUtils.getRealMatrix(population);

        RealMatrix x = RealMatrixUtils.getStandardizedMatrix(m);

        RealMatrix v = RealMatrixUtils.getCovarianceMatrix(x);

        RealMatrix r = RealMatrixUtils.getCorrelationMatrix(v);
        
        //PrintUtils.matrix("Correlation Matrix", r);
        
        
        
        
        //ExportUtils.toDot(objectivesToBeOptimized, r);
        
        
        List<Eigen> eigens = new GenerateEigen().execute(r);
        
        List<PC> pcs = new GenerateEigenvalueAnalysis().execute(eigens);
        
        Map<Integer, List<Integer>> selected = new GenerateEffectOfMultiplePrincipalComponents().execute(pcs);
        
        List<Integer> nextIndexes = new GenerateFinalReductionUsingTheCorrelationMatrix().execute(r, pcs, selected);
        
        List<String> nextObjectives = new ArrayList<>();
        
        for(Integer i : nextIndexes) {
            nextObjectives.add(objectivesToBeOptimized.get(i));
        }
        
        return nextObjectives;
    }
}
