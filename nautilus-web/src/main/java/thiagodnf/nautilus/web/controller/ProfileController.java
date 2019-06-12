package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.model.Profile;
import thiagodnf.nautilus.web.model.Role;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.EmailService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.ProfileService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.util.Messages;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("")
	public String show(Model model) {
		
		User user = securityService.getLoggedUser().getUser();
		
		model.addAttribute("user", user);
		model.addAttribute("profile", profileService.findById(user.getProfile().getId()));
		
		return "profile";
	}
	
	@GetMapping("/edit")
	public String edit(Model model) {
		
		User user = securityService.getLoggedUser().getUser();
		
		model.addAttribute("profile", profileService.findById(user.getProfile().getId()));
		
		return "profileForm";
	}
	
	@PostMapping("/save")
	public String save(
			@Valid Profile profile,
			BindingResult bindingResult, 
			RedirectAttributes ra, 
			Model model) {

		if (bindingResult.hasErrors()) {
			return "profileForm";
		}

		Profile saved = profileService.save(profile);

		flashMessageService.success(ra, Messages.PROFILE_SAVE_SUCCESS);
		
		return "redirect:/profile";
	}
}
