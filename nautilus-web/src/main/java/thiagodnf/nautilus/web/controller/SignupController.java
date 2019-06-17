package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.dto.SignupDTO;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.EmailService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.SignupService;
import thiagodnf.nautilus.web.util.Messages;

@Controller
@RequestMapping("/signup")
public class SignupController {
	
	@Autowired
	private SignupService signupService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@Autowired
	private SecurityService securityService;
	
	@Value("${server.use.confirmation.token}")
    private boolean useConfirmationToken;
	
	@GetMapping("")
	public String form(SignupDTO signupDTO, Model model) {
	
		if (securityService.isUserLogged()) {
			return "redirect:/home";
		}
		
		model.addAttribute("signupDTO", signupDTO);
		
		return "signup";
	}
	
	@PostMapping("/save")
	public String save(@Valid SignupDTO signupDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {

		if (bindingResult.hasErrors()) {
			return form(signupDTO, model);
		}
		
		User saved = signupService.create(signupDTO);
		
		if (useConfirmationToken) {

			emailService.sendConfirmationMail(saved);

			flashMessageService.success(ra, Messages.USER_CONFIRMATION_EMAIL, saved.getEmail());

			return "redirect:/login";
		}

		return "redirect:/user/confirmation?token=" + saved.getConfirmationToken();
	}
}
