package thiagodnf.nautilus.web.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thiagodnf.nautilus.web.dto.RoleDTO;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.model.UserDetails;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("The username was not found. Please verify the e-mail");
        }
		
		RoleDTO roleDTO = roleService.findById(user.getRoleId());

		Set<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(roleDTO.getPrivileges());

		return new UserDetails(user, grantedAuthorities);
	}

	private Set<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {

		Set<GrantedAuthority> authorities = new HashSet<>();

		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}

		return authorities;
	}
}
