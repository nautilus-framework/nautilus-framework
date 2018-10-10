package thiagodnf.nautilus.core.correlation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public class SpearmanCorrelation extends Correlation {

	@Override
	public String getName() {
		return "Spearman's Correlation";
	}

//	@Override
//	public List<String> correlate(List<AbstractObjective> objectives, List<Solution> solutions) {
//		
//		for(Solution s : solutions) {
//			
//			Solution 
//			
//			for(Variable v : s.getVariables()) {
//				
//			}
//		}
//		
//		
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public List<CorrelationItem> correlateObjectives(List<AbstractObjective> objectives, List<Solution> solutions) {
		
		Preconditions.checkNotNull(solutions, "The solution list should not be null");
		Preconditions.checkNotNull(objectives, "The objective list should not be null");
		
		List<CorrelationItem> items = new ArrayList<>();

		for (int i = 0; i < objectives.size(); i++) {

			CorrelationItem item = new CorrelationItem();

			item.setName(objectives.get(i).getName());

			for (int j = 0; j < objectives.size(); j++) {

					
				System.out.println(objectives.get(i).getName() +"-"+ objectives.get(j).getName());

				List<Double> xs = new ArrayList<>();
				List<Double> ys = new ArrayList<>();

				for (Solution s : solutions) {
					xs.add(s.getObjective(i));
					ys.add(s.getObjective(j));
				}

				SpearmansCorrelation correlation = new SpearmansCorrelation();

				double[] x = Converter.toDoubleArray(xs);
				double[] y = Converter.toDoubleArray(ys);

				System.out.println(xs);
				System.out.println(ys);
				System.out.println("---------");
				
				item.getValues().add(correlation.correlation(x, y));
			}

			items.add(item);
		}

		return items;
	}

	@Override
	public List<String> correlatVariables(List<AbstractObjective> objectives, List<Solution> solutions) {
		// TODO Auto-generated method stub
		return null;
	}

}
