package thiagodnf.nautilus.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.exception.PageException;
import thiagodnf.nautilus.web.exception.AbstractRedirectException;
import thiagodnf.nautilus.web.service.FlashMessageService;

@ControllerAdvice
public class ErrorController {

	@Autowired
	private FlashMessageService flashMessageService;
	
	@ExceptionHandler(AbstractRedirectException.class)
	public String handleRedirectException(HttpServletRequest req, AbstractRedirectException ex, RedirectAttributes ra) {
		
		ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

		String reason = "exception.NoMessage";

		if (status != null) {
			reason = status.reason();
		}
		
		flashMessageService.error(ra, reason);

		return "redirect:" + ex.getRedirectTo();
	}
	
	@ExceptionHandler(PageException.class)
	public ModelAndView handlePageException(HttpServletRequest req, PageException ex) {

		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.

		ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

		ex.printStackTrace();
		
		if (status != null) {
			throw ex;
		}

		// Otherwise setup and send the user to a default error-view.
		ModelAndView mav = new ModelAndView();

		mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		mav.addObject("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		mav.addObject("exception", ex);
		mav.addObject("timestamp", System.currentTimeMillis());
		mav.addObject("message", ex.getMessage());
		mav.addObject("url", req.getRequestURL());
		mav.addObject("stackTrace", ExceptionUtils.getStackTrace(ex));
		mav.setViewName("error");

		return mav;
	}
}
