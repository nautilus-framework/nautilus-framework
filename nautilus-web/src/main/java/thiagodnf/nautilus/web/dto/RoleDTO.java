package thiagodnf.nautilus.web.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Value;

@Value
public class RoleDTO {

	private String id;
	
	@NotBlank
	private String name;
	
	@NotNull
	private List<String> privileges;
}
