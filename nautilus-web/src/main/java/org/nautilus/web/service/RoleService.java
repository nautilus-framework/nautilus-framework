package org.nautilus.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.nautilus.web.dto.RoleDTO;
import org.nautilus.web.exception.RoleNotEditableException;
import org.nautilus.web.exception.RoleNotFoundException;
import org.nautilus.web.model.Role;
import org.nautilus.web.model.User;
import org.nautilus.web.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {
	
	@Autowired
	private RoleRepository repository;
	
	@Autowired
	private UserService userService;

	public RoleDTO create(RoleDTO roleDTO) {

		Role unSaved = convertFromDTO(roleDTO);
		
		unSaved.setId(null);

		Role saved = repository.save(unSaved);

		return convertToDTO(saved);
	}
	
	public RoleDTO update(RoleDTO roleDTO) {
		
		Role found = findRoleById(roleDTO.getId());
		
		if (!found.isEditable()) {
			throw new RoleNotEditableException();
		}
		
		found.setName(roleDTO.getName());
		found.setPrivileges(roleDTO.getPrivileges());
		
		Role updated = repository.save(found);
        
		return convertToDTO(updated);
	}

	public RoleDTO findRoleDTOByName(String name) {
		return convertToDTO(this.repository.findByName(name));
	}
	
	public List<RoleDTO> findAll() {
		return convertToDTOs(repository.findAll());
	}
	
	public RoleDTO findById(String id) {
		return convertToDTO(findRoleById(id));
	}
	
	public RoleDTO deleteById(String id) {
		
		Role found = findRoleById(id);
		
		if (!found.isEditable()) {
			throw new RoleNotEditableException();
		}
	   
		repository.delete(found);
		
		List<User> users = userService.findUsersByRoleId(found.getId());
		
		RoleDTO userRole = findRoleDTOByName(Role.USER);
		
		for(User user : users) {
			
		    user.setRoleId(userRole.getId());
			
			userService.save(user);
		}
		
		return convertToDTO(found);
	}
	
	private Role findRoleById(String id) {
		return this.repository.findById(id).orElseThrow(RoleNotFoundException::new);
    }
	
	private Role convertFromDTO(RoleDTO roleDTO) {
		
		Role role = new Role();

		role.setId(roleDTO.getId());
		role.setName(roleDTO.getName());
		role.setPrivileges(roleDTO.getPrivileges());
		
		return role;
    }
	
	private RoleDTO convertToDTO(Role role) {
		
		if(role == null) return null;
		
		return new RoleDTO(
			role.getId(), 
			role.getName(), 
			role.getPrivileges()
		);
    }
	
	private List<RoleDTO> convertToDTOs(List<Role> models) {
		return models.stream().map(this::convertToDTO).collect(Collectors.toList());
	}	
}
