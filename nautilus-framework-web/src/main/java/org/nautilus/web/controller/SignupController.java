package org.nautilus.web.controller;

import javax.validation.Valid;

import org.nautilus.web.dto.SignupDTO;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.service.UserService;
import org.nautilus.web.util.Redirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private Redirect redirect;
	
	@Autowired
	private SecurityService securityService;
	
	@GetMapping("")
	public String form(SignupDTO signupDTO, Model model) {
	
		if (securityService.isUserLogged()) {
			return redirect.to("/home").withNoMessage();
		}
		
		model.addAttribute("signupDTO", signupDTO);
		
		return "users/signup";
	}
	
	@PostMapping("/save")
	public String save(@Valid SignupDTO signupDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {

		if (bindingResult.hasErrors()) {
			return form(signupDTO, model);
		}
		
		userService.create(signupDTO);
		
		return redirect.to("/login").withNoMessage();
	}
}
