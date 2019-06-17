package thiagodnf.nautilus.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thiagodnf.nautilus.web.dto.RoleDTO;
import thiagodnf.nautilus.web.dto.UserDTO;
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
	private BCryptPasswordEncoder passwordEncoder;

	public User create(User user) {

		user.setId(null);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userRepository.save(user);
	}
	
	public void confirm(User user) {
		
		user.setEnabled(true);
		
		userRepository.save(user);
	}
	
	public void update(UserDTO userDTO) {

		User found = findUserById(userDTO.getId());

		found.setRoleId(userDTO.getRoleId());
		found.setFirstname(userDTO.getFirstname());
		found.setLastname(userDTO.getLastname());
		found.setEnabled(userDTO.isEnabled());
		found.setAccountNonExpired(userDTO.isAccountNonExpired());
		found.setCredentialsNonExpired(userDTO.isCredentialsNonExpired());
		found.setAccountNonLocked(userDTO.isAccountNonLocked());

		save(found);
	}
	
	public User save(User user) {

		if (!user.isEditable()) {
			throw new UserNotEditableException();
		}

		return userRepository.save(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken);
	}
	
	public UserDTO findById(String id) {
		return convertToDTO(findUserById(id));
	}
	
	public void deleteById(String id) {

		User found = findUserById(id);

		if (!found.isEditable()) {
			throw new UserNotEditableException();
		}

		userRepository.delete(found);
	}
	
	public User findUserById(String id) {
		return this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
	
	public List<User> findUserByRoleId(String id) {
		return this.userRepository.findByRoleId(id);
    }
	
	public List<UserDTO> findAll() {
		return convertToDTOs(userRepository.findAll());
	}
	
	public UserDTO convertToDTO(User user) {
		
		if(user == null) {
			return null;
		}
		
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
			user.isCredentialsNonExpired()
		);
    }
	
	private List<UserDTO> convertToDTOs(List<User> models) {
		return models.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
}