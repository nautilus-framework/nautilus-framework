package thiagodnf.nautilus.plugin.extension;

import java.util.List;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.operator.crossover.Crossover;
import thiagodnf.nautilus.core.operator.mutation.Mutation;
import thiagodnf.nautilus.core.operator.selection.Selection;

public interface OperatorExtension extends ExtensionPoint {

	public List<Selection<?>> getSelectionOperators(String problemId);
	
	public List<Crossover<?>> getCrossoverOperators(String problemId);
	
	public List<Mutation<?>> getMutationOperators(String problemId);
}
