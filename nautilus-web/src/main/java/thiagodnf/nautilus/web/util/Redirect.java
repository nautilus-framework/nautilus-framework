package thiagodnf.nautilus.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.service.FlashMessageService;

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
