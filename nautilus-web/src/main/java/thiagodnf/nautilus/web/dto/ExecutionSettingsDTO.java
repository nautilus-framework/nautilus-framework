package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.web.annotation.Contains;
import thiagodnf.nautilus.web.model.Execution.Visibility;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionSettingsDTO {
	
	private String title;
	
	private Visibility visibility;
	
	private boolean showLines;
	
	private boolean showOriginalObjectiveValues;
	
	@NotBlank
	private String normalizeId;
	
	@NotBlank
	private String correlationId;
	
	@NotBlank
	private String removerId;
	
	@NotBlank
	@Contains({"#7cb5ec", "#434348", "#90ed7d", "#f7a35c", "#8085e9", "#f15c80", "#e4d354", "#2b908f", "#f45b5b", "#91e8e1"})
    private String color;
	
	public String toString() {
        return Converter.toJson(this);
    }
}

