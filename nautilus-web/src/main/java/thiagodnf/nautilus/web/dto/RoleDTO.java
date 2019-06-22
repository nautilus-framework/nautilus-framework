package thiagodnf.nautilus.web.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Value;
import thiagodnf.nautilus.web.annotation.HTMLAutoComplete;
import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;
import thiagodnf.nautilus.web.annotation.HTMLSpellCheck;

@Value
public class RoleDTO {

	private String id;
	
	@NotBlank
	@HTMLAutoFocus
	@HTMLAutoComplete("off")
	@HTMLSpellCheck("true")
	private String name;
	
	@NotNull
	private List<String> privileges;
}
