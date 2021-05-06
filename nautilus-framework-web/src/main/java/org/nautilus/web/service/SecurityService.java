package org.nautilus.web.service;

import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	public boolean isUserLogged() {
		return getLoggedUser() != null;
	}
	
    public boolean isAdmin() {

        if (!isUserLogged()) {
            return false;
        }

        Collection<GrantedAuthority> authorities = getLoggedUser().getAuthorities();

        for (GrantedAuthority authority : authorities) {

            if (authority.getAuthority().equalsIgnoreCase("SHOW_ADMIN_PAGE")) {
                return true;
            }
        }

        return false;
    }
	
	public org.nautilus.web.model.UserDetails getLoggedUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		if(auth.getPrincipal() instanceof org.nautilus.web.model.UserDetails) {
			return (org.nautilus.web.model.UserDetails) auth.getPrincipal();
		}
		
		return null;
	}
	

}
