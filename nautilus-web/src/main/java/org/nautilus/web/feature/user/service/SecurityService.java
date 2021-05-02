package org.nautilus.web.feature.user.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.nautilus.web.feature.user.exception.UserNotFoundException;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.feature.user.repository.UserRepository;
import org.nautilus.web.persistence.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        System.out.println(userRepository.findByEmail(email));
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new UserDetails(user, grantedAuthorities);
    }
    
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
	
	public org.nautilus.web.persistence.model.UserDetails getLoggedUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		if(auth.getPrincipal() instanceof org.nautilus.web.persistence.model.UserDetails) {
			return (org.nautilus.web.persistence.model.UserDetails) auth.getPrincipal();
		}
		
		return null;
	}
	

}
