package thiagodnf.nautilus.core.reduce;

import java.util.List;

import org.uma.jmetal.problem.Problem;

import com.google.gson.Gson;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractReduce {
	
	public class RankingItem {

		protected String objectiveId;

		protected double value;

		protected boolean selected;
		
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
		
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		public boolean isSelected() {
			return this.selected;
		}
		
		public String getObjectiveId() {
			return this.objectiveId;
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
	
	public abstract List<RankingItem> execute(Problem<?> problem, InstanceData data, List<AbstractObjective> allObjectives, List<AbstractObjective> selectedObjectives, List<NSolution<?>> solutions);
	
	public abstract String getName() ;
}
