package org.nautilus.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.encoding.problem.NBinaryProblem;
import org.nautilus.core.encoding.problem.NIntegerProblem;
import org.nautilus.core.indicator.HypervolumeApprox;
import org.nautilus.core.normalize.AbstractNormalize;
import org.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.qualityattribute.RHypervolume;
import thiagodnf.rnsgaii.qualityattribute.RInvertedGenerationalDistance;
import thiagodnf.rnsgaii.qualityattribute.RSpread;
import thiagodnf.rnsgaii.rmetric.RMetric;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class SolutionListUtils {
	
	@SuppressWarnings("unchecked")
	public static List<String> getObjectives(NSolution<?> solution){
		
		String value = (String) solution.getAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES);
		
		return Converter.fromJson(value, List.class);
	}
	
	public static List<NSolution<?>> getSelectedSolutions(List<? extends NSolution<?>> solutions) {
		return getSelectedSolutions(solutions, true);
	}
	
	public static List<NSolution<?>> getSelectedSolutions(List<? extends NSolution<?>> solutions, boolean isCopied) {

		List<NSolution<?>> selectedSolutions = new ArrayList<>();

		for (NSolution<?> sol : solutions) {

			Object value = sol.getAttribute(SolutionAttribute.SELECTED);

			if (value != null && (boolean) value == true) {

				if (isCopied) {
					selectedSolutions.add((NSolution<?>) sol.copy());
				} else {
					selectedSolutions.add(sol);
				}
			}
		}

		return selectedSolutions;
	}

	public static List<NSolution<?>> getVisualizedSolutions(List<NSolution<?>> solutions) {

		List<NSolution<?>> visualizedSolutions = new ArrayList<>();

		for (NSolution<?> sol : solutions) {
			
			Object value = sol.getAttribute(SolutionAttribute.VISUALIZED);

			if (value != null && (boolean) value == true) {
				visualizedSolutions.add((NSolution<?>) sol.copy());
			}
		}

		return visualizedSolutions;
	}
	
    public static List<List<Double>> getDefaultReferencePoints(int numberOfObjectives){
	    
	    List<List<Double>> referencePoints = new ArrayList<>();
	    
	    referencePoints.add(Collections.nCopies(numberOfObjectives, 0.5));
	    
	    return referencePoints;   
	}
    
    public static double[] getMinimumObjectiveValues(List<? extends NSolution<?>> population) {

        int numberOfObjectives = population.get(0).getNumberOfObjectives();

        double[] fmin = new double[numberOfObjectives];

        for (int i = 0; i < fmin.length; i++) {
            fmin[i] = Double.POSITIVE_INFINITY;
        }

        for (NSolution<?> sol : population) {
            for (int i = 0; i < numberOfObjectives; i++) {
                fmin[i] = Math.min(fmin[i], sol.getObjective(i));
            }
        }

        return fmin;
    }

    public static double[] getMaximumObjectiveValues(List<? extends NSolution<?>> population) {

        int numberOfObjectives = population.get(0).getNumberOfObjectives();

        double[] fmax = new double[numberOfObjectives];

        for (int i = 0; i < fmax.length; i++) {
            fmax[i] = Double.NEGATIVE_INFINITY;
        }

        for (NSolution<?> sol : population) {
            for (int i = 0; i < numberOfObjectives; i++) {
                fmax[i] = Math.max(fmax[i], sol.getObjective(i));
            }
        }

        return fmax;
    }
    
    public static List<NSolution<?>> recalculate(NProblem<?> problem, List<NSolution<?>> solutions) {

        List<String> objectiveIds = problem.getObjectives()
                .stream()
                .map(e -> e.getId())
                .collect(Collectors.toList());
        
        List<NSolution<?>> finalSolutions = new ArrayList<>();

        for (NSolution<?> solution : solutions) {

            NSolution<?> newSolution = (NSolution<?>) Converter.toSolutionWithOutObjectives(problem, solution);

            if (problem instanceof NBinaryProblem) {
                ((NBinaryProblem) problem).evaluate((BinarySolution) newSolution);
            }
            if (problem instanceof NIntegerProblem) {
                ((NIntegerProblem) problem).evaluate((IntegerSolution) newSolution);
            }
            
            newSolution.getAttributes().clear();
            newSolution.setAttribute(SolutionAttribute.ID, solution.getAttribute(SolutionAttribute.ID));
            newSolution.setAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES, objectiveIds);

            finalSolutions.add(newSolution);
        }

        return finalSolutions;
    }
    
    public static void printObjectiveValues(List<NSolution<?>> solutions) {
        
        for(Solution<?> solution : solutions) {
            System.out.println(Arrays.toString(solution.getObjectives()));
        }
    }
    
    public static List<NSolution<?>> getNondominatedSolutions(List<NSolution<?>> solutions) {

        List<NSolution<?>> nonDominated = org.uma.jmetal.util.SolutionListUtils.getNondominatedSolutions(solutions);

        for (int i = 0; i < nonDominated.size(); i++) {

            NSolution<?> sol = nonDominated.get(i);

            Object s = sol.getAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES);

            sol.getAttributes().clear();

            sol.setAttribute(SolutionAttribute.ID, i);
            sol.setAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES, s);
        }

        return nonDominated;
    }
    
    public static List<PointSolution> getPointSolutions(List<NSolution<?>> solutions) {

        List<PointSolution> points = new ArrayList<>();

        for (Solution<?> s : solutions) {
            points.add(PointSolutionUtils.createSolution(s.getObjectives()));
        }

        return points;
    }
    
    public static Map<String, Number> calculateMetrics(
            NProblem<?> problem, 
            List<NSolution<?>> pfApprox,
            List<NSolution<?>> solutions,
            PointSolution zr,
            double delta){
            
        AbstractNormalize normalizer = new ByMaxAndMinValuesNormalize();
        
        List<NSolution<?>> normalizedPf = (List<NSolution<?>>) normalizer.normalize(problem.getObjectives(), solutions);
        List<NSolution<?>> normalizedPfApprox = (List<NSolution<?>>) normalizer.normalize(problem.getObjectives(), pfApprox);
            
        PointSolution normalizedZr = (PointSolution) normalizer.normalize(problem.getObjectives(), zr);
        
        RMetric rMetric = new RMetric(normalizedZr, delta);
        
        List<PointSolution> pfApproxSolutions = getPointSolutions(normalizedPfApprox);
        List<PointSolution> pfSolutions = getPointSolutions(normalizedPf);
        List<PointSolution> transferredPfSolutions = rMetric.execute(pfSolutions);
        
        // Trim the pareto-front values
        
        PointSolution zp = rMetric.pivotPointIdentification(pfApproxSolutions);
        
        List<PointSolution> trimmedPfApproxSolutions = rMetric.trimmingProcedure(zp, pfApproxSolutions);
        
        Front trimmedPfApproxFront = new ArrayFront(trimmedPfApproxSolutions);
        Front transferredPfFront = new ArrayFront(transferredPfSolutions);

        // Normalize the values
        
//        double maxValue = getMaxValue(trimmedPfApproxFront, transferredPfFront);
        double maxValue = 3.5;
        
        List<? extends Solution<?>> nadirPoints = PointSolutionUtils.getNadirPoint(problem.getNumberOfObjectives(), 0.0, maxValue);
        
        Front nadirPointFront = new ArrayFront(nadirPoints);
        
        FrontNormalizer frontNormalizer = new FrontNormalizer(nadirPointFront);
        
        Front normalizedPfApproxFront = frontNormalizer.normalize(trimmedPfApproxFront);
        Front normalizedPfFront = frontNormalizer.normalize(transferredPfFront);
        
        // Calculate the values
        
        Map<String, Number> values = new HashMap<>();
        
        values.put("r-hypervolume", (new RHypervolume(normalizedZr, delta, normalizedPfApproxFront).evaluate(normalizedPfFront)));
        values.put("r-igd", new RInvertedGenerationalDistance(normalizedZr, delta, normalizedPfApproxFront).evaluate(normalizedPfFront));
        values.put("r-spread", new RSpread(normalizedZr, delta, normalizedPfApproxFront).evaluate(normalizedPfFront));
        
        values.put("ed-mean-to-zr", getMeanEuclideanDistancetoPoint(normalizedPfFront, normalizedZr));
        values.put("ed-min-to-zr", getMinEuclideanDistancetoPoint(normalizedPfFront, normalizedZr));
        
        values.put("original-min-to-zr", getMinEuclideanDistance(normalizedPf, normalizedZr));
        
        // Trandicional Metrics
        
        values.put("hypervolume", new PISAHypervolume<PointSolution>(new ArrayFront(normalizedPfApprox)).evaluate(pfSolutions));
        values.put("igd", new InvertedGenerationalDistance<PointSolution>(new ArrayFront(normalizedPfApprox)).evaluate(pfSolutions));
        values.put("hypervolume-approx", new HypervolumeApprox<PointSolution>(new ArrayFront(normalizedPfApprox)).evaluate(pfSolutions));
        
        values.put("number-of-solutions", pfSolutions.size());
        values.put("number-of-solutions-in-roi", transferredPfSolutions.size());
        
        return values;
    }
    
    public static double getMeanEuclideanDistancetoPoint(Front front, PointSolution zr) {

        EuclideanDistance ed = new EuclideanDistance();

        double distance = 0.0;

        for (int i = 0; i < front.getNumberOfPoints(); i++) {

            Point p = front.getPoint(i);

            distance += ed.compute(p.getValues(), zr.getObjectives());
        }

        return (double) distance / (double) front.getNumberOfPoints();
    }
    
    public static double getMinEuclideanDistance(List<NSolution<?>> solutions, PointSolution zr) {

        EuclideanDistance ed = new EuclideanDistance();

        double distance = Double.MAX_VALUE;

        for (NSolution<?> solution : solutions) {

            double dist = ed.compute(solution.getObjectives(), zr.getObjectives());

            if (dist < distance) {
                distance = dist;
            }
        }

        return distance;
    }
    
    public static double getMinEuclideanDistancetoPoint(Front front, PointSolution zr) {

        EuclideanDistance ed = new EuclideanDistance();

        double distance = Double.MAX_VALUE;

        for (int i = 0; i < front.getNumberOfPoints(); i++) {

            Point p = front.getPoint(i);

            double dist =  ed.compute(p.getValues(), zr.getObjectives());
            
            if(dist < distance) {
                distance = dist;
            }
        }

        return distance;
    }
    
    public static double getMaxValue(Front transferredPfFront, Front trimmedPfApproxFront) {
        
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < transferredPfFront.getNumberOfPoints(); i++) {

            double[] values = transferredPfFront.getPoint(i).getValues();

            for (int j = 0; j < values.length; j++) {

                if (values[j] > maxValue) {
                    maxValue = values[j];
                }
            }
        }
        
        for (int i = 0; i < trimmedPfApproxFront.getNumberOfPoints(); i++) {

            double[] values = trimmedPfApproxFront.getPoint(i).getValues();

            for (int j = 0; j < values.length; j++) {

                if (values[j] > maxValue) {
                    maxValue = values[j];
                }
            }
        }
        
        return maxValue;
    }
}
