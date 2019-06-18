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

import thiagodnf.nautilus.web.dto.SettingsPreferencesDTO;
import thiagodnf.nautilus.web.dto.SettingsProfileDTO;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.PreferencesService;
import thiagodnf.nautilus.web.service.ProfileService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.util.Messages;

@Controller
@RequestMapping("/settings")
public class SettingsController {
	
	@Autowired
	private PreferencesService preferencesService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
    private FlashMessageService flashMessageService;
	
    @GetMapping({ "", "/profile" })
	public String showProfile(Model model){
		
		User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("profileDTO", profileService.findById(user.getId()));
		
		return "settings-profile";
	}
	
	@GetMapping("/preferences")
	public String showRoles(Model model){
		
		User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("preferencesDTO", preferencesService.findById(user.getId()));
		
		return "settings-preferences";
	}
	
	@PostMapping("/profile/update")
    public String update(@Valid SettingsProfileDTO profileDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {
        
        if (bindingResult.hasErrors()) {
            return "settings-profile";
        }
        
        profileService.update(profileDTO);
        
        flashMessageService.success(ra, Messages.SETTINGS_SAVE_SUCCESS);
        
        return "redirect:/settings/profile";
    }
	
	@PostMapping("/preferences/update")
    public String update(@Valid SettingsPreferencesDTO preferencesDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {
        
        if (bindingResult.hasErrors()) {
            return "settings-preferences";
        }
        
        preferencesService.update(preferencesDTO);
        
        flashMessageService.success(ra, Messages.SETTINGS_SAVE_SUCCESS);
        
        return "redirect:/settings/preferences?lang=" + preferencesDTO.getLanguage();
    }
}
