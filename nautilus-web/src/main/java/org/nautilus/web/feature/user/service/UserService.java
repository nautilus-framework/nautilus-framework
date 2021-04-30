    package org.nautilus.web.feature.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.nautilus.web.exception.ConfirmationTokenNotFoundException;
import org.nautilus.web.exception.UserNotEditableException;
import org.nautilus.web.exception.UserNotFoundException;
import org.nautilus.web.feature.user.dto.SettingsDTO;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.persistence.dto.ExecutionSimplifiedDTO;
import org.nautilus.web.persistence.dto.UserDTO;
import org.nautilus.web.persistence.repository.UserRepository;
import org.nautilus.web.service.ExecutionService;
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
    private ExecutionService executionService;

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

//		if (!found.isEditable()) {
//			throw new UserNotEditableException();
//		}
		
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
	
	public List<UserDTO> findAll() {
	    
	    List<User> users = new ArrayList<>();
	    
	    userRepository.findAll().forEach(users::add);
	    
		return convertToUserDTOs(users);
	}
	
	private List<UserDTO> convertToUserDTOs(List<User> models) {
		return models.stream().map(this::convertToUserDTO).collect(Collectors.toList());
	}
	
	public void updateUser(UserDTO user) {

        User found = findUserById(user.getId());

//        if (!found.isEditable()) {
//            throw new UserNotEditableException();
//        }
        
        found.setEnabled(user.isEnabled());
        
        found.setAccountNonExpired(user.isAccountNonExpired());
        found.setCredentialsNonExpired(user.isCredentialsNonExpired());
        found.setAccountNonLocked(user.isAccountNonLocked());
        found.getSettings().setMaxExecutions(user.getMaxExecutions());
        
        userRepository.save(found);
    }
	
    public void updateUserSettings(String userId, SettingsDTO dto) {

        User found = findUserById(userId);

        found.getSettings().setDecimalPlaces(dto.getDecimalPlaces());
        found.getSettings().setDecimalSeparator(dto.getDecimalSeparator());
        found.getSettings().setLanguage(dto.getLanguage());
        found.getSettings().setTimeZone(dto.getTimeZone());

        userRepository.save(found);
    }

    public SettingsDTO findUserSettingsDTOById(String id) {
        return convertToUserSettingsDTO(findUserById(id));
    }
    
    private UserDTO convertToUserDTO(User user) {
        
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        
        return dto;
    }
    
    public SettingsDTO convertToUserSettingsDTO(User user) {
        
        if (user == null)
            return null;
        
        SettingsDTO dto = new SettingsDTO();
        
//        return new UserSettingsDTO(
//            user.getFirstname(),
//            user.getLastname(),  
//            user.getDecimalPlaces(),
//            user.getDecimalSeparator(),
//            user.getLanguage(),
//            user.getTimeZone()
//        );
        
        return dto;
    }
}
