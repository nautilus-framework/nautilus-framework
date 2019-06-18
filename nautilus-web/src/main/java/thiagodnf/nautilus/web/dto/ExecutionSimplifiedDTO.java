package thiagodnf.nautilus.web.dto;

import java.util.Date;

import lombok.Value;

@Value
public class ExecutionSimplifiedDTO {

	private String id;
	
	private String title;
	
	private String problemId;
	
	private String instance;
	
	private boolean showToAllUsers;
	
	private Date creationDate;
}

