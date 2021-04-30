package org.nautilus.web.feature.user.service;

import java.util.HashSet;
import java.util.Set;

import org.nautilus.web.feature.user.constant.Role;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.persistence.model.UserDetails;
import org.nautilus.web.util.Privileges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("The username was not found. Please verify the e-mail");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (user.getRole() == Role.ADMIN) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Privileges.IS_ADMIN));
        }

        return new UserDetails(user, grantedAuthorities);
    }
}
