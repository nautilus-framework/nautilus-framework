package org.nautilus.web.controller;

import org.nautilus.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	public String showUsers(Model model){
		
		model.addAttribute("users", userService.findAll());
		
		return "admin";
	}
}
