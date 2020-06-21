package org.nautilus.plugin.spl.encoding.objective;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;
import org.uma.jmetal.solution.Solution;

public class NumberOfProductsObjective extends AbstractObjective {
	
	protected int numbersOfProducts;
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.numbersOfProducts = 0;
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		numbersOfProducts++;
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		return (double) numbersOfProducts / (double) instance.getNumberOfProducts();
	}
	
	@Override
	public String getName() {
		return "Number of Products";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
