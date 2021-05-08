package org.nautilus.web.controller;

import javax.validation.Valid;

import org.nautilus.web.dto.SettingsDTO;
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
	private Redirect redirect;
	
	protected String showForm(Model model, SettingsDTO settingsDTO) {
        
        model.addAttribute("availableTimeZones", TimeZones.getAvailableTimeZones());
        model.addAttribute("settingsDTO", settingsDTO);
        
        return "users/settings";
    }

    @GetMapping("")
    public String showForm(Model model) {
        return showForm(model, userService.getSettingsDTO());
    }
	
	@PostMapping("/save")
    public String update(Model model, RedirectAttributes ra, @Valid SettingsDTO settingsDTO, BindingResult form) {
        
        if (form.hasErrors()) {
            return showForm(model, settingsDTO);
        }
        
        userService.updateSettings(settingsDTO);
        
        return redirect.to("/settings?lang=" + settingsDTO.getLanguage()).withSuccess(ra, Messages.SETTINGS_SAVE_SUCCESS);
    }	
}
