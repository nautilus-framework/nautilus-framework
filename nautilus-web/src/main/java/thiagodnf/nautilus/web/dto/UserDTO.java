package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.Value;

@Value
public class UserDTO {

	private String id;
	
	private String email;

	@NotBlank
	private String firstname;

	@NotBlank
	private String lastname;
	
	@NotBlank
	private String roleId;
	
	private String roleName;
		
	public boolean enabled;
	
	private boolean accountNonExpired;
	
	private boolean accountNonLocked;
	
	private boolean credentialsNonExpired;
	
	public String getFullname() {
		return String.format("%s %s", firstname, lastname);
	}
}

