package thiagodnf.nautilus.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.web.exception.RoleNotFoundException;
import thiagodnf.nautilus.web.model.Role;
import thiagodnf.nautilus.web.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	public Role save(Role role) {
		return this.roleRepository.save(role);
	}

	public Role findByName(String name) {
		return this.roleRepository.findByName(name);
	}
	
	public List<Role> findAll(){
		return this.roleRepository.findAll();
	}
	
	public Role findById(String id) {
		return this.roleRepository.findById(id).orElseThrow(RoleNotFoundException::new);
	}
	
	public void delete(Role role) {
		this.roleRepository.delete(role);
	}
}
