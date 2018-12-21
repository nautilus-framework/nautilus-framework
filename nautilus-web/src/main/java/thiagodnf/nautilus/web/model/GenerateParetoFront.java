package thiagodnf.nautilus.web.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GenerateParetoFront {
	
	@NotNull
	@NotEmpty
	private String problemId;

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}
}
