package org.nautilus.web.feature.user.controller;

import javax.validation.Valid;

import org.nautilus.web.feature.user.dto.SignupDTO;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.feature.user.service.SecurityService;
import org.nautilus.web.feature.user.service.SignupService;
import org.nautilus.web.service.EmailService;
import org.nautilus.web.util.Messages;
import org.nautilus.web.util.Redirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController extends BasicController{
	
	@Autowired
	private SignupService signupService;
	
	@GetMapping("")
	public String form(SignupDTO signupDTO, Model model) {
	
		if (securityService.isUserLogged()) {
			return redirect.to("/home").withNoMessage();
		}
		
		model.addAttribute("signupDTO", signupDTO);
		
		return "user/signup";
	}
	
	@PostMapping("/save")
	public String save(@Valid SignupDTO signupDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {

		if (bindingResult.hasErrors()) {
			return form(signupDTO, model);
		}
		
		User saved = signupService.create(signupDTO);
		
		return redirect.to("/user/confirmation?token=" + saved.getConfirmationToken()).withNoMessage();
	}
}
