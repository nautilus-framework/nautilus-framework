package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.util.Messages;

@Controller
public class LoginController {

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@RequestMapping("/login")
	public String index(Model model, 
			RedirectAttributes ra,
			@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "logout", required = false) String logout) {
		
		if (securityService.isUserLogged()) {
			return "redirect:/home";
		}
		
		model.addAttribute("user", new User());
		
		if (error != null) {
			flashMessageService.error(ra, Messages.USER_LOGIN_ERROR);
		}
		
		if (logout != null) {
			flashMessageService.success(ra, Messages.USER_LOGGOUT_SUCCESS);
			return "redirect:/login";
		}
		
		return "login";
	}
}
