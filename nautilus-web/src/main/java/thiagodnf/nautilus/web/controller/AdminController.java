package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.service.RoleService;
import thiagodnf.nautilus.web.service.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize ("hasAuthority('SHOW_ADMIN_PAGE')")
public class AdminController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	public String show(Model model){
		
		model.addAttribute("roles", roleService.findAll());
		model.addAttribute("users", userService.findAll());
		
		return "admin";
	}
}
