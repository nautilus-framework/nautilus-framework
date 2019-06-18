package thiagodnf.nautilus.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thiagodnf.nautilus.web.dto.SettingsPreferencesDTO;
import thiagodnf.nautilus.web.model.User;

@Service
@Transactional
public class PreferencesService {
	
	@Autowired
	private UserService userService;
	
	public void update(SettingsPreferencesDTO preferencesDTO) {

		User found = userService.findUserById(preferencesDTO.getId());

		found.setDecimalPlaces(preferencesDTO.getDecimalPlaces());
		found.setDecimalSeparator(preferencesDTO.getDecimalSeparator());
		found.setLanguage(preferencesDTO.getLanguage());
		
		userService.save(found);
	}

	public SettingsPreferencesDTO findById(String id) {
		return convertToDTO(userService.findUserById(id));
	}
	
	public SettingsPreferencesDTO convertToDTO(User user) {
		
		if(user == null)  return null;
		
		return new SettingsPreferencesDTO(
			user.getId(),
			user.getDecimalPlaces(),
			user.getDecimalSeparator(),
			user.getLanguage()
		);
    }
}
