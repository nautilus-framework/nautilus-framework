package org.nautilus.web.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.nautilus.web.exception.AbstractRedirectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class FlashMessageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlashMessageService.class);
	
	@Autowired
	private MessageSource messageSource;
	
	public void message(RedirectAttributes ra, String type, String title, List<String> messages) {
		
		title = messageSource.getMessage(title, null, LocaleContextHolder.getLocale());
		
		ra.addFlashAttribute("flashType", type );
		ra.addFlashAttribute("flashTitle", title);
		
		if (messages.size() == 1) {
			ra.addFlashAttribute("flashMessage", messages.get(0));
		} else {
			StringBuffer buffer = new StringBuffer();

			buffer.append("<br>");
			buffer.append("<ul>");

			for (String message : messages) {
				buffer.append("<li>").append(message).append("</li>");
			}

			buffer.append("</ul>");

			ra.addFlashAttribute("flashMessage", buffer.toString());
		}
		
		LOGGER.info(title + " " + ra.getFlashAttributes().get("flashMessage"));
	}

	public void message(RedirectAttributes ra, String type, String title, String message, String... args) {
		
		message = messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
		
		message(ra, type, title, Arrays.asList(message));
	}
	
	public void success(RedirectAttributes ra, String message, String... args) {
		message(ra, "alert alert-success alert-dismissible fade show", "alert.success-title", message, args);
	}
	
	public void error(RedirectAttributes ra, String message, String... args) {
		message(ra, "alert alert-danger alert-dismissible fade show", "alert.error-title", message, args);
	}
	
	public void error(RedirectAttributes ra, List<ObjectError> errors) {
		message(ra, "alert alert-danger alert-dismissible fade show", "alert.error-title", errors
				.stream()
				.map(e -> e.getDefaultMessage())
				.collect(Collectors.toList()));
	}

	public void error(RedirectAttributes ra, AbstractRedirectException ex) {

		ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

		String reason = "exception.NoMessage";

		if (status != null) {
			reason = status.reason();
		}
		
		error(ra, reason);
	}
}
