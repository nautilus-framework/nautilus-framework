package thiagodnf.nautilus.web.model;

import com.google.gson.Gson;

public class Settings {

	private boolean showLines = true;
	
	private int colorize = 1;
	
	private String name;

	public boolean getShowLines() {
		return showLines;
	}

	public void setShowLines(boolean showLines) {
		this.showLines = showLines;
	}

	public int getColorize() {
		return colorize;
	}

	public void setColorize(int colorize) {
		this.colorize = colorize;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}
