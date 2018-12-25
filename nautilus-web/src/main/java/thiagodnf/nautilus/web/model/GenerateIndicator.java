package thiagodnf.nautilus.web.model;

import java.util.List;

import thiagodnf.nautilus.web.annotation.NotEmptyList;
import thiagodnf.nautilus.web.annotation.NotNullList;

public class GenerateIndicator {
	
	@NotNullList
	@NotEmptyList
	private List<String> indicatorIds;
	
	public List<String> getIndicatorIds() {
		return indicatorIds;
	}

	public void setIndicatorIds(List<String> indicatorIds) {
		this.indicatorIds = indicatorIds;
	}
}
