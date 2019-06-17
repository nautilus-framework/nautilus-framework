package thiagodnf.nautilus.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thiagodnf.nautilus.web.dto.PreferencesDTO;
import thiagodnf.nautilus.web.model.User;

@Service
@Transactional
public class PreferencesService {
	
	@Autowired
	private UserService userService;
	
	public void update(PreferencesDTO preferencesDTO) {

		User found = userService.findUserById(preferencesDTO.getId());

		found.setDecimalPlaces(preferencesDTO.getDecimalPlaces());
		found.setLanguage(preferencesDTO.getLanguage());
		
		userService.save(found);
	}

	public PreferencesDTO findById(String id) {
		return convertToDTO(userService.findUserById(id));
	}
	
	public PreferencesDTO convertToDTO(User user) {
		
		if(user == null) {
			return null;
		}
		
		return new PreferencesDTO(
			user.getId(),
			user.getDecimalPlaces(),
			user.getLanguage()
		);
    }
}
