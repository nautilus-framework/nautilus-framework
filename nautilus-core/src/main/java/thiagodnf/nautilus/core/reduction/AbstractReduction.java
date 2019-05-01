package thiagodnf.nautilus.core.reduction;

import java.util.List;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
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
		
		public String getObjectiveId() {
			return objectiveId;
		}

		public void setObjectiveId(String objectiveId) {
			this.objectiveId = objectiveId;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public String toString() {
			return Converter.toJson(this);
		}		
	}
	
	public String getId() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract List<RankingItem> execute(NProblem<?> problem, List<NSolution<?>> population);
	
	public abstract String getName() ;
}
