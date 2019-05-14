package thiagodnf.nautilus.plugin.spl.encoding.objective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;

public class NewSimilarityObjective extends AbstractObjective {
	
	protected double sum;
	
	protected double count;
	
	protected List<Integer> selectedProducts;
	
	protected Map<String, Integer> indexes;
	
	protected int[] counts;
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.sum = 0.0;
		this.count = 0.0;
		this.indexes = new HashMap<>();
		this.selectedProducts = new ArrayList<>();
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		indexes = instance.getIndexesForOptionalFeatures();
	
		this.counts = new int[indexes.size()];
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		
		this.selectedProducts.add(i);
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		List<String> optionalFeatures = instance.getProductWithoutMandatoryFeatures(i);
		
		for (String optionalFeature : optionalFeatures) {

			int j = indexes.get(optionalFeature);
			
			this.counts[j] += 1;
		}
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {

		if (selectedProducts.size() == 0) {
			return 0.0;
		}

		double count = 0;
		
		for (int i = 0; i < counts.length; i++) {

			if (counts[i] > 1) {
				count++;
			}
		}
		
		System.out.println((double) count / (double) counts.length);
		
		return (double) count / (double) counts.length;
	}
	
	@Override
	public String getName() {
		return "Similarity";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
