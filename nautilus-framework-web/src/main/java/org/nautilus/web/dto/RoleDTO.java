package org.nautilus.web.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.nautilus.web.annotation.HTMLAutoComplete;
import org.nautilus.web.annotation.HTMLAutoFocus;
import org.nautilus.web.annotation.HTMLSpellCheck;

import lombok.Value;

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
