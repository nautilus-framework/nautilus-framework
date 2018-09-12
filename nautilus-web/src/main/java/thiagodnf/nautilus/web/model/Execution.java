package thiagodnf.nautilus.web.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import com.google.gson.Gson;

public class Execution {

	@Id
	private String id;
	
	private long executionTime;
	
	@NotNull
	private Date date;
	
	@NotNull
	private Parameters parameters;
	
	@NotNull
	private List<Solution> solutions;
	
	@NotNull
	private Settings settings;
	
	public Execution() {
		this.parameters = new Parameters();
		this.solutions = new ArrayList<>();
		this.date  = new Date();
		this.settings = new Settings();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	public String getTitle() {

		String name = getSettings().getName();

		if (StringUtils.isBlank(name)) {
			return getId();
		}

		return name;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}
