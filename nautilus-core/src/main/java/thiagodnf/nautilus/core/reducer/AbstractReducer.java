package thiagodnf.nautilus.core.reducer;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import com.google.gson.Gson;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractReducer {
	
	public class RankingItem {

		public String objectiveId;

		private double value;

		public boolean selected;
		
		public RankingItem(String objectiveId, double value) {
			this(objectiveId, value, false);
		}
		
		public RankingItem(String objectiveId, double value, boolean selected) {
			this.objectiveId = objectiveId;
			this.value = value;
			this.selected = selected;
		}
		
		public void setValue(double value) {
			this.value = value;
		}
		
		public double getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return new Gson().toJson(this);
		}		
	}
	
	public String getId() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract List<RankingItem> execute(List<AbstractObjective> allObjectives, List<AbstractObjective> selectedObjectives, List<Solution<?>> solutions);
	
	public abstract String getName() ;
}
