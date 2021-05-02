package org.nautilus.web.feature.user.controller;

import javax.validation.Valid;

import org.nautilus.web.feature.user.constant.DecimalSeparator;
import org.nautilus.web.feature.user.constant.Language;
import org.nautilus.web.feature.user.dto.SettingsDTO;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.feature.user.service.UserService;
import org.nautilus.web.util.Messages;
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
public class SettingsController extends BasicController {
	
	@Autowired
    private UserService userService;
	
	private String form(Model model, SettingsDTO settingsDTO) {
        
        model.addAttribute("settingsDTO", settingsDTO);
        model.addAttribute("decimalSeparators", DecimalSeparator.values());
        model.addAttribute("languages", Language.values());
        
        return "user/settings";
    }

	@GetMapping("")
    public String showProfile(Model model) {
        return form(model, userService.getSettings());
    }
	
	@PostMapping("/update")
    public String update(Model model, @Valid SettingsDTO userSettingsDTO, BindingResult form, RedirectAttributes ra) {
        
        if (form.hasErrors()) {
            return form(model, userSettingsDTO);
        }
        
        userService.saveSettings(userSettingsDTO);
        
        return redirect.to("/settings?lang=" + userSettingsDTO.getLanguage()).withSuccess(ra, Messages.SETTINGS_SAVE_SUCCESS);
    }
}
