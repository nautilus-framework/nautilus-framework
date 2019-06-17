package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.Value;

@Value
public class NewExecutionDTO {

	@NotBlank
	private String problemId;
}

