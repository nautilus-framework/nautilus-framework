package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.PreferencesService;
import thiagodnf.nautilus.web.service.ProfileService;
import thiagodnf.nautilus.web.service.SecurityService;

@Controller
@RequestMapping("/settings")
public class SettingsController {
	
	@Autowired
	private PreferencesService preferenceService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private SecurityService securityService;
	
	@GetMapping("")
	public String show(Model model){
		return showProfile(model);
	}
	
	@GetMapping("/profile")
	public String showProfile(Model model){
		
		User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("profileDTO", profileService.findById(user.getId()));
		
		return "settings-profile";
	}
	
	@GetMapping("/preferences")
	public String showRoles(Model model){
		
		User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("preferencesDTO", preferenceService.findById(user.getId()));
		
		return "settings-preferences";
	}
}
