package thiagodnf.nautilus.plugin.spl.extension.instance;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.gui.Tab;
import thiagodnf.nautilus.plugin.gui.TableTabContent;
import thiagodnf.nautilus.plugin.spl.encoding.instance.NewTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.instance.OldTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.extension.problem.SPLProblemExtension;

@Extension
public class SPLInstanceDataExtension implements InstanceDataExtension {

	@Override
	public InstanceData getInstanceData(Path path) {
		return new NewTXTInstanceData(path);
	}
	
	@Override
	public List<String> getProblemIds() {
		return Arrays.asList(
			new SPLProblemExtension().getId()
		);
	}
	
	@Override
	public List<Tab> getTabs(InstanceData data) {

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
					"" + data.getProductCost(i),
					"" + data.getProductImportance(i)
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
