package thiagodnf.nautilus.web.model;

import com.google.gson.Gson;

import thiagodnf.nautilus.core.colorize.ByEuclideanDistanceColorize;
import thiagodnf.nautilus.core.correlation.SpearmanCorrelation;
import thiagodnf.nautilus.core.duplicated.ByVariablesDuplicatesRemover;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;

public class Settings {

	private boolean showLines = true;
	
	private String colorize = new ByEuclideanDistanceColorize().getKey();
	
	private String normalize = new ByParetoFrontValuesNormalize().getId();
	
	private String correlation = new SpearmanCorrelation().getKey();
	
	private String duplicatesRemover = new ByVariablesDuplicatesRemover().getId();
	
	private String name;

	public boolean getShowLines() {
		return showLines;
	}

	public void setShowLines(boolean showLines) {
		this.showLines = showLines;
	}

	public String getColorize() {
		return colorize;
	}

	public void setColorize(String colorize) {
		this.colorize = colorize;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNormalize() {
		return normalize;
	}

	public void setNormalize(String normalize) {
		this.normalize = normalize;
	}
	
	public String getCorrelation() {
		return correlation;
	}

	public void setCorrelation(String correlation) {
		this.correlation = correlation;
	}

	public String getDuplicatesRemover() {
		return duplicatesRemover;
	}

	public void setDuplicatesRemover(String duplicatesRemover) {
		this.duplicatesRemover = duplicatesRemover;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}
