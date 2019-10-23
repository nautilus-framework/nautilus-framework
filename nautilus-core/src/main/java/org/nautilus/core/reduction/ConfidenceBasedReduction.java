package org.nautilus.core.reduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.reduction.AbstractReduction.ItemForEvaluation;
import org.nautilus.core.reduction.AbstractReduction.RankingItem;
import org.nautilus.core.util.RandomUtils;
import org.nautilus.core.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

public class ConfidenceBasedReduction extends AbstractReduction {

	protected Map<String, Double> confiability;

	protected double minConfidenceLevel;

	public ConfidenceBasedReduction() {
		this(0.8);
	}
	
	public ConfidenceBasedReduction(double minConfidenceLevel) {
		
		this.minConfidenceLevel = minConfidenceLevel;
		this.confiability = new HashMap<>();
		
		this.confiability.put("1.0_1.0", 0.0);
		this.confiability.put("1.0_0.0", 0.2);
		this.confiability.put("1.0_-1.0", 0.0);

		this.confiability.put("0.0_1.0", 0.8);
		this.confiability.put("0.0_0.0", 0.5);
		this.confiability.put("0.0_-1.0", 0.2);

		this.confiability.put("-1.0_1.0", 1.0);
		this.confiability.put("-1.0_0.0", 0.8);
		this.confiability.put("-1.0_-1.0", 1.0);
	}
	
    public static double[] getMinimumObjectiveValues(List<AbstractObjective> objectives) {

        double[] fmin = new double[objectives.size()];

        for (int i = 0; i < objectives.size(); i++) {
            fmin[i] = objectives.get(i).getMinimumValue();
        }

        return fmin;
    }
    
    public static double[] getMaximumObjectiveValues(List<AbstractObjective> objectives) {

        double[] fmax = new double[objectives.size()];

        for (int i = 0; i < objectives.size(); i++) {
            fmax[i] = objectives.get(i).getMaximumValue();
        }

        return fmax;
    }
	
	
	@Override
    public List<RankingItem> execute(List<AbstractObjective> objectives, List<NSolution<?>> population, List<ItemForEvaluation> itemsForEvaluation) {

	    List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));

        double[] minValues = SolutionListUtils.getMinimumObjectiveValues(population);
        double[] maxValues = SolutionListUtils.getMaximumObjectiveValues(population);
        
        System.out.println("Maximum Values: " + Arrays.toString(maxValues));
        System.out.println("Minimum Values: " + Arrays.toString(minValues));
        
        ItemForEvaluation[] maxItem = new ItemForEvaluation[optimizedObjectives.size()];
        ItemForEvaluation[] minItem = new ItemForEvaluation[optimizedObjectives.size()];
        
        for (ItemForEvaluation item : itemsForEvaluation) {

            int objectiveIndex = item.getObjectiveIndex();
            
            double distToMaxValue = Math.abs(maxValues[objectiveIndex] - item.getObjectiveValue());
            double distToMinValue = Math.abs(minValues[objectiveIndex] - item.getObjectiveValue());
                    
            if (distToMaxValue < distToMinValue) {

                if (maxItem[objectiveIndex] == null || item.getObjectiveValue() > maxItem[objectiveIndex].getObjectiveValue()) {
                    maxItem[objectiveIndex] = item;
                } else if (item.getObjectiveValue() == maxItem[objectiveIndex].getObjectiveValue()) {
                    
                    if (item.getFeedback() < maxItem[objectiveIndex].getFeedback()) {
                        maxItem[objectiveIndex] = item;
                    }
                }
            }

            if (distToMinValue < distToMaxValue) {

                if (minItem[objectiveIndex] == null || item.getObjectiveValue() < minItem[objectiveIndex].getObjectiveValue()) {
                    minItem[objectiveIndex] = item;
                } else if(item.getObjectiveValue() == minItem[objectiveIndex].getObjectiveValue()){

                    if (item.getFeedback() < minItem[objectiveIndex].getFeedback()) {
                        minItem[objectiveIndex] = item;
                    }
                }
            }
            
            if (distToMinValue == distToMaxValue) {

                if (minItem[objectiveIndex] == null) {
                    minItem[objectiveIndex] = item;
                } else {

                    if (item.getFeedback() < minItem[objectiveIndex].getFeedback()) {
                        minItem[objectiveIndex] = item;
                    }
                }

                if (maxItem[objectiveIndex] == null) {
                    maxItem[objectiveIndex] = item;
                } else {
                    if (item.getFeedback() < maxItem[objectiveIndex].getFeedback()) {
                        maxItem[objectiveIndex] = item;
                    }
                }
            }
        }
        
        double[] worst = new double[optimizedObjectives.size()];
        double[] best = new double[optimizedObjectives.size()];
        double[] confidence = new double[optimizedObjectives.size()];

        for (int i = 0; i < optimizedObjectives.size(); i++) {
            
            if (maxItem[i] != null) {
                worst[i] = maxItem[i].getFeedback();
            }
            
            if (minItem[i] != null) {
                best[i] = minItem[i].getFeedback();
            }
        }
        
        for (int i = 0; i < optimizedObjectives.size(); i++) {
            confidence[i] = confidence(best[i], worst[i]);
        }
        
        System.out.println("Worst" + Arrays.toString(worst));
        System.out.println("Best" + Arrays.toString(best));
        System.out.println("Confidence" + Arrays.toString(confidence));
        
        List<Integer> selectedObjectives = new ArrayList<>();

        for (int i = 0; i < optimizedObjectives.size(); i++) {

            if (confidence[i] < minConfidenceLevel) {
                selectedObjectives.add(i);
            }
        }
        
        if (selectedObjectives.isEmpty()) {
            selectedObjectives.add(RandomUtils.randInt(0, optimizedObjectives.size() - 1));
        }
        
        List<RankingItem> rankings = new ArrayList<>();

        for (int i = 0; i < optimizedObjectives.size(); i++) {
            rankings.add(new RankingItem(optimizedObjectives.get(i), confidence[i]));
        }

        for (int i = 0; i < optimizedObjectives.size(); i++) {

            if (selectedObjectives.contains(i)) {
                rankings.get(i).setSelected(true);
            } else {
                rankings.get(i).setSelected(false);
            }
        }
        
        return rankings;
	    
	    
	    
	    
	    
	    
	    
//	    List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));
//
//	    
//        
//	    
//        double[] maxWorst = new double[optimizedObjectives.size()];
//        double[] maxBest = new double[optimizedObjectives.size()];
//
//        for (int i = 0; i < optimizedObjectives.size(); i++) {
//            maxWorst[i] = Double.NEGATIVE_INFINITY;
//            maxBest[i] = Double.NEGATIVE_INFINITY;
//        }
//        
//        for (ItemForEvaluation item : itemsForEvaluation) {
//
//            if (item.getObjectiveValue() == 0.0) {
//                maxBest[item.getObjectiveIndex()] = Math.max(maxBest[item.getObjectiveIndex()], item.getFeedback());
//            } else if (item.getObjectiveValue() == 1.0) {
//                maxWorst[item.getObjectiveIndex()] = Math.max(maxWorst[item.getObjectiveIndex()], item.getFeedback());
//            }
//        }
//        
//        double[] minValues = SolutionListUtils.getMinimumObjectiveValues(population);
//        double[] maxValues = SolutionListUtils.getMaximumObjectiveValues(population);
//        
//        for (int i = 0; i < optimizedObjectives.size(); i++) {
//            
//            if(minValues[i] == maxValues[i] && minValues[i] == 0.0) {
//                maxWorst[i] = maxBest[i];
//            }
//            if(minValues[i] == maxValues[i] && minValues[i] == 1.0) {
//                maxBest[i] = maxWorst[i] = 0.0;
//            }
//        }
//        
//        double[] worst = new double[optimizedObjectives.size()];
//        double[] best = new double[optimizedObjectives.size()];
//        
//        for (int i = 0; i < optimizedObjectives.size(); i++) {
//            
//            if (maxBest[i] == Double.NEGATIVE_INFINITY) {
//                best[i] = 0;
//            } else {
//                best[i] = maxBest[i];
//            }
//            
//            if (maxWorst[i] == Double.NEGATIVE_INFINITY) {
//                worst[i] = 0;
//            } else {
//                worst[i] = maxWorst[i];
//            }
//        }
//        
//        System.out.println(Arrays.toString(worst));
//        System.out.println(Arrays.toString(best));
//        
//        List<Integer> selectedObjectives = new ArrayList<>();
//
//        for (int i = 0; i < optimizedObjectives.size(); i++) {
//
//            if (confidence(best[i], worst[i]) < minConfidenceLevel) {
//                selectedObjectives.add(i);
//            }
//        }
//        
//        if (selectedObjectives.isEmpty()) {
//            selectedObjectives.add(RandomUtils.randInt(0, optimizedObjectives.size() - 1));
//        }
//        
//        List<RankingItem> rankings = new ArrayList<>();
//
//        for (int i = 0; i < optimizedObjectives.size(); i++) {
//            rankings.add(new RankingItem(optimizedObjectives.get(i), confidence(best[i], worst[i])));
//        }
//
//        for (int i = 0; i < optimizedObjectives.size(); i++) {
//
//            if(selectedObjectives.contains(i)) {
//                rankings.get(i).setSelected(true);
//            }else {
//                rankings.get(i).setSelected(false);
//            }
//        }
//        
//        return rankings;
	    
	}
//	@Override
//	public List<RankingItem> execute(NProblem<?> problem, List<NSolution<?>> population, List<ItemForEvaluation> itemsForEvaluation) {
//		return execute(population, itemsForEvaluation);
//	}

	private double confidence(double best, double worst) {
		return confiability.get(best + "_" + worst);
	}	
}

