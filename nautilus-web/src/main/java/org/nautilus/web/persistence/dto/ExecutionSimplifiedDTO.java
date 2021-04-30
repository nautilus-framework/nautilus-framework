package org.nautilus.web.persistence.dto;

import java.util.Date;
import java.util.List;

import org.nautilus.core.model.SelectedSolution;

import lombok.Value;

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
	
	private List<String> objectiveIds;
}

