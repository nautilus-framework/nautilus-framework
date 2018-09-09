package thiagodnf.nautilus.web.model;

import com.google.gson.Gson;

public class Settings {

	private boolean showLines = true;
	
	private boolean colorize = true;

	public boolean getShowLines() {
		return showLines;
	}

	public void setShowLines(boolean showLines) {
		this.showLines = showLines;
	}

	public boolean getColorize() {
		return colorize;
	}

	public void setColorize(boolean colorize) {
		this.colorize = colorize;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}
