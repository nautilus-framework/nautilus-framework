package org.nautilus.web.persistence.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.nautilus.web.annotation.HTMLAutoComplete;
import org.nautilus.web.annotation.HTMLAutoFocus;
import org.nautilus.web.annotation.HTMLRequired;
import org.nautilus.web.annotation.HTMLSpellCheck;
import org.nautilus.web.annotation.HTMLStep;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

	private String id;
	
	private String email;

	@NotBlank
    @HTMLAutoFocus
    @HTMLSpellCheck("false")
	@HTMLAutoComplete("off")
    private String firstname;

    @NotBlank
    @HTMLSpellCheck("false")
    @HTMLAutoComplete("off")
    private String lastname;

	public boolean enabled;
	
	public boolean admin;
	
	private boolean accountNonExpired;
	
	private boolean accountNonLocked;
	
	private boolean credentialsNonExpired;
	
	@Min(1)
    @Max(Integer.MAX_VALUE)
    @HTMLStep(1)
    @HTMLRequired
    @HTMLAutoComplete("off")
    private int maxExecutions;
	
	public String getFullname() {
	    return String.format("%s %s", getFirstname(), getLastname());
	}
}

