package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thiagodnf.nautilus.core.colorize.DontColorize;
import thiagodnf.nautilus.core.correlation.DontCorrelation;
import thiagodnf.nautilus.core.duplicated.ByObjectivesDuplicatesRemover;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.reduction.ConfidenceBasedReduction;

@Setter
@Getter
@AllArgsConstructor
public class ExecutionSettingsDTO {
	
	private String title;
	
	private boolean showToAllUsers = false;
	
	private boolean showLines = true;
	
	@NotBlank
	private String colorizeId = new DontColorize().getId();
	
	@NotBlank
	private String normalizeId = new ByParetoFrontValuesNormalize().getId();
	
	@NotBlank
	private String correlationId = new DontCorrelation().getId();
	
	@NotBlank
	private String duplicatesRemoverId = new ByObjectivesDuplicatesRemover().getId();
	
	@NotBlank
	private String reducerId= new ConfidenceBasedReduction().getId();
}

