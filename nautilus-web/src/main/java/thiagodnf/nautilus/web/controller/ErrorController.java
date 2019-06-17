package thiagodnf.nautilus.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.exceptions.TemplateInputException;

import thiagodnf.nautilus.web.exception.AbstractRedirectException;
import thiagodnf.nautilus.web.exception.PageException;
import thiagodnf.nautilus.web.service.FlashMessageService;

@ControllerAdvice
public class ErrorController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@ExceptionHandler(AbstractRedirectException.class)
	public String handleRedirectException(HttpServletRequest req, AbstractRedirectException ex, RedirectAttributes ra) {
		
		LOGGER.info("Handling redirecting exception");
		
		ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

		String reason = "exception.NoMessage";

		if (status != null) {
			reason = status.reason();
		}
		
		flashMessageService.error(ra, reason);

		return "redirect:" + ex.getRedirectTo();
	}
	
	@ExceptionHandler(TemplateInputException.class)
	public ModelAndView handleTemplateInputException(HttpServletRequest req, TemplateInputException ex) {
	
		LOGGER.info("Handling TemplateInputException");
		
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
	
	@ExceptionHandler(PageException.class)
	public ModelAndView handlePageException(HttpServletRequest req, PageException ex) {

		LOGGER.info("Handling page exception");
		
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
