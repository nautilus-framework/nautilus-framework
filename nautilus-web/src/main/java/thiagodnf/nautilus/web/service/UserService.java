package thiagodnf.nautilus.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.dto.RoleDTO;
import thiagodnf.nautilus.web.dto.UserDTO;
import thiagodnf.nautilus.web.dto.UserDisplayDTO;
import thiagodnf.nautilus.web.dto.UserProfileDTO;
import thiagodnf.nautilus.web.exception.ConfirmationTokenNotFoundException;
import thiagodnf.nautilus.web.exception.UserNotEditableException;
import thiagodnf.nautilus.web.exception.UserNotFoundException;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.repository.UserRepository;

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
	
	public void updateUserDisplay(String userId, UserDisplayDTO userDisplayDTO) {

        User found = findUserById(userId);

        found.setDecimalPlaces(userDisplayDTO.getDecimalPlaces());
        found.setDecimalSeparator(userDisplayDTO.getDecimalSeparator());
        found.setLanguage(userDisplayDTO.getLanguage());
        
        userRepository.save(found);
    }
	
	public void updateUserProfile(String userId, UserProfileDTO userProfileDTO) {

        User found = findUserById(userId);

        found.setFirstname(userProfileDTO.getFirstname());
        found.setLastname(userProfileDTO.getLastname());
        
        userRepository.save(found);
    }

    public UserDisplayDTO findUserDisplayDTOById(String id) {
        return convertToUserDisplayDTO(findUserById(id));
    }
    
    public UserProfileDTO findUserProfileDTOById(String id) {
        return convertToUserProfileDTO(findUserById(id));
    }
    
    private UserDTO convertToUserDTO(User user) {
        
        if(user == null) return null;
        
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
    
    public UserDisplayDTO convertToUserDisplayDTO(User user) {
        
        if(user == null)  return null;
        
        return new UserDisplayDTO(
            user.getDecimalPlaces(),
            user.getDecimalSeparator(),
            user.getLanguage()
        );
    }
    
    private UserProfileDTO convertToUserProfileDTO(User user) {
        
        if(user == null)  return null;
        
        return new UserProfileDTO(
            user.getFirstname(),
            user.getLastname()
        );
    }
}
