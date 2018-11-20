package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class RowItem {

	private String name;

	private List<Object> values;

	public RowItem(String name) {
		this.name = name;
		this.values = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
