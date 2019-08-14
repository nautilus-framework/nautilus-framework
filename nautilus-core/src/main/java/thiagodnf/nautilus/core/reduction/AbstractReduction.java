package thiagodnf.nautilus.core.reduction;

import java.util.List;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractReduction {
	
	public static class ItemForEvaluation {
		
		protected int solutionIndex;
		
		protected int objectiveIndex;
		
		protected double objectiveValue;
		
		protected double feedback;
		
		/** 
		 * Constructor. Default value = 0.0
		 * 
		 * @param solutionIndex the solution index
		 * @param objectiveIndex the objective index
		 */
		public ItemForEvaluation(int solutionIndex, int objectiveIndex) {
			this(solutionIndex, objectiveIndex, 0.0);
		}

		/**
		 * Constructor
		 * 
		 * @param solutionIndex the solution index
		 * @param objectiveIndex the objective index
		 * @param objectiveValue the objective value
		 */
        public ItemForEvaluation(int solutionIndex, int objectiveIndex, double objectiveValue) {
            this(solutionIndex, objectiveIndex, objectiveValue, 0.0);
        }

        public ItemForEvaluation(int solutionIndex, int objectiveIndex, double objectiveValue, double feedback) {

            this.solutionIndex = solutionIndex;
            this.objectiveIndex = objectiveIndex;
            this.objectiveValue = objectiveValue;
            this.feedback = feedback;
        }

		/**
		 * Constructor
		 */
		public ItemForEvaluation() {
			this(-1, -1);
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
		
		public double getObjectiveValue() {
			return objectiveValue;
		}

		public void setObjectiveValue(double objectiveValue) {
			this.objectiveValue = objectiveValue;
		}

		public double getFeedback() {
			return feedback;
		}

		public void setFeedback(double feedback) {
			this.feedback = feedback;
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
	
	public String toString() {
		return Converter.toJson(this);
	}
	
	public abstract List<RankingItem> execute(List<AbstractObjective> objectives, List<NSolution<?>> population, List<ItemForEvaluation> itemsForEvaluation);
	    
//	public List<RankingItem> execute(NProblem<?> problem, List<NSolution<?>> population, List<ItemForEvaluation> itemsForEvaluation){
//	    return null;
//	}
}
