package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.SecurityService;

@Controller
public class IndexController {

	@Autowired
	private SecurityService securityService;

	@GetMapping("/")
	public String index(Model model) {

		if (securityService.isUserLogged()) {
			return "redirect:/home";
		}

		model.addAttribute("user", new User());

		return "index";
	}
}
