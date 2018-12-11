package thiagodnf.nautilus.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class FlashMessageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlashMessageService.class);
	
	@Autowired
	private MessageSource messageSource;
	
	public void message(RedirectAttributes ra, String type, String title, String message, String... args) {

		title = messageSource.getMessage(title, null, LocaleContextHolder.getLocale());
		message = messageSource.getMessage(message, args, LocaleContextHolder.getLocale());

		ra.addFlashAttribute("type", type);
		ra.addFlashAttribute("title", title);
		ra.addFlashAttribute("message", message);
		
		LOGGER.info(title + " " + message);
	}

	public void error(RedirectAttributes ra, String message, String... args) {
		message(ra, "alert alert-danger alert-dismissible fade show", "alert.error.title", message, args);
	}
	
	public void success(RedirectAttributes ra, String message, String... args) {
		message(ra, "alert alert-success alert-dismissible fade show", "alert.success.title", message, args);
	}
	
}
