package thiagodnf.nautilus.core.reduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.reduction.AbstractReduction.ItemForEvaluation;
import thiagodnf.nautilus.core.util.RandomUtils;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionListUtils;

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
	
	@Override
    public List<RankingItem> execute(List<NSolution<?>> population, List<ItemForEvaluation> itemsForEvaluation) {

	    List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));
        
//      List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(population); 
        
        double[] maxWorst = new double[optimizedObjectives.size()];
        double[] maxBest = new double[optimizedObjectives.size()];

        for (int i = 0; i < optimizedObjectives.size(); i++) {
            maxWorst[i] = Double.NEGATIVE_INFINITY;
            maxBest[i] = Double.NEGATIVE_INFINITY;
        }
        
//      for (NSolution<?> solution : selectedSolutions) {
//
//          for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
//
//              Object value = solution.getAttribute(SolutionAttribute.FEEDBACK_FOR_OBJECTIVE + i);
//
//              if (value == null) {
//                  value = 0.0;
//              }
//              
//              if (solution.getObjective(i) == 0.0) {
//                  best[i] = Math.max(best[i], (double) value);
//              } else if (solution.getObjective(i) == 1.0) {
//                  worst[i] = Math.max(worst[i], (double) value);
//              }
//          }
//      }
        
        for (ItemForEvaluation item : itemsForEvaluation) {

            if (item.getObjectiveValue() == 0.0) {
                maxBest[item.getObjectiveIndex()] = Math.max(maxBest[item.getObjectiveIndex()], item.getFeedback());
            } else if (item.getObjectiveValue() == 1.0) {
                maxWorst[item.getObjectiveIndex()] = Math.max(maxWorst[item.getObjectiveIndex()], item.getFeedback());
            }
        }
        
        double[] worst = new double[optimizedObjectives.size()];
        double[] best = new double[optimizedObjectives.size()];
        
        for (int i = 0; i < optimizedObjectives.size(); i++) {
            
            if (maxBest[i] == Double.NEGATIVE_INFINITY) {
                best[i] = 0;
            } else {
                best[i] = maxBest[i];
            }
            
            if (maxWorst[i] == Double.NEGATIVE_INFINITY) {
                worst[i] = 0;
            } else {
                worst[i] = maxWorst[i];
            }
        }
        
        System.out.println(Arrays.toString(worst));
        System.out.println(Arrays.toString(best));
        
        List<Integer> selectedObjectives = new ArrayList<>();

        for (int i = 0; i < optimizedObjectives.size(); i++) {

            if (confidence(best[i], worst[i]) < minConfidenceLevel) {
                selectedObjectives.add(i);
            }
        }
        
        if (selectedObjectives.isEmpty()) {
            selectedObjectives.add(RandomUtils.randInt(0, optimizedObjectives.size() - 1));
        }
        
        List<RankingItem> rankings = new ArrayList<>();

        for (int i = 0; i < optimizedObjectives.size(); i++) {
            rankings.add(new RankingItem(optimizedObjectives.get(i), confidence(best[i], worst[i])));
        }

        for (int i = 0; i < optimizedObjectives.size(); i++) {

            if(selectedObjectives.contains(i)) {
                rankings.get(i).setSelected(true);
            }else {
                rankings.get(i).setSelected(false);
            }
        }
        
        return rankings;
	    
	}
	@Override
	public List<RankingItem> execute(NProblem<?> problem, List<NSolution<?>> population, List<ItemForEvaluation> itemsForEvaluation) {
		return execute(population, itemsForEvaluation);
	}

	private double confidence(double best, double worst) {
		return confiability.get(best + "_" + worst);
	}	
}

