package thiagodnf.nautilus.core.adapter;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import thiagodnf.nautilus.core.operator.crossover.Crossover;
import thiagodnf.nautilus.core.operator.mutation.Mutation;
import thiagodnf.nautilus.core.operator.selection.Selection;

public class OperatorAdapter {

	private Map<String, Selection<?>> selections;
	
	private Map<String, Crossover<?>> crossovers;

	private Map<String, Mutation<?>> mutations;
	
	/**
	 * Constructor
	 */
	public OperatorAdapter() {
		this.selections = new HashMap<>();
		this.crossovers = new HashMap<>();
		this.mutations = new HashMap<>();
	}

	public void addSelection(Selection<?> selection) {

		checkNotNull(selection, "The selection instance should not be null");

		this.selections.put(selection.getKey(), selection);
	}
	
	public void addMutation(Mutation<?> mutation) {

		checkNotNull(mutation, "The mutation instance should not be null");

		this.mutations.put(mutation.getKey(), mutation);
	}

	public void addCrossover(Crossover<?> crossover) {

		checkNotNull(crossover, "The crossover instance should not be null");

		this.crossovers.put(crossover.getKey(), crossover);
	}
	
	public Map<String, Selection<?>> getSelections() {
		return this.selections;
	}
	
	public Map<String, Mutation<?>> getMutations() {
		return this.mutations;
	}
	
	public Map<String, Crossover<?>> getCrossovers() {
		return this.crossovers;
	}

	public Selection<?> getSelection(String key) {
		return getSelections().get(key);
	}
	
	public Crossover<?> getCrossover(String key) {
		return getCrossovers().get(key);
	}

	public Mutation<?> getMutation(String key) {
		return getMutations().get(key);
	}
}
