package org.nautilus.web.controller;

import org.nautilus.web.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@Autowired
	private SecurityService securityService;
	
	@GetMapping("/")
	public String index(Model model) {

		if (securityService.isUserLogged()) {
			return "redirect:/home";
		}
		
		return "index";
	}
	
	@GetMapping("/forms")
    public String forms(Model model) {
	    return "forms";
	}
}
