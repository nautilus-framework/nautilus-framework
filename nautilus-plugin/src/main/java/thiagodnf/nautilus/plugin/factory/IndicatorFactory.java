package thiagodnf.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thiagodnf.nautilus.plugin.extension.IndicatorExtension;
import thiagodnf.nautilus.plugin.extension.indicator.EpsilonExtension;
import thiagodnf.nautilus.plugin.extension.indicator.GDExtension;
import thiagodnf.nautilus.plugin.extension.indicator.IGDExtension;
import thiagodnf.nautilus.plugin.extension.indicator.PISAHypervolumeExtension;
import thiagodnf.nautilus.plugin.extension.indicator.SpreadExtension;

public class IndicatorFactory {

	private Map<String, IndicatorExtension> map = new HashMap<>();
	
	public IndicatorFactory() {
		add(new PISAHypervolumeExtension());
		add(new SpreadExtension());
		add(new IGDExtension());
		add(new GDExtension());
		add(new EpsilonExtension());
	}

	public void add(IndicatorExtension extension) {
		map.put(extension.getId(), extension);
	}
	
	public IndicatorExtension getExtension(String indicatorId) {
		return map.get(indicatorId);
	}

	public List<IndicatorExtension> getExtensions() {
		return new ArrayList<>(map.values());
	}
}
