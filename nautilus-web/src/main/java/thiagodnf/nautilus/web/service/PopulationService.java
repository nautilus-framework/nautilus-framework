package thiagodnf.nautilus.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.web.model.Population;
import thiagodnf.nautilus.web.repository.PopulationRepository;

@Service
public class PopulationService {
	
	@Autowired
	private PopulationRepository populationRepository;
	
	
	public Population save(Population parentoFront) {
		return this.populationRepository.save(parentoFront);
	}
}
