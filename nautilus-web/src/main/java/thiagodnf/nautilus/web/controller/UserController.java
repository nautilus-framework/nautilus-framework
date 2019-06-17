package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

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

import thiagodnf.nautilus.web.dto.UserDTO;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.RoleService;
import thiagodnf.nautilus.web.service.UserService;
import thiagodnf.nautilus.web.util.Messages;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("/confirmation")
	public String confirmation(@RequestParam(required = true) String token, RedirectAttributes ra, Model model) {
	
		User user = userService.findByConfirmationToken(token);
		
		if (user == null) {
			flashMessageService.error(ra, Messages.USER_CONFIRMATION_TOKEN_FAIL);
		} else {

			// The token was found and now the user should be able to perform the log in
			userService.confirm(user);

			// Show a success message to user
			flashMessageService.success(ra, Messages.USER_CONFIRMATION_TOKEN_SUCCESS);
		}
		
		return "redirect:/login";
	}
	
	@PostMapping("/update")
	public String update(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {
		
		if (bindingResult.hasErrors()) {
			return form(userDTO, model);
		}
		
		userService.update(userDTO);
		
		flashMessageService.success(ra, Messages.USER_SAVE_SUCCESS);
		
		return "redirect:/admin/users";
	}
	
	@GetMapping("/edit/{id:.+}")
	public String edit(@PathVariable("id") String id, Model model) {
		return form(userService.findById(id), model);
	}
	
	@PostMapping("/delete/{id:.+}")
	public String deleteById(@PathVariable String id, RedirectAttributes ra, Model model) {

		userService.deleteById(id);

		flashMessageService.success(ra, Messages.USER_DELETE_SUCCESS);

		return "redirect:/admin/users";
	}
	
	private String form(UserDTO userDTO, Model model) {
		
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("roleDTOs", roleService.findAll());
		
		return "userForm";
	}
}
