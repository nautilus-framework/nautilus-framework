package org.nautilus.web.feature.user.controller;

import javax.validation.Valid;

import org.nautilus.web.feature.user.dto.SignupDTO;
import org.nautilus.web.feature.user.service.SignupService;
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
@RequestMapping("/signup")
public class SignupController extends BasicController {

    @Autowired
    private SignupService signupService;

    @GetMapping("")
    public String form(Model model, SignupDTO signupDTO) {

        if (securityService.isUserLogged()) {
            return redirect.to("/home").withNoMessage();
        }

        model.addAttribute("signupDTO", signupDTO);

        return "user/signup";
    }

    @PostMapping("/save")
    public String save(Model model, RedirectAttributes ra, @Valid SignupDTO signupDTO, BindingResult form) {

        if (form.hasErrors()) {
            return form(model, signupDTO);
        }

        signupService.create(signupDTO);

        return redirect.to("/login").withSuccess(ra, Messages.USER_CONFIRMATION_TOKEN_SUCCESS);
    }
}
