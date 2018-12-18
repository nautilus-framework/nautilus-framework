package thiagodnf.nautilus.core.reducer;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractReducer {
	
	public class Ranking {

		public String objectiveId;

		public double value;

		public boolean selected;
		
		public Ranking(String objectiveId, double value, boolean selected) {
			this.objectiveId = objectiveId;
			this.value = value;
			this.selected = selected;
		}
	}
	
	public String getId() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract List<Ranking> execute(List<AbstractObjective> allObjectives, List<AbstractObjective> selectedObjectives, List<? extends Solution<?>> solutions);
	
	public abstract String getName() ;
}
