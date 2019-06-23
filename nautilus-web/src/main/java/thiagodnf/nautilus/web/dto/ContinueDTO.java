package thiagodnf.nautilus.web.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContinueDTO {

	@NotBlank
	private String previousExecutionId;

	@NotNull
	@Size(min = 1)
	private List<String> nextObjectiveIds;
}