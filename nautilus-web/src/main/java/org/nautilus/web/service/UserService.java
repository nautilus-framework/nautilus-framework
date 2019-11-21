package org.nautilus.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.nautilus.web.dto.ExecutionSimplifiedDTO;
import org.nautilus.web.dto.RoleDTO;
import org.nautilus.web.dto.UserDTO;
import org.nautilus.web.dto.UserSettingsDTO;
import org.nautilus.web.exception.ConfirmationTokenNotFoundException;
import org.nautilus.web.exception.UserNotEditableException;
import org.nautilus.web.exception.UserNotFoundException;
import org.nautilus.web.model.User;
import org.nautilus.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
    private ExecutionService executionService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public User create(User user) {

		user.setId(null);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return save(user);
	}
	
	public void confirm(User user) {
		
		user.setEnabled(true);
		
		save(user);
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken).orElseThrow(ConfirmationTokenNotFoundException::new);
	}
	
	public UserDTO findUserDTOById(String id) {
		return convertToUserDTO(findUserById(id));
	}
	
	public void deleteById(String id) {

		User found = findUserById(id);

		if (!found.isEditable()) {
			throw new UserNotEditableException();
		}
		
		// After removing a user, we have to remove all his/her executions

        List<ExecutionSimplifiedDTO> executions = executionService.findExecutionSimplifiedDTOByUserId(found.getId());

        for (ExecutionSimplifiedDTO execution : executions) {
            executionService.deleteById(execution.getId());
        }

        userRepository.delete(found);
	}
	
	public User findUserById(String id) {
		return this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
	
	public List<User> findUsersByRoleId(String id) {
		return this.userRepository.findByRoleId(id);
    }
	
	public List<UserDTO> findAll() {
		return convertToUserDTOs(userRepository.findAll());
	}
	
	
	
	private List<UserDTO> convertToUserDTOs(List<User> models) {
		return models.stream().map(this::convertToUserDTO).collect(Collectors.toList());
	}
	
	public void updateUser(UserDTO userDTO) {

        User found = findUserById(userDTO.getId());

        if (!found.isEditable()) {
            throw new UserNotEditableException();
        }
        
        RoleDTO roleDTO = roleService.findById(userDTO.getRoleId());
        
        found.setRoleId(roleDTO.getId());
        found.setEnabled(userDTO.isEnabled());
        found.setAccountNonExpired(userDTO.isAccountNonExpired());
        found.setCredentialsNonExpired(userDTO.isCredentialsNonExpired());
        found.setAccountNonLocked(userDTO.isAccountNonLocked());
        found.setMaxExecutions(userDTO.getMaxExecutions());
        found.setFirstname(userDTO.getFirstname());
        found.setLastname(userDTO.getLastname());
        
        userRepository.save(found);
    }
	
    public void updateUserSettings(String userId, UserSettingsDTO dto) {

        User found = findUserById(userId);

        found.setFirstname(dto.getFirstname());
        found.setLastname(dto.getLastname());
        found.setDecimalPlaces(dto.getDecimalPlaces());
        found.setDecimalSeparator(dto.getDecimalSeparator());
        found.setLanguage(dto.getLanguage());
        found.setTimeZone(dto.getTimeZone());

        userRepository.save(found);
    }

    public UserSettingsDTO findUserSettingsDTOById(String id) {
        return convertToUserSettingsDTO(findUserById(id));
    }
    
    private UserDTO convertToUserDTO(User user) {
        
        if (user == null)
            return null;
        
        RoleDTO role = roleService.findById(user.getRoleId());
        
        return new UserDTO(
            user.getId(),
            user.getEmail(),
            user.getFirstname(),
            user.getLastname(),
            role.getId(),
            role.getName(),
            user.isEnabled(),
            user.isAccountNonExpired(),
            user.isAccountNonLocked(),
            user.isCredentialsNonExpired(),
            user.getMaxExecutions()
        );
    }
    
    public UserSettingsDTO convertToUserSettingsDTO(User user) {
        
        if (user == null)
            return null;
        
        return new UserSettingsDTO(
            user.getFirstname(),
            user.getLastname(),  
            user.getDecimalPlaces(),
            user.getDecimalSeparator(),
            user.getLanguage(),
            user.getTimeZone()
        );
    }
}