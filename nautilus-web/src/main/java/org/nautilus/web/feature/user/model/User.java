package org.nautilus.web.feature.user.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.nautilus.web.annotation.UniqueEmail;
import org.nautilus.web.config.DatabaseConfiguration.BaseModel;
import org.nautilus.web.feature.user.constant.Role;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseModel {

    @Email
    @NotBlank
//    @UniqueEmail
    private String email;

    @NotBlank
    @Size(min = 6)
	private String password;
	
	private String confirmationToken;
	
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;
	
	private boolean enabled = false;
	
	private boolean accountNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean credentialsNonExpired = true;
	
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	private Settings settings = new Settings();
}
