package thiagodnf.nautilus.core.adapter;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;

import thiagodnf.nautilus.core.util.Converter;

public class OperatorAdapter {

	private Map<String, CrossoverOperator<?>> crossovers;

	private Map<String, String> crossoverNames;
	
	private Map<String, MutationOperator<?>> mutations;

	private Map<String, String> mutationNames;

	/**
	 * Constructor
	 */
	public OperatorAdapter() {
		this.crossovers = new HashMap<>();
		this.crossoverNames = new HashMap<>();
		
		this.mutations = new HashMap<>();
		this.mutationNames = new HashMap<>();
	}

	public void addMutation(String name, MutationOperator<?> mutation) {

		checkNotNull(mutation, "The mutation instance should not be null");
		
		String key = Converter.toKey(name);

		this.mutations.put(key, mutation);
		this.mutationNames.put(key, name);
	}

	public void addCrossover(String name, CrossoverOperator<?> crossover) {

		checkNotNull(crossover, "The crossover instance should not be null");
		
		String key = Converter.toKey(name);

		this.crossovers.put(key, crossover);
		this.crossoverNames.put(key, name);
	}

	public Map<String, String> getMutationNames() {
		return this.mutationNames;
	}
	
	public Map<String, String> getCrossoverNames() {
		return this.crossoverNames;
	}

	public CrossoverOperator<?> getCrossover(String key) {
		return this.crossovers.get(key);
	}

	public MutationOperator<?> getMutation(String key) {
		return this.mutations.get(key);
	}
}
