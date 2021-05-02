package org.nautilus.web.feature.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.nautilus.web.config.DatabaseConfiguration.BaseModel;
import org.nautilus.web.feature.user.constant.DecimalSeparator;
import org.nautilus.web.feature.user.constant.Language;
import org.nautilus.web.feature.user.constant.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User extends BaseModel {

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
	private String password;
	
    @NotNull
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;
	
	private boolean enabled = true;
	
	private boolean accountNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean credentialsNonExpired = true;
	
	@Min(0)
    @Max(10)
    private int decimalPlaces = 2;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DecimalSeparator decimalSeparator = DecimalSeparator.POINT;

	@NotNull
	@Enumerated(EnumType.STRING)
    private Language language = Language.EN_US;
}
