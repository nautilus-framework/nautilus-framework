package org.nautilus.web.controller;

import javax.validation.Valid;

import org.nautilus.web.persistence.constant.DecimalSeparator;
import org.nautilus.web.persistence.constant.Language;
import org.nautilus.web.persistence.dto.UserSettingsDTO;
import org.nautilus.web.persistence.model.User;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.service.UserService;
import org.nautilus.web.util.Messages;
import org.nautilus.web.util.Redirect;
import org.nautilus.web.util.TimeZones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/settings")
public class SettingsController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private Redirect redirect;
	
    @GetMapping("")
    public String showProfile(Model model) {

        User user = securityService.getLoggedUser().getUser();

        return form(userService.findUserSettingsDTOById(user.getId()), model);
    }
	
	@PostMapping("/update")
    public String update(@Valid UserSettingsDTO userSettingsDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {
        
        if (bindingResult.hasErrors()) {
            return form(userSettingsDTO, model);
        }
        
        User user = securityService.getLoggedUser().getUser();
        
        userService.updateUserSettings(user.getId(), userSettingsDTO);
        
        return redirect.to("/settings?lang=" + userSettingsDTO.getLanguage()).withSuccess(ra, Messages.SETTINGS_SAVE_SUCCESS);
    }
	
	private String form(UserSettingsDTO userSettingsDTO, Model model) {
	    
	    model.addAttribute("availableTimeZones", TimeZones.getAvailableTimeZones());
        model.addAttribute("userSettingsDTO", userSettingsDTO);
        
        model.addAttribute("decimalSeparators", DecimalSeparator.values());
        model.addAttribute("languages", Language.values());
        
        return "settings";
    }
}
