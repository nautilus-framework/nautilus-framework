package thiagodnf.nautilus.core.reducer;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class AlphaReducer extends AbstractReducer {

	@Override
	public String getName() {
		return "Alpha Reducer";
	}

	@Override
	public List<Ranking> execute(List<AbstractObjective> allObjectives, 
			List<AbstractObjective> selectedObjectives,
			List<? extends Solution<?>> solutions) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}
