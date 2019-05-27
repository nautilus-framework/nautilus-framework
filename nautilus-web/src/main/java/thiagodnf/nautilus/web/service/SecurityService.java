package thiagodnf.nautilus.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.web.model.UserDetails;

@Service
public class SecurityService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	public boolean isUserLogged() {
		return getLoggedUser() != null;
	}
	
	public thiagodnf.nautilus.web.model.UserDetails getLoggedUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		if(auth.getPrincipal() instanceof thiagodnf.nautilus.web.model.UserDetails) {
			return (thiagodnf.nautilus.web.model.UserDetails) auth.getPrincipal();
		}
		
		return null;
	}
	
	public void autologin(String username, String password) {

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
			userDetails, 
			password, 
			userDetails.getAuthorities()
		);

		authenticationManager.authenticate(token);
		
		if (token.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(token);
		}
	}
}
