package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

	private String id;
	
	private String email;

	private String fullname;

	@NotBlank
	private String roleId;
	
	private String roleName;
		
	public boolean enabled;
	
	private boolean accountNonExpired;
	
	private boolean accountNonLocked;
	
	private boolean credentialsNonExpired;
}

