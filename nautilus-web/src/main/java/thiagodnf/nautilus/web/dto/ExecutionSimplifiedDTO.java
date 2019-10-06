package thiagodnf.nautilus.web.dto;

import java.util.Date;
import java.util.List;

import lombok.Value;
import thiagodnf.nautilus.core.model.SelectedSolution;

@Value
public class ExecutionSimplifiedDTO {

	private String id;
	
	private String title;
	
	private String problemId;
	
	private String instance;
	
	private String algorithmId;
	
	private Date creationDate;
	
	private List<SelectedSolution> selectedSolutions;
	
	private String userId;
}

