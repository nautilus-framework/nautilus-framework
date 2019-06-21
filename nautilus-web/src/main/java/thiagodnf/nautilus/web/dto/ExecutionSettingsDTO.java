package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thiagodnf.nautilus.web.model.Execution.Visibility;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionSettingsDTO {
	
	private String title;
	
	private Visibility visibility;
	
	private boolean showLines;
	
	@NotBlank
	private String colorizeId;
	
	@NotBlank
	private String normalizeId;
	
	@NotBlank
	private String correlationId;
	
	@NotBlank
	private String duplicatesRemoverId;
	
	@NotBlank
	private String reducerId;
}

