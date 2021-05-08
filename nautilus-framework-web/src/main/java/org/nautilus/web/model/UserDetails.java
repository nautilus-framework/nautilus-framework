package org.nautilus.web.model;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetails extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1816387556811377455L;

	private User user;

	public UserDetails(User user, Set<GrantedAuthority> grantedAuthorities) {
		super(
			user.getEmail(), 
			user.getPassword(), 
			user.isEnabled(),
			user.isAccountNonExpired(), 
			user.isCredentialsNonExpired(),
			user.isAccountNonLocked(),
			grantedAuthorities
		);
		
		this.user = user;
	}
}
