package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import thiagodnf.nautilus.web.exception.EmailAlreadyUsedException;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@PostMapping("/user/signup")
	public String signup(@Valid User user, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "index";
		}

		if (userService.findByEmail(user.getEmail()) != null) {
			throw new EmailAlreadyUsedException();
		}
		
		userService.save(user);
		
		securityService.autologin(user.getEmail(), user.getPassword());

		return "redirect:/home";
	}
}
