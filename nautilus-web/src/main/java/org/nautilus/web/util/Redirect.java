package org.nautilus.web.util;

import org.nautilus.web.service.FlashMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
public class Redirect {

    @Autowired
    private FlashMessageService flashMessageService;

    private String target;

    public Redirect to(String target) {
        this.target = target;
        return this;
    }

    public String withSuccess(RedirectAttributes ra, String message, String... args) {

        flashMessageService.success(ra, message, args);

        return "redirect:" + target;
    }

    public String withError(RedirectAttributes ra, String message, String... args) {

        flashMessageService.error(ra, message, args);

        return "redirect:" + target;
    }
    
    public String withNoMessage() {
        return "redirect:" + target;
    }

}
