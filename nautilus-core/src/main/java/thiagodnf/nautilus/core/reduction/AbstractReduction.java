package thiagodnf.nautilus.core.reduction;

import java.util.List;

import org.uma.jmetal.problem.Problem;

import com.google.gson.Gson;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractReduction {
	
	public static class Interaction {
		
		protected int objectiveIndex;
		
		protected int solutionIndex;
		
		public Interaction(int objectiveIndex, int solutionIndex) {
			this.objectiveIndex = objectiveIndex;
			this.solutionIndex = solutionIndex;
		}

		public int getSolutionIndex() {
			return solutionIndex;
		}

		public void setSolutionIndex(int solutionIndex) {
			this.solutionIndex = solutionIndex;
		}

		public int getObjectiveIndex() {
			return objectiveIndex;
		}

		public void setObjectiveIndex(int objectiveIndex) {
			this.objectiveIndex = objectiveIndex;
		}
		
		public String toString() {
			return Converter.toJson(this);
		}
	}
	
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
	
	public abstract List<RankingItem> execute(Problem<?> problem, InstanceData data, List<AbstractObjective> allObjectives, List<NSolution<?>> population);
	
	public abstract String getName() ;
}
