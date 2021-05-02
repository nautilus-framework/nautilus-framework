    package org.nautilus.web.feature.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.nautilus.web.feature.user.dto.SettingsDTO;
import org.nautilus.web.feature.user.exception.UserNotFoundException;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.feature.user.repository.UserRepository;
import org.nautilus.web.persistence.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SecurityService securityService;

    @Autowired
    private ModelMapper modelMapper;
    
	public UserDTO findUserDTOById(String id) {
		return convertToUserDTO(findUserById(id));
	}
	
	public void deleteById(String id) {

		User found = findUserById(id);

//		if (!found.isEditable()) {
//			throw new UserNotEditableException();
//		}
		
		// After removing a user, we have to remove all his/her executions

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
//        found.getSettings().setMaxExecutions(user.getMaxExecutions());
        
        userRepository.save(found);
    }
	
    public void saveSettings(SettingsDTO dto) {

        String userId = securityService.getLoggedUser().getUser().getId();
        
        User found = findUserById(userId);

        found.setDecimalPlaces(dto.getDecimalPlaces());
        found.setDecimalSeparator(dto.getDecimalSeparator());
        found.setLanguage(dto.getLanguage());
        
        userRepository.save(found);
    }

    public SettingsDTO getSettings() {
        
        String userId = securityService.getLoggedUser().getUser().getId();
        
        return modelMapper.map(findUserById(userId), SettingsDTO.class);
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
        
        
        return new SettingsDTO(
            user.getDecimalPlaces(),
            user.getDecimalSeparator(),
            user.getLanguage()
        );
    }
}
