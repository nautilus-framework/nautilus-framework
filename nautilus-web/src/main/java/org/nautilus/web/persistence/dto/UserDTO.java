package org.nautilus.web.persistence.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private String id;
	
	private String email;

	@NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

	public boolean enabled;
	
	public boolean admin;
	
	private boolean accountNonExpired;
	
	private boolean accountNonLocked;
	
	private boolean credentialsNonExpired;
	
	@Min(1)
    @Max(Integer.MAX_VALUE)
    private int maxExecutions;
	
	public String getFullname() {
	    return String.format("%s %s", getFirstname(), getLastname());
	}
}

