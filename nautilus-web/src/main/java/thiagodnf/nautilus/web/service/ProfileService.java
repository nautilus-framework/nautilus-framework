package thiagodnf.nautilus.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.web.model.Profile;
import thiagodnf.nautilus.web.repository.ProfileRepository;

@Service
public class ProfileService {
	
	@Autowired
	private ProfileRepository profileRepository;

	public Profile save(Profile profile) {
		return this.profileRepository.save(profile);
	}

	public Profile findById(String id) {
		return this.profileRepository.findById(id).get();
	}
	
	public void delete(Profile profile) {
		this.profileRepository.delete(profile);
	}
}
