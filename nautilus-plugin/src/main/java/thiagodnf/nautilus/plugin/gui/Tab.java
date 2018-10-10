package thiagodnf.nautilus.plugin.gui;

import thiagodnf.nautilus.core.util.Converter;

public class Tab {

	private String name;

	private TabContent content;

	public Tab(String name, TabContent content) {
		this.name = name;
		this.content = content;
	}

	public TabContent getTabContent() {
		return this.content;
	}

	public String getId() {
		return Converter.toKey(getName());
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
