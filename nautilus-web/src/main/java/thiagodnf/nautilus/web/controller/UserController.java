package thiagodnf.nautilus.web.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.model.Role;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.EmailService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.RoleService;
import thiagodnf.nautilus.web.service.SecurityService;
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
	private EmailService emailService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@Autowired
	private SecurityService securityService;
	
	@Value("${server.use.confirmation.token}")
    private boolean useConfirmationToken;
	
	@GetMapping("/signup")
	public String signup(Model model) {
	
		if (securityService.isUserLogged()) {
			return "redirect:/home";
		}
		
		model.addAttribute("user", new User());
		
		return "signup";
	}
	
	@PostMapping("/save")
	public String save(@Valid User user, BindingResult bindingResult, RedirectAttributes ra, Model model) {

		if (bindingResult.hasErrors()) {
			return "signup";
		}
		
		user.setRole(roleService.findByName(Role.USER));
		user.setConfirmationToken(UUID.randomUUID().toString());

		if (useConfirmationToken) {
			user.setEnabled(false);
		} else {
			user.setEnabled(true);
		}

		User saved = userService.signup(user);

		if (useConfirmationToken) {
			emailService.sendConfirmationMail(user);
			flashMessageService.success(ra, Messages.USER_CONFIRMATION_EMAIL, saved.getEmail());
		} else {
			flashMessageService.success(ra, Messages.USER_CONFIRMATION_TOKEN_SUCCESS);
		}
		
		return "redirect:/login";
	}
	
	@GetMapping("/confirmation")
	public String confirmation(@RequestParam(required = true) String token, RedirectAttributes ra, Model model) {
	
		User user = userService.findByConfirmationToken(token);
		
		if (user == null) {
			flashMessageService.error(ra, Messages.USER_CONFIRMATION_TOKEN_FAIL);
		}else {
			
			// The token was found and now the user should be able to perform the log in
			user.setEnabled(true);
			userService.save(user);
			
			// Show a success message to user
			flashMessageService.success(ra, Messages.USER_CONFIRMATION_TOKEN_SUCCESS);
		}
		
		return "redirect:/login";
	}
}
