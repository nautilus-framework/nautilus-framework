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

import thiagodnf.nautilus.web.dto.UserDisplayDTO;
import thiagodnf.nautilus.web.dto.UserProfileDTO;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.UserService;
import thiagodnf.nautilus.web.util.Messages;
import thiagodnf.nautilus.web.util.Redirect;

@Controller
@RequestMapping("/settings")
public class SettingsController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private Redirect redirect;
	
    @GetMapping({ "", "/profile" })
    public String showProfile(Model model) {

        User user = securityService.getLoggedUser().getUser();

        model.addAttribute("userProfileDTO", userService.findUserProfileDTOById(user.getId()));

        return "settings-profile";
    }
	
	@PostMapping("/profile/update")
    public String update(@Valid UserProfileDTO userProfileDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {
        
        if (bindingResult.hasErrors()) {
            return "settings-profile";
        }
        
        userService.updateUserProfile(userProfileDTO);
        
        return redirect.to("/settings/profile").withSuccess(ra, Messages.SETTINGS_SAVE_SUCCESS);
    }
	
	@GetMapping("/display")
	public String showDisplay(Model model){
		
		User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("userDisplayDTO", userService.findUserDisplayDTOById(user.getId()));
		
		return "settings-display";
	}
	
	@PostMapping("/display/update")
    public String update(@Valid UserDisplayDTO userDisplayDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {
        
        if (bindingResult.hasErrors()) {
            return "settings-display";
        }
        
        userService.updateUserDisplay(userDisplayDTO);
        
        return redirect.to("/settings/display?lang=" + userDisplayDTO.getLanguage()).withSuccess(ra, Messages.SETTINGS_SAVE_SUCCESS);
    }
}
