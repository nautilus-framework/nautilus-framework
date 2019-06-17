package thiagodnf.nautilus.plugin.spl.extension.problem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.extension.problem.AbstractProblemExtension;
import thiagodnf.nautilus.plugin.gui.Tab;
import thiagodnf.nautilus.plugin.gui.TableTabContent;
import thiagodnf.nautilus.plugin.spl.encoding.instance.NewTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.objective.AliveMutantsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.CostObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.NewSimilarityObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.NumberOfProductsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UncoveredPairsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UnimportantFeaturesObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UnselectedFeaturesObjective;
import thiagodnf.nautilus.plugin.spl.encoding.problem.SPLProblem;

@Extension
public class SPLProblemExtension extends AbstractProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new SPLProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "SPL Problem";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return BinarySolution.class;
	}

	@Override
	public List<AbstractObjective> getObjectives() {
		
		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new NumberOfProductsObjective());
		objectives.add(new AliveMutantsObjective());
		objectives.add(new UncoveredPairsObjective());
//		objectives.add(new SimilarityObjective());
		objectives.add(new NewSimilarityObjective());
		objectives.add(new CostObjective());
		objectives.add(new UnselectedFeaturesObjective());
		objectives.add(new UnimportantFeaturesObjective());
		
		return objectives;
	}

	@Override
	public Instance getInstance(Path path) {
		return new NewTXTInstanceData(path);
	}
	
	@Override
	public List<Tab> getTabs(Instance data) {

		NewTXTInstanceData c = (NewTXTInstanceData) data;

		List<Tab> tabs = new ArrayList<>();

		tabs.add(getFeaturesTab(c));
		tabs.add(getProductsTab(c));
		tabs.add(getPairwiseCoverageTab(c));
		tabs.add(getMutationCoverageTab(c));

		return tabs;
	}

	protected Tab getFeaturesTab(NewTXTInstanceData data) {

		TableTabContent table = new TableTabContent(Arrays.asList("Feature", "Cost", "Importance"));

		List<String> features = data.getFeatures();

		for (int i = 0; i < data.getNumberOfFeatures(); i++) {
			table.getRows().add(Arrays.asList(
					features.get(i),
					"" + data.getCosts().get(i),
					"" + data.getImportances().get(i)
			));
		}

		return new Tab("Features", table);
	}

	protected Tab getProductsTab(NewTXTInstanceData data) {

		TableTabContent table = new TableTabContent(Arrays.asList("Product Id", "Feature", "Cost", "Importance"));

		for (int i = 0; i < data.getNumberOfProducts(); i++) {
			table.getRows().add(Arrays.asList("Product " + i, 
				data.getProduct(i).toString(), 
				"" + data.getProductCost(i),
				"" + data.getProductImportance(i)
			));
		}

		return new Tab("Products", table);
	}
	
	protected Tab getPairwiseCoverageTab(NewTXTInstanceData data) {

		TableTabContent table = new TableTabContent(Arrays.asList("Product Id", "Covered", "Uncovered", "# of Uncovered"));

		for (int i = 0; i < data.getNumberOfProducts(); i++) {

			List<String> covered = new ArrayList<>();
			List<String> unCovered = new ArrayList<>();

			for (int j = 0; j < data.getNumberOfPairs(); j++) {
				if (data.getPairs(i).contains(j)) {
					covered.add(String.valueOf(j));
				} else {
					unCovered.add(String.valueOf(j));
				}
			}

			table.getRows().add(Arrays.asList("Product " + i, covered.toString(), unCovered.toString(), ""+unCovered.size()));
		}

		return new Tab("Pairwise Coverate", table);
	}
	
	protected Tab getMutationCoverageTab(NewTXTInstanceData data) {

		TableTabContent table = new TableTabContent(Arrays.asList("Product Id", "Dead", "Alive", "# of Alive"));

		for (int i = 0; i < data.getNumberOfProducts(); i++) {

			List<String> covered = new ArrayList<>();
			List<String> unCovered = new ArrayList<>();

			for (int j = 0; j < data.getNumberOfMutants(); j++) {
				if (data.getMutants(i).contains(j)) {
					covered.add(String.valueOf(j));
				} else {
					unCovered.add(String.valueOf(j));
				}
			}

			table.getRows().add(Arrays.asList("Product " + i, covered.toString(), unCovered.toString(), ""+unCovered.size()));
		}

		return new Tab("Mutation Coverage", table);
	}
}
