package org.nautilus.web.model;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.nautilus.web.util.Role;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@NoArgsConstructor
public class User {

	@Id
	private String id;
	
	@NotBlank
	private String name;
	
	@Email
    @NotBlank
	private String email;

	@NotBlank
    @Size(min = 6)
	private String password;
	
	@NotNull
	private Role role = Role.USER;
	
	private boolean enabled = true;
	
	private boolean accountNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean credentialsNonExpired = true;
	
	private String decimalSeparator = "POINT";
	
	@Min(1)
    @Max(10)
	private int decimalPlaces = 4;
	
	private String language = "en_US";
	
	private String timeZone = "(GMT-03:00) America/Sao_Paulo";

	@CreatedDate
	private Date creationDate;
	
	@LastModifiedDate
	private Date lastChangeDate;
}
