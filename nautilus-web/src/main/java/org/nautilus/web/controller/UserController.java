package org.nautilus.web.controller;

import javax.validation.Valid;

import org.nautilus.web.dto.UserDTO;
import org.nautilus.web.model.User;
import org.nautilus.web.service.RoleService;
import org.nautilus.web.service.UserService;
import org.nautilus.web.util.Messages;
import org.nautilus.web.util.Redirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
    private Redirect redirect;
	
	@GetMapping("/confirmation")
	public String confirmation(@RequestParam(required = true) String token, RedirectAttributes ra, Model model) {
	
		User user = userService.findByConfirmationToken(token);
		
		userService.confirm(user);

		return redirect.to("/login").withSuccess(ra, Messages.USER_CONFIRMATION_TOKEN_SUCCESS);
	}
	
	@PostMapping("/update")
	public String update(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {
		
	    if (bindingResult.hasErrors()) {
			return form(userDTO, model);
		}
		
		userService.updateUser(userDTO);
		
		return redirect.to("/admin").withSuccess(ra, Messages.USER_SAVED_SUCCESS);
	}
	
	@GetMapping("/edit/{id:.+}")
	public String edit(@PathVariable String id, Model model) {
		return form(userService.findUserDTOById(id), model);
	}
	
    @PostMapping("/delete/{id:.+}")
    public String deleteById(@PathVariable String id, RedirectAttributes ra, Model model) {

        userService.deleteById(id);

        return redirect.to("/admin").withSuccess(ra, Messages.USER_DELETED_SUCCESS);
    }
	
	private String form(UserDTO userDTO, Model model) {
		
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("roleDTOs", roleService.findAll());
		
		return "form-user";
	}
}
