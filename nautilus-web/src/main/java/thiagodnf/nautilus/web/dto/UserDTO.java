package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thiagodnf.nautilus.web.annotation.HTMLAutoComplete;
import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;
import thiagodnf.nautilus.web.annotation.HTMLRequired;
import thiagodnf.nautilus.web.annotation.HTMLSpellCheck;
import thiagodnf.nautilus.web.annotation.HTMLStep;

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

	@NotBlank
	private String roleId;
	
	private String roleName;
		
	public boolean enabled;
	
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

