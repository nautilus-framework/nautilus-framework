package thiagodnf.nautilus.core.plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.adapter.ObjectiveAdapter;
import thiagodnf.nautilus.core.adapter.OperatorAdapter;
import thiagodnf.nautilus.core.colorize.ByEuclideanDistanceColorize;
import thiagodnf.nautilus.core.colorize.BySimilarityColorize;
import thiagodnf.nautilus.core.colorize.Colorize;
import thiagodnf.nautilus.core.colorize.NoColorColorize;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractPlugin {
	
	protected Map<String, Colorize> colorizes = new HashMap<>();
	
	protected Map<String, Normalize> normalizes = new HashMap<>();
	
	public void initIt() {
		
		addColorize(new NoColorColorize());
		addColorize(new ByEuclideanDistanceColorize());
		addColorize(new BySimilarityColorize());
		
		addNormalize(new ByMaxAndMinValuesNormalize());
		addNormalize(new ByParetoFrontValuesNormalize());
	}
	
	public void addColorize(Colorize colorize) {
		this.colorizes.put(colorize.getKey(), colorize);
	}
	
	public Map<String, Colorize> getColorizes(){
		return colorizes;
	}
	
	public Colorize getColorize(String key) {
		return getColorizes().get(key);
	}
	
	public void addNormalize(Normalize normalize) {
		this.normalizes.put(normalize.getKey(), normalize);
	}
	
	public Map<String, Normalize> getNormalizes(){
		return normalizes;
	}
	
	public Normalize getNormalize(String key) {
		return getNormalizes().get(key);
	}
	
	public String getProblemKey() {
		return Converter.toKey(getProblemName());
	}
	
	public double getSimilarityDistance(List<String> variables1, List<String> variables2) {
		return 0.0;
	}
	
	
	public ObjectiveAdapter getObjectiveAdapter() {
		return new ObjectiveAdapter();
	}
	
	public OperatorAdapter getOperatorAdapter() {
		return new OperatorAdapter();
	}
	
	public abstract Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException;
	
	public abstract String getProblemName();
}
