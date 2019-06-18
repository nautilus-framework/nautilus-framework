package thiagodnf.nautilus.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thiagodnf.nautilus.web.dto.SettingsProfileDTO;
import thiagodnf.nautilus.web.dto.UserDTO;
import thiagodnf.nautilus.web.model.User;

@Service
@Transactional
public class ProfileService {
	
	@Autowired
	private UserService userService;
	
	public void update(SettingsProfileDTO profileDTO) {

		User found = userService.findUserById(profileDTO.getId());

		found.setFirstname(profileDTO.getFirstname());
		found.setLastname(profileDTO.getLastname());
		
		userService.save(found);
	}

	public SettingsProfileDTO findById(String id) {
		return convertToDTO(userService.findById(id));
	}
	
	public SettingsProfileDTO convertToDTO(UserDTO userDTO) {
		
		if(userDTO == null) {
			return null;
		}
		
		return new SettingsProfileDTO(
			userDTO.getId(),
			userDTO.getFirstname(),
			userDTO.getLastname()
		);
    }
}
