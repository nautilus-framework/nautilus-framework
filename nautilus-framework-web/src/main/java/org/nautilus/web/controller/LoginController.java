package org.nautilus.web.controller;

import org.nautilus.web.dto.LoginDTO;
import org.nautilus.web.model.User;
import org.nautilus.web.service.FlashMessageService;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@RequestMapping("")
	public String index(Model model, RedirectAttributes ra,
			@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "logout", required = false) String logout) {
		
		if (securityService.isUserLogged()) {
			return "redirect:/home";
		}
		
		model.addAttribute("loginDTO", new LoginDTO("", ""));
		
		if (error != null) {
			flashMessageService.error(ra, Messages.LOGIN_ERROR);
		}
		
		if (logout != null) {
			flashMessageService.success(ra, Messages.LOGGOUT_SUCCESS);
			return "redirect:/login";
		}
		
		return "users/login";
	}
	
	@RequestMapping("/success")
	public String success() {

		User user = securityService.getLoggedUser().getUser();

		return "redirect:/home?lang=" + user.getLanguage();
	}
}
