package org.nautilus.web.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.nautilus.web.dto.SettingsDTO;
import org.nautilus.web.dto.SignupDTO;
import org.nautilus.web.exception.UserNotFoundException;
import org.nautilus.web.model.User;
import org.nautilus.web.model.UserDetails;
import org.nautilus.web.repository.UserRepository;
import org.nautilus.web.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {
	
    @Autowired
    private SecurityService securityService;
    
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("The username was not found. Please verify the e-mail");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new UserDetails(user, grantedAuthorities);
    }
	 
    @Transactional
    public User create(SignupDTO signupDTO) {
        return create(signupDTO.getName(), signupDTO.getEmail(), signupDTO.getPassword(), Role.USER);
    }
    
    @Transactional
    public User create(String name, String email, String password, Role role) {
        
        User user = new User();

        user.setId(null);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepository.save(user);
    }
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findByUserId(String userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
	
	public List<User> findAll() {
        return userRepository.findAll(); 
    }
	
    public void updateSettings(SettingsDTO dto) {

        User user = securityService.getLoggedUser().getUser();
        
        user = findByUserId(user.getId());

        user.setName(dto.getName());
        user.setDecimalPlaces(dto.getDecimalPlaces());
        user.setDecimalSeparator(dto.getDecimalSeparator());
        user.setLanguage(dto.getLanguage());
        user.setTimeZone(dto.getTimeZone());

        user = userRepository.save(user);
        
        securityService.getLoggedUser().setUser(user);
    }

    public SettingsDTO getSettingsDTO() {

        User user = securityService.getLoggedUser().getUser();

        return convertToSettingsDTO(findByUserId(user.getId()));
    }
    
    protected SettingsDTO convertToSettingsDTO(User user) {
        return modelMapper.map(user, SettingsDTO.class);
    }
}
