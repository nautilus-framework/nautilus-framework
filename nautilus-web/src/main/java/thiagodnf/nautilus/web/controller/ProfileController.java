package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.dto.ProfileDTO;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.ProfileService;
import thiagodnf.nautilus.web.util.Messages;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@PostMapping("/update")
	public String update(@Valid ProfileDTO profileDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {
		
		if (bindingResult.hasErrors()) {
			return "settings-profile";
		}
		
		profileService.update(profileDTO);
		
		flashMessageService.success(ra, Messages.SETTINGS_SAVE_SUCCESS);
		
		return "redirect:/settings/profile";
	}
}
